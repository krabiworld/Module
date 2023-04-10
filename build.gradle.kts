plugins {
    java
    application
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
}

version = "2.0"
application {
	mainClass.set("org.module.Module")
}

repositories {
    mavenCentral()
	maven("https://jitpack.io")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("net.dv8tion:JDA:5.0.0-beta.8")
	implementation("com.github.walkyst:lavaplayer-fork:1.3.99.2")
	implementation("org.json:json:20220924")
	implementation("ch.qos.logback:logback-classic:1.2.11")
	implementation("org.codehaus.groovy:groovy:3.0.14")
	runtimeOnly("org.postgresql:postgresql:42.5.1")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot {
	buildInfo()
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}
