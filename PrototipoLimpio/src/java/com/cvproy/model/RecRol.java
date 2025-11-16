package com.cvproy.model;

import com.cvproy.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RecRol {
    public List<Map<String, String>> getRecomendacionesPorHabilidades(List<Integer> idsHabilidades) {
        
        List<Map<String, String>> recomendaciones = new ArrayList<>();
        
        if (idsHabilidades == null || idsHabilidades.isEmpty()) {
            return recomendaciones;
        }

        
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, h.nombre AS habilidadNombre " +
            "FROM recomendacion_habilidad r " +
            "JOIN habilidad h ON r.id_habilidad = h.id_habilidad " +
            "WHERE r.id_habilidad IN (");
        
        for (int i = 0; i < idsHabilidades.size(); i++) {
            sql.append("?");
            if (i < idsHabilidades.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            int index = 1;
            for (Integer id : idsHabilidades) {
                ps.setInt(index++, id);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    
                    Map<String, String> rec = new HashMap<>();
                    rec.put("habilidadNombre", rs.getString("habilidadNombre"));
                    rec.put("concepto_clave", rs.getString("concepto_clave"));
                    rec.put("curso_nombre", rs.getString("curso_nombre"));
                    rec.put("curso_enlace", rs.getString("curso_enlace"));
                    rec.put("doc_nombre", rs.getString("doc_nombre"));
                    rec.put("doc_enlace", rs.getString("doc_enlace"));
                    rec.put("sugerencia_proy", rs.getString("sugerencia_proy"));
                    rec.put("curso_desc", rs.getString("curso_desc"));
                    recomendaciones.add(rec);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return recomendaciones;
    }
}
