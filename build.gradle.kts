/*
 * This file is part of Module.

 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    java
    application
	id("org.springframework.boot") version "2.6.3"
    id("com.heroku.sdk.heroku-gradle") version "2.0.0"
}

version = "1.1"

repositories {
    mavenCentral()
	maven("https://m2.chew.pro/snapshots/")
}

dependencies {
	implementation("net.dv8tion:JDA:5.0.0-alpha.4") {
		exclude(module = "opus-java")
	}
    implementation("pw.chew:jda-chewtils:2.0-SNAPSHOT")
    implementation("ch.qos.logback:logback-classic:1.2.10")
	implementation("org.codehaus.groovy:groovy:3.0.9")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")
	implementation("org.springframework.boot:spring-boot-starter:2.6.2")
	implementation("org.reflections:reflections:0.10.2")
	runtimeOnly("org.postgresql:postgresql:42.3.1")
	compileOnly("org.projectlombok:lombok:1.18.22")
	annotationProcessor("org.projectlombok:lombok:1.18.22")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.2")
}

heroku {
    appName = "modulebot"
    jdkVersion = "17"
    includes = listOf("build/libs/module.jar")
    isIncludeBuildDir = false
    processTypes = mapOf("worker" to "java -jar build/libs/module.jar")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	mainClass.set("org.module.Launcher")
	this.archiveFileName.set("module.jar")
}
tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}
tasks.processResources {
	filesMatching("application.yml") {
		expand(project.properties)
	}
}
