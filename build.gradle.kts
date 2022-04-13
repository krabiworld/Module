/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    java
    application
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

version = "1.3"
application {
	mainClass.set("org.module.Module")
}

repositories {
    mavenCentral()
	maven("https://m2.chew.pro/snapshots/")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("net.dv8tion:JDA:5.0.0-alpha.9") {
		exclude(module = "opus-java")
	}
    implementation("pw.chew:jda-chewtils-commons:2.0-SNAPSHOT")
	implementation("org.json:json:20220320")
	implementation("ch.qos.logback:logback-classic:1.2.11")
	implementation("org.codehaus.groovy:groovy:3.0.10")
	runtimeOnly("org.postgresql:postgresql:42.3.3")
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
