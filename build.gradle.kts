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
    maven {
        name = "eldonexus"
        url = uri("https://eldonexus.de/repository/maven-releases/")
    }
    maven { url = uri("https://maven.enginehub.org/repo/") }
}

dependencies {
    paperweight.paperDevBundle("1.21.11-R0.1-SNAPSHOT")
    implementation("com.github.yannicklamprecht:worldborderapi:1.2111.0:dev")
    implementation("org.jdbi:jdbi3-core:3.52.1-SNAPSHOT")
    implementation("org.jdbi:jdbi3-sqlobject:3.52.1-SNAPSHOT")
    implementation("ch.jalu:configme:1.4.1")
    implementation("com.zaxxer:HikariCP:7.0.2")

    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.16")
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
    relocate("com.github.yannicklamprecht.worldborder", "com.github.peroxide486.captiveminecraft.worldborderapi")
}
