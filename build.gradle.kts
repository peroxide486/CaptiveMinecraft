import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("com.gradleup.shadow") version "9.4.1"
}

group = "com.github.peroxide486"
version = "0.1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven { url = uri("https://repo.extendedclip.com/releases/") }
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")

    compileOnly("me.clip:placeholderapi:2.12.2")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paperweight {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<ShadowJar> {
    relocate("com.zaxxer.hikari", "com.github.peroxide486.captiveminecraft.hikari")
}
