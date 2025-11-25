package com.cvproy.model;

import com.cvproy.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Rol {

    private int id_rol;
    private String nombre;

    // Getters y Setters
    public int getId() {
        return id_rol;
    }

    public void setId(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Rol> getAllRoles() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT id_rol, nombre FROM rol ORDER BY nombre ASC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("nombre"));
                roles.add(rol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }
    

    public List<Rol> getRolesEvaluadosPorUsuario(int idUsuario) {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT r.id_rol, r.nombre FROM rol r " +
                     "JOIN persona_rol pr ON r.id_rol = pr.id_rol " +
                     "WHERE pr.id_persona = ?";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setId(rs.getInt("id_rol")); 
                    rol.setNombre(rs.getString("nombre")); 
                    roles.add(rol);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }


    public Set<Integer> getHabilidadesRequeridasPorRol(int idRol) {
        Set<Integer> habilidades = new HashSet<>();
        String sql = "SELECT id_habilidad FROM rol_habilidad WHERE id_rol = ?";
        
        try (Connection con = ConexionBD.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idRol);
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
    
    public Map<Integer, Set<Integer>> obtenerTodosLosRequisitos() {
        Map<Integer, Set<Integer>> requisitos = new HashMap<>();
        String sql = "SELECT id_rol, id_habilidad FROM Rol_Habilidad";
        
        try (Connection con = ConexionBD.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idRol = rs.getInt("id_rol");
                    int idHabilidad = rs.getInt("id_habilidad");

                    if (!requisitos.containsKey(idRol)) {
                        requisitos.put(idRol, new HashSet<>());
                    }
                    requisitos.get(idRol).add(idHabilidad);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requisitos;
    }    

    public int getIdPorNombre(String nombre) {
        String sql = "SELECT id_rol FROM rol WHERE nombre = ?";
        int idRol = 0; 
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idRol = rs.getInt("id_rol");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idRol;
    }


    public Map<Integer, String> getHabilidadesMapPorRol(int idRol) {
        Map<Integer, String> habilidadesMap = new HashMap<>();
        String sql = "SELECT h.id_habilidad, h.nombre FROM habilidad h " +
                     "JOIN rol_habilidad rh ON h.id_habilidad = rh.id_habilidad " +
                     "WHERE rh.id_rol = ?";
        
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idRol);
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
}