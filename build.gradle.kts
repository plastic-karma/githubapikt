import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version("1.3.50")
    jacoco
    id("org.jlleitschuh.gradle.ktlint") version "8.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC15"
    id("org.jetbrains.dokka") version "0.9.18"
}

group = "com.plastickarma"
version = "0.1-SNAPSHOT"

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
        events("passed", "skipped", "failed")
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
