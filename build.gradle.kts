plugins {
    val kotlinVersion = "1.9.24"

    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.23"

    id("org.openapi.generator") version "7.5.0"

    id("org.flywaydb.flyway") version "10.13.0"
}

group = "org.jaqpot"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")

    // logger
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.9")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // swagger needed dependencies
    implementation("io.swagger.core.v3:swagger-annotations:2.2.19")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // flyway
    implementation("org.flywaydb:flyway-core:10.12.0")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.12.0")

    // keycloak admin client
    implementation("org.keycloak:keycloak-admin-client:24.0.3")

    runtimeOnly("org.postgresql:postgresql")

    // tests
    // rest assured
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.4.0")
    // testcontainers
    testImplementation("org.testcontainers:postgresql:1.19.8")
    testImplementation("com.github.dasniko:testcontainers-keycloak:3.3.1")
    testImplementation("org.testcontainers:junit-jupiter:1.19.8")
}

springBoot {
    mainClass.set("org.jaqpot.api.JaqpotApiApplicationKt")
}

tasks.compileKotlin {
    dependsOn(tasks.openApiGenerate)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/openapi.yaml")
    invokerPackage.set("org.jaqpot.api")
    apiPackage.set("org.jaqpot.api")
    modelPackage.set("org.jaqpot.api.model")
    outputDir.set("${buildDir}/openapi")
    modelNameSuffix.set("Dto")
    ignoreFileOverride.set("$projectDir/.openapi-generator-ignore")
    // config options: https://openapi-generator.tech/docs/generators/kotlin-spring/
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "useBeanValidation" to "true",
            "useTags" to "true",
            "delegatePattern" to "true",
            "useSpringBoot3" to "true"
        )
    )
}

sourceSets {
    main {
        kotlin {
            srcDir("${buildDir}/openapi")
        }
    }
}


