# SaveX Backend Docs 🚀

## MySQL Database Setup 🛠️

1. **Install MySQL on your system.** 🖥️
   - Download and install MySQL from the official website: [MySQL Downloads](https://dev.mysql.com/downloads/installer/)
   - Install MySQL Server and MySQL Shell
   - Next on every step until you reach the password configuration
   - Passwords
     - Create a user of the DB with username `root` and password `Str0ngP@ssw0rd!` for database password
2. **Open MySQL Shell and connect to your MySQL server:** 🔌
   ```sh
   \connect root@localhost
   ```
   If prompted, enter your MySQL password. 🔑

3. **Switch to SQL mode:** 🔄
   ```sh
   \sql
   ```

4. **Create the database:** 🗄️
   ```sql
   CREATE DATABASE savexdb;
   ```

5. **Select the database:** ✅
   ```sql
   USE savexdb;
   ```

## Spring Boot Configuration ⚙️

Ensure your `application.properties` file is configured correctly to connect to the MySQL database. Here is an example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/savexdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Replace `your_username` and `your_password` with your MySQL credentials.

## Running the Application ▶️

To run the application, ensure MySQL is running and then execute the following command in your project directory:

```sh
mvn spring-boot:run
```

This will start your Spring Boot application and connect to the MySQL database.