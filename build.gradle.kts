plugins {
    id("java")
    idea
    application
}

version = "2.2"

repositories {
    mavenCentral()
}

val mainClassName = "Main"
val log4j = "2.24.3"

dependencies {
    implementation(fileTree(mapOf("include" to "*.jar", "dir" to "libs")))
    implementation("com.formdev:flatlaf:3.5.4")
    implementation("com.formdev:flatlaf-extras:3.5.4")
    implementation("com.miglayout:miglayout-swing:11.4.2")

    implementation(platform("org.apache.logging.log4j:log4j-bom:$log4j"))
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-1.2-api")
    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-jpl")
    implementation("org.apache.logging.log4j:log4j-jul")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl")

    implementation("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = mainClassName
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

tasks.jar {
    entryCompression = ZipEntryCompression.STORED
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes("Main-Class" to mainClassName)
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}