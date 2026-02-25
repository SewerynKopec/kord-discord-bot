
plugins {
    kotlin("jvm") version "2.3.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.kord.dev/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("dev.kord:kord-core:0.17.0")
    implementation("org.quartz-scheduler:quartz:2.4.1")

    implementation("org.slf4j:slf4j-api:2.0.13")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.13")
}

kotlin {
    jvmToolchain(25)
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "MainKt"))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}