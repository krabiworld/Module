plugins {
    java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
    mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("net.dv8tion:JDA:5.0.0")
	implementation("dev.arbjerg:lavaplayer:2.2.0") {
		exclude(group = "commons-logging", module = "commons-logging")
	}
	implementation("org.json:json:20231013")
	implementation("org.codehaus.groovy:groovy:3.0.14")
	runtimeOnly("org.postgresql:postgresql:42.7.2")
	compileOnly("org.projectlombok:lombok:1.18.34")
	annotationProcessor("org.projectlombok:lombok:1.18.34")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

