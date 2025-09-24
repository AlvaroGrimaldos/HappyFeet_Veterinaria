package com.happyfeet.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//La conexion como un Singleton
public class ConexionDB {
    private static String host = "jdbc:mysql://localhost:3306/happyfeet_database";
    private static String user = "root";
    private static String password = "Alvaro2007@";

    private static Connection connection;
    private static ConexionDB instancia;

    private ConexionDB() {
        this.instancia = null;
        this.connection = null;
    }

    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
            try {
                if(connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(host, user, password);
                    System.out.println("Conexion con exito!");
                }
            }catch(SQLException e){
                connection = null;
                System.out.println("Error al conectar con la base de datos!\n" + e.getMessage());
            }
        }
        return instancia;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try{
            if(connection != null || !connection.isClosed()) {
                connection.close();
                System.out.println("Conexion cerrada");
            }
        }catch(SQLException e){
            System.out.println("Error al cerrar conexion con la base de datos!\n" + e.getMessage());
        }
    }
}
