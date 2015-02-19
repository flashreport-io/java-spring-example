# java-spring-example
Spring application using flashreport.io api

This application illustrates how our API can be accessed from a Spring application in the simplest and most straightforward way.

## Requirements

### API token

FlashReport uses an API token to secure its endpoints. Before you can run these samples, you must obtain your token and
specify it in the code.

    public static final String API_TOKEN = "PASTE YOU API TOKEN HERE";

To obtain your API token

- Go to [FlashReport website] (http://flashreport.io)
- Register, it's free and does not require any payment information
- Log in, and get your API token from the _Control Panel_ screen
- Your free evaluation period lasts 30 days, and so does your token

### Required software

To run this example application, you need

- [Java 8 JDK] (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven tool] (http://maven.apache.org/index.html)

## Running the sample



After specifying your API token as described above, just execute _SampleApplication_ from your IDE.

Alternatively, you can build the JAR file and execute it from the command line.
From the directory where pom.xml is, run the following commands

    mvn clean package
    java -jar target\spring-example-1.0.0-SNAPSHOT.jar







