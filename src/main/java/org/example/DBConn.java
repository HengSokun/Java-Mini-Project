package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DBConn {

    private  static  Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        DBConn.connection = connection;
    }

    public Connection connDB(){
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            Properties properties = new Properties();
            properties.put("user","postgres");
            properties.put("password","1213@A*#");
            connection = DriverManager.getConnection(url,properties);
            Statement statement = connection.createStatement();
            String create = "CREATE TABLE Products(id serial4 primary key,name varchar(100) not null,unitPrice int,stockQty int,importDate date not null)";
            statement.executeUpdate(create);

        } catch (Exception e) {
  //          System.out.println(e.getMessage());
        }
        return connection;
    }
    public static void main(String[] args) {

    }

    public Statement createStatement() {
        return createStatement();
    }
}


