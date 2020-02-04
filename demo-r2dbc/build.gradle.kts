import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    id("com.google.cloud.tools.jib") version "2.0.0"
}

group = "com.airwallex"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    maven("https://artistry.airwallex.com/repository/lib-release/libs-release-local")
    jcenter()
    maven("https://repo.spring.io/milestone")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("ch.sbb:springboot-graceful-shutdown:2.0.1")
    implementation("com.airwallex.common:common-rx:1.1.9")
    implementation("org.springframework.boot.experimental:spring-boot-starter-data-r2dbc")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")

    // jdbc only for flyway at startup time
    runtimeOnly("org.springframework:spring-jdbc")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.boot.experimental:spring-boot-test-autoconfigure-r2dbc")
    testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot.experimental:spring-boot-bom-r2dbc:0.1.0.M3")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

jib {
    from.image = "registry.airwallex.com/awx-openjdk11-jre"
    to.image = "gcr.io/jfang-test/demo-r2dbc"
    container {
        user = "airwallex"
        group = "airwallex"
    }
}
