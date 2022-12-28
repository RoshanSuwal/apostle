## APOSTLE - API Management System
A scalable, multi-cloud API management platform for securing, publishing, and analyzing APIs.

### Description of Project
1. Built using __JAVA-17__.
2. __MAVEN__ built tools.
3. used __SPRING FRAMEWORK__.

### Steps to build project
1. import the project
2. execute __apostle-build.sh__ : creates a folder apostle containing dirs: __*bin*__, __*libs*__,__*plugins*__
3. execute the apostle.sh file present inside ***bin*** directory -
``` ./apostle/bin/apostle.sh```
4. Note : ***application.properties*** file and ***logback-spring.xml*** file should be present inside ***bin*** directory.

### POLICY
- Policy contains the sets of rules that is used to process the request before and after fetching the response.
- Policy that operates before fetching are called pre-policy and after fetching are called post-policy.
- Policy can be added to the system by annotating the class with ***@Policy*** and extending the class with ***Policy*** class.
- List of Policies pre-installed
  1. Ip Policy : policy for validation of ip
  2. Api Policy : policy for validating apis
  3. PublicKey Policy : policy for validating publicKey.
  4. Quota Policy : policy for validating the quota.
  5. Rate Limit Policy : policy for controlling the rate of request.
  6. PersonalPackage Policy : policy for validating the subscribed package.

### RESPONSE SERVICE
- define the rules for fetching the response.
- service can be added by extending the class with ***ResponseService*** class and annotating with ***@ResponseService***.
- List of pre-installed response services
  1. Backend Response Service : fetch the response from backend
  2. Mock Response Service : 
  3. Cache Response Service : returns the mock response

### POST SERVICES
- services that are executes at the end of every requests either on success or failure.
- these post services can be creating by extending the class with ***PostService*** class and annotating it with ***@PostService*** annotation.
- List of installed post services :
  1. Metrics Service : Logs the information to file
  2. Email Services : send emails 
  3. Notification Services : sends notification
  
### Notification Plugin
- plugin that is used to add notification platforms.
- List of installed notification plugin
  1. Mattermost : send notification to mattermost channel.
- Method to install notification
  1. Create a class extending ***Notifier*** class. This class contains logic for sending notification to specific platform.
  2. create a class extending ***NotificationFactory*** class which builds the specific ***Notifier***.
  3. annotate the ***NotificationFactory*** with ***@NotificationFactory*** annotation

### Steps to install plugin
- build ***jar*** file for the plugin.
- copy the ***jar*** file to ***bin/plugin*** directory.
