package com.cvproy.controller;

import com.cvproy.model.Competencia;
import com.cvproy.model.Habilidad;     
import com.cvproy.model.Rol;           
import com.cvproy.model.RolResultado; 
import com.cvproy.model.Usuario;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;    
import java.util.Map;
import java.util.Set;     

@WebServlet(name = "ResultadosServlet", urlPatterns = {"/ResultadosServlet"})
public class ResultadosServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión no válida");
            return;
        }

        try {
            int idUsuario = usuario.getId();
            
            Competencia compModel = new Competencia();
            Map<String, Integer> resumenCompetencias = compModel.getResumenCompetencias(idUsuario);

            Rol rolModel = new Rol();
            Habilidad habModel = new Habilidad();
            
            List<RolResultado> historialRoles = new ArrayList<>();
            
            List<Rol> rolesEvaluados = rolModel.getRolesEvaluadosPorUsuario(idUsuario);
            Set<Integer> habilidadesUsuario = habModel.getHabilidadesPorUsuario(idUsuario);

            for (Rol rol : rolesEvaluados) {
                Set<Integer> habilidadesRequeridas = rolModel.getHabilidadesRequeridasPorRol(rol.getId());
                
                if (habilidadesRequeridas.isEmpty()) {
                    historialRoles.add(new RolResultado(rol.getNombre(), 100, 0, 0, habilidadesUsuario.size()));
                    continue;
                }

                int totalRequeridas = habilidadesRequeridas.size();
                int fortalezas = 0;
                int oportunidades = 0;

                for (int idHabUsuario : habilidadesUsuario) {
                    if (habilidadesRequeridas.contains(idHabUsuario)) {
                        fortalezas++; 
                    } else {
                        oportunidades++; 
                    }
                }
                
                int brechas = totalRequeridas - fortalezas;
                
                int afinidad = (totalRequeridas > 0) ? (fortalezas * 100) / totalRequeridas : 0;
                
                historialRoles.add(new RolResultado(rol.getNombre(), afinidad, fortalezas, brechas, oportunidades));
            }

            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("resumenCompetencias", resumenCompetencias); 
            dashboardData.put("historialRoles", historialRoles);       

            String jsonResponse = new Gson().toJson(dashboardData);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener los datos del dashboard");
        }
    }
}