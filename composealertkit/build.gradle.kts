import java.io.FileInputStream
import java.security.MessageDigest
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("signing")
}


val versionPropsFile = file("version.properties")
println("Version props file exists: ${versionPropsFile.exists()}")
println("Version props file path: ${versionPropsFile.absolutePath}")

val versionProps = Properties()
if (versionPropsFile.exists()) {
    versionProps.load(FileInputStream(versionPropsFile))
    println("Loaded properties: ${versionProps.propertyNames().toList()}")
}

val majorVersion = versionProps.getProperty("majorVersion", "1")
val minorVersion = versionProps.getProperty("minorVersion", "0")
val patchVersion = versionProps.getProperty("patchVersion", "0")
val libraryVersion = "$majorVersion.$minorVersion.$patchVersion"

println("========= VERSION INFO =========")
println("majorVersion: ${versionProps.getProperty("majorVersion", "1")}")
println("minorVersion: ${versionProps.getProperty("minorVersion", "0")}")
println("patchVersion: ${versionProps.getProperty("patchVersion", "0")}")
println("libraryVersion: $libraryVersion")
println("===============================")

android {
    namespace = "com.zahid.alertkit"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "VERSION_NAME", "\"$libraryVersion\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


group = "io.github.zahid4kh"
version = libraryVersion

val secretPropsFile = rootProject.file("local.properties")
val secretProps = Properties()
if (secretPropsFile.exists()) {
    secretProps.load(FileInputStream(secretPropsFile))
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.zahid4kh"
            artifactId = "compose-alert-kit"
            version = libraryVersion

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Compose Alert Kit")
                description.set("A lightweight, state-driven library for Jetpack Compose that makes showing toast messages and snackbars simple and intuitive.")
                url.set("https://github.com/zahid4kh/compose-alert-kit")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("zahid4kh")
                        name.set("Zahid Khalilov")
                        email.set("funroboticshere@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:github.com/zahid4kh/compose-alert-kit.git")
                    developerConnection.set("scm:git:ssh://github.com/zahid4kh/compose-alert-kit.git")
                    url.set("https://github.com/zahid4kh/compose-alert-kit/tree/main")
                }
            }
        }
    }
}

signing {
    val isJitPackBuild = System.getenv("JITPACK") == "true"

    if (!isJitPackBuild) {
        val signingKeyFile = rootProject.file("private_key.gpg")
        val signingPassword = secretProps.getProperty("signing.password", "")

        if (signingKeyFile.exists()) {
            useInMemoryPgpKeys(signingKeyFile.readText(), signingPassword)
        } else {
            useGpgCmd()
        }
        sign(publishing.publications["release"])
    }
}

tasks.register<Zip>("createBundle") {
    dependsOn("clean", "assemble", "publishToMavenLocal", "generateChecksums")

    archiveFileName.set("composealertkit-${libraryVersion}-bundle.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))

    from(file("${System.getProperty("user.home")}/.m2/repository")) {
        include("io/github/zahid4kh/compose-alert-kit/${libraryVersion}/**")
    }

    doLast {
        println("====================================")
        println("Bundle created at: ${archiveFile.get().asFile.absolutePath}")
        println("====================================")
    }
}

tasks.register("generateChecksums") {
    dependsOn("publishToMavenLocal", "signReleasePublication")

    doLast {
        Thread.sleep(1000)

        val repoDir = file("${System.getProperty("user.home")}/.m2/repository/io/github/zahid4kh/compose-alert-kit/${libraryVersion}")
        println("Generating checksums in directory: ${repoDir.absolutePath}")

        repoDir.listFiles()?.forEach { file ->
            if (file.isFile && !file.name.endsWith(".md5") && !file.name.endsWith(".sha1")) {
                println("Generating checksums for: ${file.name}")

                val md5File = File(file.absolutePath + ".md5")
                md5File.writeText(generateMD5(file))

                val sha1File = File(file.absolutePath + ".sha1")
                sha1File.writeText(generateSHA1(file))
            }
        }
    }
}


fun generateMD5(file: File): String {
    val md = MessageDigest.getInstance("MD5")
    file.inputStream().use { input ->
        val buffer = ByteArray(8192)
        var read: Int
        while (input.read(buffer).also { read = it } > 0) {
            md.update(buffer, 0, read)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}

fun generateSHA1(file: File): String {
    val md = MessageDigest.getInstance("SHA-1")
    file.inputStream().use { input ->
        val buffer = ByteArray(8192)
        var read: Int
        while (input.read(buffer).also { read = it } > 0) {
            md.update(buffer, 0, read)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}