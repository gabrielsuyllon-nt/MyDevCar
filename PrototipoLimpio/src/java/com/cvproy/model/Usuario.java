package com.cvproy.model;

import com.cvproy.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class Usuario {
    
    private int id;
    private String nombre;
    private String apellido;
    private String email;
        
    //Getters y Setters
    public int getId() { return id; }
    public void setId(int id){this.id=id;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre){this.nombre=nombre;}
    public String getApellido() { return apellido; }
    public void setApellido(String apellido){this.apellido=apellido;}
    public String getEmail() { return email; }
    public void setEmail(String email){this.email=email;}

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
    
    public String getIniciales() {
        if (nombre != null && !nombre.isEmpty() && apellido != null && !apellido.isEmpty()) {
            return String.valueOf(nombre.charAt(0)) + String.valueOf(apellido.charAt(0));
        }
        return "N/A";
    }
    
    /*REGISTRO EN LA BASE DE DATOS */
    public boolean registrarUsuario(String nombre, String apellido, String email, String password) {
        
        //Transformación del parámetro en hash
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        
        
        String sql = "INSERT INTO persona (nombre, apellido, email, password) VALUES (?, ?, ?, ?)";
        
    
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, email);
            pstmt.setString(4, passwordHash); 

            
            int filasAfectadas = pstmt.executeUpdate();
            
            
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("ERROR AL REGISTRAR USUARIO: " + e.getMessage());
            
            e.printStackTrace();
            return false;
        }
    }
    
    /*VALIDACIÓN CONTRASEÑA X EMAIL*/
    public boolean validarLogin(String email, String passwordIngresada) {
        
    
        String sql = "SELECT password FROM persona WHERE email = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            
            
            try (ResultSet rs = pstmt.executeQuery()) {
                
                
                if (rs.next()) {
                    
                    String passwordHashAlmacenada = rs.getString("password");//
                    
                   //Algoritmo de comparación de hash con texto plano
                    return BCrypt.checkpw(passwordIngresada, passwordHashAlmacenada);
                    
                } else {
                    
                    return false; 
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /*OBTENCIÓN DE LOS DATOS DEL USUARIO SEGÚN EMAIL*/
    public Usuario getUsuarioPorEmail(String email) {
        
        String sql = "SELECT id_persona, nombre, apellido, email FROM persona WHERE email = ?";
        
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                   
                    int id = rs.getInt("id_persona");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    Usuario usuario=new Usuario();
                    usuario.setId(id);
                    usuario.setNombre(nombre);
                    usuario.setApellido(apellido);
                    usuario.setEmail(email);
                                                
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; 
    }
    
    public boolean verificarPassword(int idUsuario, String passwordActual) {
        String sql = "SELECT password FROM Persona WHERE id_persona = ?";
        try (Connection cn = new ConexionBD().conectar();
             PreparedStatement pt = cn.prepareStatement(sql)) {
            pt.setInt(1, idUsuario);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                String hashGuardado = rs.getString("password");
                return BCrypt.checkpw(passwordActual, hashGuardado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarCorreo(int idUsuario, String nuevoCorreo) {
        String sql = "UPDATE Persona SET email = ? WHERE id_persona = ?";
        try (Connection cn = new ConexionBD().conectar();
             PreparedStatement pt = cn.prepareStatement(sql)) {
            pt.setString(1, nuevoCorreo);
            pt.setInt(2, idUsuario);
            return pt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
        String sql = "UPDATE Persona SET password = ? WHERE id_persona = ?";
        try (Connection cn = new ConexionBD().conectar();
             PreparedStatement pt = cn.prepareStatement(sql)) {
            String nuevoHash = BCrypt.hashpw(nuevaPassword, BCrypt.gensalt());
            pt.setString(1, nuevoHash);
            pt.setInt(2, idUsuario);
            return pt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }   
    
    public boolean guardarEvaluacionRol(int idUsuario, int idRol, String[] idsHabilidades) {
        
        String sqlRol = "INSERT INTO persona_rol (id_persona, id_rol) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE fecha_reg = CURRENT_TIMESTAMP";
        String deleteSqlHab = "DELETE FROM persona_habilidad WHERE id_persona = ?";
        String insertSqlHab = "INSERT INTO persona_habilidad (id_persona, id_habilidad) VALUES (?, ?)";

        Connection conn = null; 
        
        try {
            conn = ConexionBD.conectar();
            
            conn.setAutoCommit(false);

            try (PreparedStatement psRol = conn.prepareStatement(sqlRol)) {
                psRol.setInt(1, idUsuario);
                psRol.setInt(2, idRol);
                psRol.executeUpdate();
            }

            try (PreparedStatement psDelete = conn.prepareStatement(deleteSqlHab)) {
                psDelete.setInt(1, idUsuario);
                psDelete.executeUpdate();
            }

            if (idsHabilidades != null && idsHabilidades.length > 0) {
                try (PreparedStatement psInsert = conn.prepareStatement(insertSqlHab)) {
                    for (String idHabStr : idsHabilidades) {
                        if (idHabStr != null && !idHabStr.isEmpty()) {
                            psInsert.setInt(1, idUsuario);
                            psInsert.setInt(2, Integer.parseInt(idHabStr));
                            psInsert.addBatch();
                        }
                    }
                    psInsert.executeBatch();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    System.err.println("¡Error! Haciendo rollback de la transacción.");
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
            
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean eliminarRol(int idUsuario, int idRol) {
        String sql = "DELETE FROM persona_rol WHERE id_persona = ? AND id_rol = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idRol);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}