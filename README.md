# GottaEat
Code for the fictitious food delivery company GottaEat used in the Pulsar In Action book

## Build Instructions

This project leverages the [Continuous delivery friendly version of Maven](https://maven.apache.org/maven-ci-friendly.html), which allows us to dynamically specify the project version
via a property, e.g.

`mvn -Drevision=1.0.0 clean install`


When Maven install (or deploy) ours artifacts, pom.xml are copied without modification (${revision} properties are not replaced by their resolved value). If you try to depend on one of these artifacts it will fail because ${revision} will not be set in this outside project. In order to fix this issue, you will need to download and install the [unique-revision-maven-filtering](https://jeanchristophegay.com/en/posts/maven-unique-version-multi-modules-build/) Maven extension, and place it in your ${MAVEN_HOME}/lib folder
