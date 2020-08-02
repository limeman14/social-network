### Social Network

This is a completed group project from Skillbox online courses. The project includes 
backend and frontend for a typical social network. The main objective was to implement its backend (nevertheless,
frontend was modified and adapted for it), so the resulting web application supports:
* User management: sign up, sign-in, password recovery, change e-mail/password, delete account.
* Profile management: adding information to profile, upload avatar
* Blog/posts management: create, read, update delete posts with tags and search through them, add likes and comments to posts
* Social contacts and messaging: friends functionality + person-to-person dialogue, also blocking annoying users :)
* Searching for posts and people

**Technological stack:** _backend_ - Spring Boot, Security (JWT), Data, Mail, AOP, Admin+Actuator; MySQL, Liquibase;
 _frontend_ - Vue.js + pug.

**Launching and usage:** the app runs at 8080 port by default, use /login to get access to login page or /register to
register a new user. Spring Admin is available at 8082 port, for login and password see application.yml in
social-network-admin module.