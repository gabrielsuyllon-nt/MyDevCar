package com.cvproy.model;

import com.cvproy.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Nivel {
    private int id;
    private String nombre;

    // --- Getters y Setters ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    
    private Set<Integer> getCompetenciasDePersona(int id_persona) throws SQLException {
        String sql = "SELECT id_competencia FROM persona_competencia WHERE id_persona = ?";
        Set<Integer> competencias = new HashSet<>();
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id_persona);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    competencias.add(rs.getInt("id_competencia"));
                }
            }
        }
        return competencias;
    }

    private Set<Integer> getCompetenciasRequeridas(int id_nivel) throws SQLException {
        String sql = "SELECT id_competencia FROM competencia WHERE id_nivel = ?";
        Set<Integer> competencias = new HashSet<>();
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id_nivel);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    competencias.add(rs.getInt("id_competencia"));
                }
            }
        }
        return competencias;
    }

    /*ALGORITMO DE COMPARACIÓN*/
    public boolean compararNivel(int id_persona, int id_nivel_a_comparar) {
        try {
            
            Set<Integer> compPersona = this.getCompetenciasDePersona(id_persona);

            // --- Lógica de Nivel 3 ---
            if (id_nivel_a_comparar == 3) {
                Set<Integer> reqNivel1 = this.getCompetenciasRequeridas(1);
                Set<Integer> reqNivel2 = this.getCompetenciasRequeridas(2);
                Set<Integer> reqNivel3 = this.getCompetenciasRequeridas(3);

                boolean tieneTodasN1 = compPersona.containsAll(reqNivel1);
                boolean tieneTodasN2 = compPersona.containsAll(reqNivel2);
                
                // Lógica para "al menos una" (intersección no vacía)
                boolean tieneAlgunaN3 = false;
                for (int compN3 : reqNivel3) {
                    if (compPersona.contains(compN3)) {
                        tieneAlgunaN3 = true;
                        break; 
                    }
                }
                
                return tieneTodasN1 && tieneTodasN2 && tieneAlgunaN3;
            }
            
            // --- Lógica de Nivel 2 ---
            else if (id_nivel_a_comparar == 2) {
                Set<Integer> reqNivel1 = this.getCompetenciasRequeridas(1);
                Set<Integer> reqNivel2 = this.getCompetenciasRequeridas(2);
                
                boolean tieneTodasN1 = compPersona.containsAll(reqNivel1);
                
                boolean tieneAlgunaN2 = false;
                for (int compN2 : reqNivel2) {
                    if (compPersona.contains(compN2)) {
                        tieneAlgunaN2 = true;
                        break;
                    }
                }
                
                return tieneTodasN1 && tieneAlgunaN2;
            }
            
            // --- Lógica de Nivel 1 ---
            else if (id_nivel_a_comparar == 1) {
                Set<Integer> reqNivel1 = this.getCompetenciasRequeridas(1);

                boolean tieneAlgunaN1 = false;
                for (int compN1 : reqNivel1) {
                    if (compPersona.contains(compN1)) {
                        tieneAlgunaN1 = true;
                        break;
                    }
                }
                return tieneAlgunaN1;
            }
            
            
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
    
    public String getNivelPersona(int id_persona) {
        String sql = "SELECT n.nombre FROM nivel n " +
                     "JOIN persona_nivel pn ON n.id = pn.id_nivel " +
                     "WHERE pn.id_persona = ?";
        String nombreNivel = null;

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id_persona);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    nombreNivel = rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreNivel;
    }

    public boolean guardarNivelPersona(int id_persona) {
        
 
        int id_N;
        if (this.compararNivel(id_persona, 3)) { 
            id_N = 3;
        } else if (this.compararNivel(id_persona, 2)) { 
            id_N = 2;
        } else if (this.compararNivel(id_persona, 1)) { 
            id_N = 1;
        } else {
            id_N = 1; 
        }
        
        String sqlDelete = "DELETE FROM persona_nivel WHERE id_persona = ?";
        String sqlInsert = "INSERT INTO persona_nivel(id_persona, id_nivel) VALUES (?, ?)";
        
        
        try (Connection conn = ConexionBD.conectar()) {
            
            try {
                conn.setAutoCommit(false); 

                
                try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                    pstmtDelete.setInt(1, id_persona);
                    pstmtDelete.executeUpdate();
                }
                
                
                int filasAfectadas;
                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                    pstmtInsert.setInt(1, id_persona);
                    pstmtInsert.setInt(2, id_N); // Inserta el nivel calculado
                    filasAfectadas = pstmtInsert.executeUpdate();
                }
                
                
                conn.commit(); 
                return filasAfectadas > 0;
            
            } catch (SQLException e) {
                System.err.println("Error en la transacción, haciendo rollback.");
                e.printStackTrace();
                conn.rollback(); 
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}