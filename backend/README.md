# SaveX Backend Docs 🚀

## MySQL Database Setup 🛠️

1. **Install MySQL on your system.** 🖥️
   - You can download MySQL from the official website: [MySQL Downloads](https://dev.mysql.com/downloads/)
   - You can also use a package manager to install MySQL. (In windows you can use `winget install Oracle.MySQL`)
2. **Open MySQL Shell and connect to your MySQL server:** 🔌
   - ⚠️ Don't forget to add the **backslash** and the **`;`** in the command.
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
spring.datasource.url=jdbc:mysql://localhost:3306/savex
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