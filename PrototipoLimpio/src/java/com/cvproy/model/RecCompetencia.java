package com.cvproy.model;

import com.cvproy.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecCompetencia {
    private int idRecCompetencia;
    private int idCompetencia;
    private String nombreCompetencia;
    private String tipoCompetencia;
    private String conceptoClave;
    private String libroNombre;
    private String libroEnlace;
    private String libroDescripcion;
    private String recursoDigitalNombre;
    private String recursoDigitalEnlace;
    private String recursoDigitalDescripcion;

    public RecCompetencia() {
    }
    
    public int getIdRecCompetencia() {return idRecCompetencia;}
    public void setIdRecCompetencia(int idRecCompetencia) {this.idRecCompetencia = idRecCompetencia;}

    public int getIdCompetencia() {return idCompetencia;}
    public void setIdCompetencia(int idCompetencia) {this.idCompetencia = idCompetencia;}

    public String getNombreCompetencia() {return nombreCompetencia; }
    public void setNombreCompetencia(String nombreCompetencia) {
        this.nombreCompetencia = nombreCompetencia;
    }

    public String getConceptoClave() {
        return conceptoClave;
    }

    public void setConceptoClave(String conceptoClave) {
        this.conceptoClave = conceptoClave;
    }

    public String getLibroNombre() {
        return libroNombre;
    }

    public void setLibroNombre(String libroNombre) {
        this.libroNombre = libroNombre;
    }

    public String getLibroEnlace() {
        return libroEnlace;
    }

    public void setLibroEnlace(String libroEnlace) {
        this.libroEnlace = libroEnlace;
    }

    public String getRecursoDigitalNombre() {
        return recursoDigitalNombre;
    }

    public void setRecursoDigitalNombre(String recursoDigitalNombre) {
        this.recursoDigitalNombre = recursoDigitalNombre;
    }

    public String getRecursoDigitalEnlace() {
        return recursoDigitalEnlace;
    }

    public void setRecursoDigitalEnlace(String recursoDigitalEnlace) {
        this.recursoDigitalEnlace = recursoDigitalEnlace;
    }
    
    public String getTipoCompetencia() { return tipoCompetencia; }
    public void setTipoCompetencia(String tipoCompetencia) { 
        this.tipoCompetencia = tipoCompetencia; }

    public String getLibroDescripcion() { return libroDescripcion; }
    public void setLibroDescripcion(String libroDescripcion) { 
        this.libroDescripcion = libroDescripcion; }

    public String getRecursoDigitalDescripcion() { return recursoDigitalDescripcion; }
    public void setRecursoDigitalDescripcion(String recursoDigitalDescripcion) { 
        this.recursoDigitalDescripcion = recursoDigitalDescripcion; }
    
    public List<RecCompetencia> obtenerRecomendacionesPorUsuario(int idUsuario) {
        List<RecCompetencia> lista = new ArrayList<>();
               
        String sql = "SELECT rc.*, c.nombre AS nombre_competencia, cc.tipo AS tipo_competencia " +
                    "FROM Recomendacion_Competencia rc " +
                    "JOIN Competencia c ON rc.id_competencia = c.id_competencia " +
                    "JOIN Categoria_Competencia cc ON c.id_categoria = cc.id_categoria " +
                    "WHERE c.id_competencia NOT IN (" +
                    "    SELECT id_competencia FROM Persona_Competencia WHERE id_persona = ?" +
                    ") " +
                    "ORDER BY cc.tipo, c.nombre";

        try (Connection cn = new ConexionBD().conectar();
             PreparedStatement pt = cn.prepareStatement(sql)) {

            pt.setInt(1, idUsuario);
            ResultSet rs = pt.executeQuery();

            while (rs.next()) {
                RecCompetencia rec = new RecCompetencia();
                rec.setIdRecCompetencia(rs.getInt("id_rec_comp"));
                rec.setIdCompetencia(rs.getInt("id_competencia"));
                rec.setNombreCompetencia(rs.getString("nombre_competencia"));
                rec.setConceptoClave(rs.getString("concepto_clave"));
                rec.setLibroNombre(rs.getString("libro_nombre"));
                rec.setLibroEnlace(rs.getString("libro_enlace"));
                rec.setRecursoDigitalNombre(rs.getString("rec_dig_nombre"));
                rec.setRecursoDigitalEnlace(rs.getString("rec_dig_enlace"));
                rec.setTipoCompetencia(rs.getString("tipo_competencia"));
                rec.setLibroDescripcion(rs.getString("libro_desc")); 
                rec.setRecursoDigitalDescripcion(rs.getString("rd_desc"));
                
                lista.add(rec);
            }

        } catch (Exception e) {
            System.err.println("Error en obtenerRecomendacionesPorUsuario: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    
}