import com.jfrog.bintray.gradle.BintrayExtension.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date

plugins {
    kotlin("jvm") version("1.3.50")
    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "8.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC15"
    id("org.jetbrains.dokka") version "0.9.18"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

group = "com.plastickarma"
version = "0.0.5"

repositories {
    jcenter()
}

dependencies {

    val kotlinVersion = "1.3.61"
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))

    val coroutinesVersion = "1.3.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")

    val fuelVersion = "2.2.1"
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:$fuelVersion")

    implementation("com.beust:klaxon:5.0.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("failed")
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }
}

detekt {
    failFast = true
}

jacoco {
    toolVersion = "0.8.4"
}

val codeCoverageFiles = fileTree(baseDir = "${project.buildDir}/classes/kotlin/main") {
    exclude("**/model/*")
    exclude("**/examples/*")
}

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        xml.destination = file("$buildDir/reports/jacoco/jacocoTestReport.xml")
        html.destination = file("$buildDir/reports/jacoco")
    }

    afterEvaluate {
        classDirectories = codeCoverageFiles
    }
}

tasks {
    named<Task>("check") {
        dependsOn(named<Task>("jacocoTestReport"))
    }
    named<Task>("build") {
        dependsOn(named<Task>("dokka"))
    }
    named<Task>("jacocoTestCoverageVerification") {
        dependsOn(named<Task>("jacocoTestReport"))
    }
}

tasks.withType<JacocoCoverageVerification> {

    violationRules {
        rule {
            limit {
                counter = "LINE"
                minimum = "0.85".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                minimum = "0.85".toBigDecimal()
            }
            limit {
                counter = "COMPLEXITY"
                minimum = "0.60".toBigDecimal()
            }
        }
    }
    afterEvaluate {
        classDirectories = codeCoverageFiles
    }
}

tasks.withType<DokkaTask> {
    outputDirectory = "$buildDir/reports/javadoc"
    noJdkLink = true
    noStdlibLink = true
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}

val projectName: String = "githubapikt"

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("main")
    publish = true
    override = true

    pkg(closureOf<PackageConfig> {
        repo = "maven"
        name = "githubapikt"
        vcsUrl = "https://github.com/plastic-karma/githubapikt.git"
        version(closureOf<VersionConfig> {
            name = project.version.toString()
            desc = "Kotlin Github API wrapper"
            released = Date().toString()
        })
    })
}

publishing {
    publications {
        register("main", MavenPublication::class) {
            artifactId = projectName
            val sourcesJar by tasks.creating(Jar::class) {
                this.classifier = "sources"
                from(sourceSets["main"].allSource)
            }

            val javadocJar by tasks.creating(Jar::class) {
                this.classifier = "javadoc"
                from("$buildDir/reports/javadoc")
            }

            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)

            pom {
                name.set(projectName)
                description.set("This is an early version of a library to access Github API v3 written in Kotlin using coroutines.")
                url.set("https://github.com/plastic-karma/githubapikt/")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/plastic-karma/githubapikt/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("plastic-karma")
                        name.set("Benjamin Rogge")
                        email.set("benni.rogge@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/plastic-karma/githubapikt.git")
                    developerConnection.set("scm:git:ssh://github.com/plastic-karma/githubapikt.git")
                    url.set("https://github.com/plastic-karma/githubapikt")
                }
            }
        }
    }
}
