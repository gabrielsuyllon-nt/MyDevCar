package com.cvproy.model;

import com.cvproy.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Competencia {
    private int id;
    private String nombre;
    private boolean marcada; 
       
    // Getters y Setters
    public int getId() { return id; }
    public void setID(int id){this.id=id;}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre){this.nombre=nombre;}
    
    public boolean isMarcada() { return marcada; }
    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }
     
    /*VERIFICACIÓN DE REGISTRO POR TIPO*/
    public boolean haRegistradoTipo(int id_persona, String tipo) {
        String sql = "SELECT COUNT(*) FROM persona_competencia pc " +
                     "JOIN competencia c ON pc.id_competencia = c.id_competencia " +
                     "JOIN categoria_competencia cc ON c.id_categoria = cc.id_categoria " +
                     "WHERE pc.id_persona = ? AND cc.tipo = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id_persona);
            pstmt.setString(2, tipo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //VALIDACIÓN DE CATEGORÍAS REVISADAS
    public List<Categoria> getCategoriasParaUsuario(int id_persona, String tipo) {
        List<Categoria> categorias = new ArrayList<>();
        Set<Integer> categoriasCompletadas = new HashSet<>();
              
        try (Connection conn = ConexionBD.conectar()) {
            
            
            String sqlCompletadas = "SELECT DISTINCT c.id_categoria " +
                                    "FROM persona_competencia pc " +
                                    "JOIN competencia c ON pc.id_competencia = c.id_competencia " +
                                    "JOIN categoria_competencia cc ON c.id_categoria = cc.id_categoria " +
                                    "WHERE pc.id_persona = ? AND cc.tipo = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCompletadas)) {
                pstmt.setInt(1, id_persona);
                pstmt.setString(2, tipo);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        categoriasCompletadas.add(rs.getInt("id_categoria"));
                    }
                }
            }
            
           
            String sqlCategorias = "SELECT id_categoria, nombre FROM categoria_competencia WHERE tipo = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCategorias)) {
                
                pstmt.setString(1, tipo); 
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int idCat = rs.getInt("id_categoria");
                        String nombreCat = rs.getString("nombre");
                        
                      
                        Categoria cat = new Categoria(idCat, nombreCat);
                        
                       
                        if (categoriasCompletadas.contains(idCat)) {
                            cat.setCompletada(true); 
                        }
                        categorias.add(cat);
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }
    
    /*CARGA DE COMPETENCIAS SEGÚN CATEGORÍA*/
    public List<Competencia> getCompetenciasPorCategoria(int id_persona, int id_categoria) {
        List<Competencia> competencias = new ArrayList<>();
        Set<Integer> competenciasMarcadas = new HashSet<>();
        
        try (Connection conn = ConexionBD.conectar()) {
            
            
            String sqlMarcadas = "SELECT id_competencia FROM persona_competencia WHERE id_persona = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMarcadas)) {
                pstmt.setInt(1, id_persona);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        competenciasMarcadas.add(rs.getInt("id_competencia"));
                    }
                }
            }
            
           
            String sqlCompetencias = "SELECT c.id_competencia, c.nombre, n.nombre AS nombre_nivel " +
                                     "FROM competencia c " +
                                     "JOIN nivel n ON c.id_nivel = n.id_nivel " +
                                     "WHERE c.id_categoria = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCompetencias)) {
                pstmt.setInt(1, id_categoria);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int idComp = rs.getInt("id_competencia");
                        String nombreComp = rs.getString("nombre");
                                                
                        Competencia comp = new Competencia();
                        comp.setID(idComp);
                        comp.setNombre(nombreComp);
                                                
                        if (competenciasMarcadas.contains(idComp)) {
                            comp.setMarcada(true); 
                        }
                        competencias.add(comp);
                        
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return competencias;
    }
    
    public boolean guardarCompetenciasUsuario(int id_persona, int id_categoria, String[] idsCompetencias) {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false); 
            
            String sqlDelete = "DELETE pc FROM persona_competencia pc " +
                               "JOIN competencia c ON pc.id_competencia = c.id_competencia " +
                               "WHERE pc.id_persona = ? AND c.id_categoria = ?";
            try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                pstmtDelete.setInt(1, id_persona);
                pstmtDelete.setInt(2, id_categoria);
                pstmtDelete.executeUpdate();
            }
            
            
            if (idsCompetencias != null && idsCompetencias.length > 0) {
                String sqlInsert = "INSERT INTO persona_competencia (id_persona, id_competencia) VALUES (?, ?)";
                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                    
                    for (String idStr : idsCompetencias) {
                        pstmtInsert.setInt(1, id_persona);
                        pstmtInsert.setInt(2, Integer.parseInt(idStr));
                        pstmtInsert.addBatch(); 
                    }
                    pstmtInsert.executeBatch(); 
                }
            }
            
            conn.commit(); 
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); 
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Map<String, Integer> getResumenCompetencias(int idUsuario) {
        Map<String, Integer> resumen = new HashMap<>();
        //Agrupamos competencias por nivel
        String sql = "SELECT n.nombre, COUNT(pc.id_competencia) AS total " +
                     "FROM Persona_Competencia pc " +
                     "JOIN Competencia c ON pc.id_competencia = c.id_competencia " +
                     "JOIN Nivel n ON c.id_nivel = n.id_nivel " +
                     "WHERE pc.id_persona = ? " +
                     "GROUP BY n.nombre";

        try (Connection cn = new ConexionBD().conectar();
             PreparedStatement pt = cn.prepareStatement(sql)) {

            pt.setInt(1, idUsuario);
            try (ResultSet rs = pt.executeQuery()) {
                while (rs.next()) {
                    resumen.put(rs.getString("nombre"), rs.getInt("total"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resumen;
    }

}