import org.jetbrains.kotlin.fir.analysis.diagnostics.existing

plugins {
    kotlin("jvm") version "1.4.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
//<!-- https://mvnrepository.com/artifact/com.icegreen/greenmail -->
//<dependency>
//<groupId>com.icegreen</groupId>
//<artifactId>greenmail</artifactId>
//<version>1.6.0</version>
//<scope>test</scope>
//</dependency>

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.icegreen:greenmail:1.6.0")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}