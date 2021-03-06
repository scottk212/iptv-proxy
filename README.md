# Overview

This project is a simple iptv restreamer. For now it supports only HLS (m3u8) streams.
Some iptv providers allow to connect only one device per url and this is not really
comfortable when you have 3+ tv. Iptv-proxy allocates such 'urls' dynamically. I.e. your
iptv provider have two urls (playlists) and allows only one connection per url, but
you have 4 tv in your house and you never watch more then 2 tv at the same time.
In this case you can setup two playlists in iptv proxy and they will be dynamically
allocated to active tv.

## Configuration

```yaml
host: 127.0.0.1
port: 8080
base_url: http://127.0.0.1:8080
forwarded_pass: password
token_salt: 6r8bt67ta5e87tg7afn
connect_timeout_sec: 2
servers:
  - name: someiptv-1
    url: https://someiptv.com/playlist.m3u
    max_connections: 1
  - name: another_proxy-2
    url: https://iptv-proxy.example.com/playlist.m3u
    max_connections: 4
    send_user: true
    proxy_stream: true
    channel_failed_ms: 1000
    info_timeout_sec: 3
    info_retry_delay_ms: 500
allow_anonymous: false
users:
  - 65182_login1
  - 97897_login2
```

* `base_url` - url of your service, you may omit this (see forwarded_pass)
* `forwarded_pass` - password for Forwarded header in case iptvproxy is behind proxy
* `token_salt` - just random chars, they are used to create encrypted tokens
* `connect_timeout_sec` - http connect timeout, default value is 2 seconds and this is more than enough,
used for both info and stream connections
* `max_connections` - max active connections allowed for this playlist
* `send_user` - this is useful only when you're using cascade config - iptv-proxy behind iptv-proxy.
If 'true' then iptv-proxy will send current user name in special http header.
We need this to identify device (endpoint) - this will help us to handle max connections and
channel switching properly.
* `proxy_stream` - true (default) means proxy all data through own server,
false means using direct urls for data
* `channel_failed_ms` - on channel failure (error on downloading current m3u8 info)
it will be marked as 'failed' for some time and will be not used for any subsequent requests.
This feature should be enabled for last iptvproxy in chain (the one which connects to your iptv service)
and should be disabled in other situation
* `info_timeout_sec` - some providers may return 404 http error on m3u8 request. This setting
will trigger automatic request retry. We'll be trying to make additional requests for this period.
Default value is 3 seconds. 6 seconds is recommended for cascade proxy. 
* `info_retry_delay_ms` - delay in milliseconds between retries. Default value is 500ms  
* `allow_anonymous` - allow to connect any device without specific user name.
It is not good idea to use such setup. You really should add name for each device you're using.

iptv proxy will embed full urls in it's lists - it means we should know url from which service is accessed by user.
Url is calculated in following way:
* if forwarded_pass is enabled url is taken from Forwarded header
(nginx setup: `proxy_set_header Forwarded "pass=PASS;baseUrl=https://$host";`).
Password must match setting in iptvproxy config
* base_url is used in case it defined
* schema host and port from request is used (will not work in case iptvproxy is behind proxy)

## Device setup

On device you should use next url as dynamic playlist:

`<base_url>/m3u/<user_name>`

or

`<base_url>/m3u`

for anonymous access.
