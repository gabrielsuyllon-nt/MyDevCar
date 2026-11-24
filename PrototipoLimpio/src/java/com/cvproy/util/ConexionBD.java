package com.cvproy.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Conexión a la base de datos MySQL (TiDB Cloud).
 *
 * Las credenciales se leen de variables de entorno para no guardarlas en el código:
 *   DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
 * En Render se configuran en la sección "Environment" del servicio.
 * En local se pueden definir en el sistema o en la configuración de Tomcat/NetBeans.
 */
public class ConexionBD {

    private static final String HOST = env("DB_HOST", "gateway01.us-east-1.prod.aws.tidbcloud.com");
    private static final String PORT = env("DB_PORT", "4000");
    private static final String DB_NAME = env("DB_NAME", "BDCOMP");
    private static final String USER = env("DB_USER", null);
    private static final String PASSWORD = env("DB_PASSWORD", null);

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?sslMode=VERIFY_IDENTITY&useSSL=true&allowPublicKeyRetrieval=true";

    private static String env(String nombre, String porDefecto) {
        String valor = System.getenv(nombre);
        return (valor == null || valor.isBlank()) ? porDefecto : valor;
    }

    public static Connection conectar() {
        Connection cn = null;
        if (USER == null || PASSWORD == null) {
            System.err.println("ERROR DE CONEXIÓN: faltan las variables de entorno DB_USER y/o DB_PASSWORD");
            return null;
        }
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
