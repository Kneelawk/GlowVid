pluginManagement {
    repositories {
        maven("https://maven.quiltmc.org/repository/release") {
            name = "Quilt"
        }
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev/") {
            name = "Architectury"
        }
        maven("https://maven.neoforged.net/releases/") {
            name = "NeoForged"
        }
        maven("https://repo.sleeping.town/") {
            name = "SleepingTown"
        }
        maven("https://maven.kneelawk.com/releases/") {
            name = "Kneelawk"
        }
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        val loom_version: String by settings
        id("fabric-loom") version loom_version
        val moddev_version: String by settings
        id("net.neoforged.moddev") version moddev_version
        val minivan_version: String by settings
        id("agency.highlysuspect.minivan") version minivan_version
        val remapcheck_version: String by settings
        id("com.kneelawk.remapcheck") version remapcheck_version
        val versioning_version: String by settings
        id("com.kneelawk.versioning") version versioning_version
        val kpublish_version: String by settings
        id("com.kneelawk.kpublish") version kpublish_version
        val submodule_version: String by settings
        id("com.kneelawk.submodule") version submodule_version
    }
}

rootProject.name = "templatemod"

fun module(enabled: Boolean, name: String) {
    if (!enabled) return
    val sanitizedName = name.replace('/', '-')
    include(sanitizedName)
    project(":$sanitizedName").projectDir = File(rootDir, "modules/$name")
}

fun module(enabled: Boolean, name: String, vararg submodules: Pair<Boolean, String>) {
    if (!enabled) return
    val sanitizedName = name.replace('/', '-')
    include(sanitizedName)
    project(":$sanitizedName").projectDir = File(rootDir, "modules/$name")

    for ((enabled, submodule) in submodules) {
        if (!enabled) continue
        val sanitizedSubmodule = submodule.replace('/', '-')
        include("$sanitizedName:$sanitizedSubmodule")
        project(":$sanitizedName:$sanitizedSubmodule").projectDir = File(rootDir, "modules/$name/$submodule")
    }
}

fun example(enabled: Boolean, name: String) {
    if (!enabled) return
    val sanitizedName = name.replace('/', '-')
    include(sanitizedName)
    project(":$sanitizedName").projectDir = File(rootDir, "examples/$name")
}

fun example(enabled: Boolean, name: String, vararg submodules: Pair<Boolean, String>) {
    if (!enabled) return
    val sanitizedName = name.replace('/', '-')
    include(sanitizedName)
    project(":$sanitizedName").projectDir = File(rootDir, "examples/$name")

    for ((enabled, submodule) in submodules) {
        if (!enabled) continue
        val sanitizedSubmodule = submodule.replace('/', '-')
        include("$sanitizedName:$sanitizedSubmodule")
        project(":$sanitizedName:$sanitizedSubmodule").projectDir = File(rootDir, "examples/$name/$submodule")
    }
}

fun javadoc(enabled: Boolean, name: String) {
    if (!enabled) return
    val sanitizedName = name.replace('/', '-')
    include("javadoc-$sanitizedName")
    project(":javadoc-$sanitizedName").projectDir = File(rootDir, "javadoc/$name")
}

val xplat = true
val intermediary = true
val fabric = true
val neoforge = true

module(xplat, "core/xplat")
module(intermediary, "core/xplat-intermediary")
module(fabric, "core/fabric")
module(neoforge, "core/neoforge")

example(xplat, "simple/xplat")
example(fabric, "simple/fabric")
example(neoforge, "simple/neoforge")
