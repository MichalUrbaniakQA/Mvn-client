# Mvn-client

Client for usage maven command and git pull from the branch.

Technologies: Java, Javafx, Scene Builder, Spring Boot.


How to maven:
- create a jar file 
- create "config.txt" in the same folder as .jar
- in the "config.txt" put: 

mavenPath-e.g.:MAVEN_HOME, 
gradlePath-e.g.:C:/Gradle/gradle, 
projectsPath-e.g.:C:/Users/michalurbaniak/IdeaProjects, 
email-e.g.:abc123@abc.abc (for e.g.: bitbucket), 
pass-e.g.:abc123 (for e.g.: bitbucket), 
branch-e.g.:develop (default branch), 

- open app
- enter path to project ("submit" button)
- select project
- select maven command
- run maven command ("maven" button")


How to git:
- enter the branch name (but you have name of branch from config.txt) or change it
- enter path to project ("submit" button)
- select project
- select local branch to pull from the remote branch 
- run pull using "git" button
- You can build many maven projects but You can using git for only one project
