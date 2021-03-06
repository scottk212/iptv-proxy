// common libs
val junitVersion = "5.5.1"
val jacksonVersion = "2.9.9"
val jacksonDatabindVersion = "2.9.9.3"
val janinoVersion = "3.1.0"
val logbackVersion = "1.2.3"
val slf4jVersion = "1.7.26"
val snakeYamlVersion = "1.25"
val undertowVersion = "2.0.27.Final"

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

tasks.wrapper {
    gradleVersion = "6.4"
    distributionType = Wrapper.DistributionType.ALL
}

repositories {
    mavenCentral()
}

dependencies {
    // logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:log4j-over-slf4j:$slf4jVersion")
    implementation("org.slf4j:jcl-over-slf4j:$slf4jVersion")
    implementation("org.slf4j:jul-to-slf4j:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")

    // app specific
    implementation("org.yaml:snakeyaml:$snakeYamlVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonDatabindVersion")
    implementation("io.undertow:undertow-core:$undertowVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_13
    targetCompatibility = JavaVersion.VERSION_13
}

application {
    mainClassName = "com.kvaster.iptv.App"
}

configurations.forEach {
    it.exclude("org.apache.httpcomponents", "httpclient")
    it.exclude("org.apache.httpcomponents", "httpcore")

    it.exclude("com.sun.mail", "javax.mail")
    it.exclude("javax.activation", "activation")
}
