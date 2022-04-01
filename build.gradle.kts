import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.papermc.paperweight.userdev") version "1.3.5"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "net.stckoverflw"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // PaperMC Dependency
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    // KSpigot dependency
    implementation("net.axay", "kspigot", "1.18.2")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

bukkit {
    name = "ExamplePlugin"
    apiVersion = "1.18"
    authors = listOf(
        "StckOverflw",
        "CoolePizza",
        "DQMME",
        "l4zs"
    )
    main = "$group.exampleplugin.ExamplePlugin"
    version = getVersion().toString()
    libraries = listOf(
        "net.axay:kspigot:1.18.2",
    )
}
