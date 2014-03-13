GraffitiTracker
===============

Status
------
Basic infrastructure in place.  Login, username, password recovery feature complete.

Summary
-------
Tracks crowd sourced graffiti through metadata characteristics, by date and location.

Feature/Infrastructure Overview
----------------
-Implemented using Spring 4.0  
-Spring MVC for servlet/JSP  
-Spring Security for login, custom role management, page access based per user role, and HTTP/HTTPS protocol trafficking  
-MySQL for data persitence  
-Spring OR/M for object to data mapping  
-Spring IoC/DI with autowiring for service implementations, data source, and application configurations  
-SHA-256 hashing of user passwords for data security (clear text passwords are never persisted)   
-Apache Tiles for interface templating layout  
-Apache Velocity for email templating layout  
-Maven for build management  
-SSL used for all logins to protect password transfer  
-User registration timestamp, user last login timestamp, and login count persisted for statistics display upon login  
-Programmatic IP address banning by single IP address or IP address range  
-Text based logic captcha used for user registration through a 3rd party web service (textCAPTCHA)  
-Banned phrase prevention in username through a 3rd party web service (wdyl.com)  
-Registration confirmation by emailed unique, expiring URL link for security  
-Lost username recovery by email, with silent failures for security  
-Password reset through answering user defined security answer and emailed unique, expiring URL link for security, with silent failures for security  
-Daily email sent to super admin users containing basic site usage statistics  
-Application deployed with Amazon's AWS Relational Database Services and Amazon's AWS Elastic Beanstalk (auto-scaling web server)  
-User inputs validated and errors displayed  

To Build With Maven
-------------------
mvn package
