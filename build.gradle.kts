import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "global.wavy"
version = "0.0.1-SNAPSHOT"

plugins {
    java
    kotlin("jvm") version "1.3.41"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("org.springframework.kafka:spring-kafka:2.2.9.RELEASE")

    implementation("org.slf4j:slf4j-api:1.7.28")
    implementation("ch.qos.logback:logback-core:1.2.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}