package com.cvproy.model;

import com.cvproy.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Habilidad {

    private int id_habilidad;
    private String nombre;
    private int id_categoria;


    public int getId_habilidad() {
        return id_habilidad;
    }

    public void setId_habilidad(int id_habilidad) {
        this.id_habilidad = id_habilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }


    public Set<Integer> getHabilidadesPorUsuario(int idUsuario) {
        Set<Integer> habilidades = new HashSet<>();
        String sql = "SELECT id_habilidad FROM persona_habilidad WHERE id_persona = ?";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habilidades.add(rs.getInt("id_habilidad"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return habilidades;
    }


    public Map<Integer, String> getHabilidadesMapPorUsuario(int idUsuario) {
        Map<Integer, String> habilidadesMap = new HashMap<>();
        String sql = "SELECT h.id_habilidad, h.nombre FROM habilidad h " +
                     "JOIN persona_habilidad ph ON h.id_habilidad = ph.id_habilidad " +
                     "WHERE ph.id_persona = ?";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habilidadesMap.put(rs.getInt("id_habilidad"), rs.getString("nombre"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return habilidadesMap;
    }

    public Map<String, List<Habilidad>> getHabilidadesAgrupadas() {
        Map<String, List<Habilidad>> mapa = new HashMap<>();
        
        String sql = "SELECT c.nombre AS categoria_nombre, h.id_habilidad, h.nombre AS habilidad_nombre, h.id_cat_hab " +
                     "FROM habilidad h " +
                     "JOIN categoria_habilidad c ON h.id_cat_hab = c.id_cat_hab " +
                     "ORDER BY c.nombre, h.nombre";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoriaNombre = rs.getString("categoria_nombre");
                
                Habilidad hab = new Habilidad();
                hab.setId_habilidad(rs.getInt("id_habilidad"));
                hab.setNombre(rs.getString("habilidad_nombre"));
                hab.setId_categoria(rs.getInt("id_cat_hab"));
                
                if (!mapa.containsKey(categoriaNombre)) {
                    mapa.put(categoriaNombre, new ArrayList<>());
                }
                
                mapa.get(categoriaNombre).add(hab);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return mapa; 
    }
}