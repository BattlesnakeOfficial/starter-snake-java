# starter-snake-java

Requirements
---

- Install JDK 8 [http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html]()
- Install Maven [https://maven.apache.org/install.html]()

Running the snake
---

```bash
mvn exec:exec
```

Snake will start up on port 8080


Run the tests
---

```bash
mvn test
```


Executable Jar
---

```bash
mvn package
```

Will result in a jar file in `target` called `starter-snake-java.jar`

You can then run this file with the command

```bash
java -jar target/starter-snake-java.jar
```