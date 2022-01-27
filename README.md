# Module
![](https://img.shields.io/github/workflow/status/HeadcrabJ/Module/CodeQL)
![](https://img.shields.io/github/repo-size/HeadcrabJ/Module)
![](https://img.shields.io/maven-central/v/net.dv8tion/JDA?label=JDA)

Multifunctional bot with moderation, information and utilities commands. [Server](https://discord.gg/DWEjaavW6d/).

## Technologies
- [Java](https://www.oracle.com/java/)
- [Spring](https://spring.io/)
- [JDA](https://github.com/DV8FromTheWorld/JDA)
- [JDA-Chewtils](https://github.com/Chew/JDA-Chewtils)
- [PostgreSQL](https://www.postgresql.org/)
- [Lombok](https://projectlombok.org/)

## Build and run
1. Clone this repository.
2. In directory `src/main/resources` rename `application.template.yml` to `application.yml` and write required data to this file.
3. Create tables with provided schemas in directory `src/main/resources/schemas`.
4. Install JDK 17.
5. Run `./gradlew build` to build executable jar file.
6. And run `java -jar /build/libs/module.jar` to start the bot.

## License
Module is licensed under the [GPL v3](LICENSE)

## Special Thanks
[u032](https://github.com/u032), [Kasefuchs](https://github.com/Kasefuchs) and [JetBrains](https://jb.gg/OpenSourceSupport)!
