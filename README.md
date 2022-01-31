# Module
![](https://img.shields.io/github/workflow/status/HeadcrabJ/Module/CodeQL)
![](https://img.shields.io/github/repo-size/HeadcrabJ/Module)
![](https://img.shields.io/maven-central/v/net.dv8tion/JDA?label=JDA)

Multifunctional bot with moderation, information and utilities commands. [Invite](https://discord.com/api/oauth2/authorize?client_id=920294906008834058&permissions=8&scope=bot%20applications.commands), [Server](https://discord.gg/DWEjaavW6d/).

## Technologies
- [Java](https://www.oracle.com/java/)
- [JDA](https://github.com/DV8FromTheWorld/JDA)
- [JDA-Chewtils](https://github.com/Chew/JDA-Chewtils)
- [Hibernate](https://hibernate.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Lombok](https://projectlombok.org/)

## Build and run
1. Clone this repository.
2. In directory `src/main/resources` rename `application.example.properties` and `hibernate.example.properties` to `application.properties` and `hibernate.properties` and write required data to these files.
3. Create tables with provided schemas in directory `src/main/resources/schemas`.
4. Install JDK 17.
5. Run `./gradlew build` to build executable jar file.
6. And run `java -jar /build/libs/module.jar` to start the bot.

## License
Module is licensed under the [GPL v3](LICENSE)

## Special Thanks
[u032](https://github.com/u032), [Kasefuchs](https://github.com/Kasefuchs) and [JetBrains](https://jb.gg/OpenSourceSupport)!
