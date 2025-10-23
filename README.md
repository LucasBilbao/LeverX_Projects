# LeverX_Projects

## Java Part 1 Homework

### Table of Contents

- [Compile and run the program in the console](#compile-and-run-the-program-in-the-console)
- [Build and execute JAR file](#build-and-execute-jar-file)

### Compile and run the program in the console

```shell
    cd java_part_1/
    javac -d out src/main/java/**/*.java
    java -cp out App
```

Explanation:

* `javac -d out src/main/java/**/*.java`
    * `javac` – Java compiler command
    * `-d out` – specifies the destination folder for compiled `.class` files (so they don’t clutter your source
      directory)
    * `src/main/java/**/*.java` – means “compile all `.java` files inside `src/main/java` and all its subdirectories”
* `java -cp out App`
    * `java` – the Java runtime (executes compiled bytecode)
    * `-cp out` – sets the classpath to out, telling Java where to find your compiled `.class` files.

### Build and execute JAR file

```shell
    cd java_part_1/
    javac -d out src/main/java/**/*.java
    jar cfe app.jar App -C out .
    java -jar app.jar
```

* `javac -d out src/main/java/**/*.java`
    * `javac` – the Java compiler
    * `-d out` – tells the compiler to put compiled files into the out folder (creating subfolders matching package
      names)
    * `src/main/java/**/*.java` — means "compile all .java files inside src/main/java and all subdirectories."
* `jar cfe app.jar App -C out .`
    * `jar` — the Java archiver tool (similar to a zip utility, but for Java programs)
    * `c` — create a new JAR file
    * `f` — file: the name of the JAR file to create (here: app.jar)
    * `e` — entry point: the class containing your `main()` method (here: App)
    * `-C out .` — means "change to the out directory and include everything inside it"
* `java -jar app.jar`
    * `java` — the Java runtime launcher
    * `-jar app.jar` — tells Java to run the program inside `app.jar`
