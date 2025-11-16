package com.cvproy.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {

    private static final String HOST = "gateway01.us-east-1.prod.aws.tidbcloud.com";
    private static final String PORT = "4000";
    private static final String DB_NAME = "BDCOMP"; 
    private static final String USER = "BnJB2XT7peMaaYr.root"; 
    private static final String PASSWORD = "1W6dZQPcS95bxiyG";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME 
            + "?sslMode=VERIFY_IDENTITY&useSSL=true&allowPublicKeyRetrieval=true";

    public static Connection conectar() { 
        Connection cn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("CONECTADO A LA NUBE (TiDB)");
        } catch (Exception e) {
            System.err.println("ERROR DE CONEXIÓN: " + e.getMessage());
            e.printStackTrace();
        }
        return cn;
    }

    public static void main(String[] args) {
        conectar();
    }
}