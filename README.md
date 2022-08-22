# BANKING PROJECT

## This project is the back-end and front-end of the banking system we make as a final bootcamp project.

> - This project is conceived as a multiple banking management system.
> - For login, the user can log in to the system by creating a username and password.
> - At the same time, users can register in the system with the necessary information.
> - There are 4 different authorizations in the system and only authorized users can perform some operations.
> - Users with "ACTIVATE_DEACTIVATE_USER" authorization can enable registered users to login.
> - Users with "CREATE_BANK" authorization can create banks that can be used in the system.
> - Users with "CREATE_ACCOUNT" authorization can create a new account with the required information.
> - With the Account Detail service, users can see the information of their own accounts.
> - Users with "REMOVE_ACCOUNT" authorization can delete accounts.
> - Users can deposit funds into their own accounts with the deposit service.
> - With the transfer service, users can even transfer money from their own accounts to accounts in different banks or different types.

## Structure
> - The backend of this project was developed in Java and the frontend was developed in Angular JS. 
Data operations were performed using the MyBatis.
MSSQL was used for database in the project. 
This system includes authentication and authorization. 
Users can only perform transactions for which they are authorized. 
Implemented with JWT spring security, logging with Apache Kafka and log4j. 


## TECHNOLOGIES
- Java
- Spring Boot
- MyBatis
- Kafka
- Spring Security
- MySQL
- Log4J
- Collect API
- Angular JS (Frontend)

## Below are the application pictures.

-Auth

![login picture](images_for_readme/login.png)

-Register

![register picture](images_for_readme/login.png)

-Transfer

![transfer picture](images_for_readme/transfer.png)

-Accounts

![accounts picture](images_for_readme/get-accounts.png)
