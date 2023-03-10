# ReviewBoardSvnIntegrator
This service watches the new commits on several SVN repositories and manage reviews or new revisions on ReviewBoard. The commits must contain the Jira ID (you can make a pull request to manage other IDs than jira's).

## Prerequisites
- A Reviewboard server (using Docker, find the docker-compose.yml file here [Docker-Compose](https://github.com/reviewboard/reviewboard/tree/master/contrib/docker/examples/))  (configure the property file for connection details)
- A SVN Server (configure the property file for connection details) 
- Jdk 17
- Maven
- RbTools installed and bin folder referenced in PATH environment variable. Download link: https://www.reviewboard.org/downloads/rbtools/
- Svn installed and bin folder referenced in PATH environment variable. Download link: https://sliksvn.com/download/
- Windows support (Linux should work after fixing one or two details in code, like the command name of rbtools -> "rbt" instead of "rbt.cmd". You can make a PR if you want to add the OS-adapt feature)
- Create a credentials.properties file and add necessary properties (or you will get errors at first runtime). This file is git ignored.
  ![Screenshot: Run Config](/doc/Credentials_property-file.JPG)

## How to run
You can run using maven with the spring-boot plugin, such as: mvn spring-boot:run -Dspring.profiles.active=dev

Or with Intellij you can just start the program normally:
![Screenshot: Run Config](/doc/Intellij_Run-Debug_Configuration.JPG)

## TODOs
- Implement all the NotImplementedException throws
- Add all the missing / remaining projects in ApplicationsConfig.java (ex: CP-API, CS, CS-API, ...)
- Implement new features:
  - Add auto-management RB groups -> 1 group per new created branch. To remove when branch doesn't exist anymore on server. Manage future user add to repository lately.
  - Add more commit data in description of review
  - Manage review per Application
  - Auto-update new repo add in config
  - Build a new release interceptor that will notif Teams of all the new released versions of the day

