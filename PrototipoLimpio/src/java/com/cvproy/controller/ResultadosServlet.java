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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;    
import java.util.Map;
import java.util.Set;     
import java.util.stream.Collectors;

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
            Rol rolModel = new Rol();
            Habilidad habModel = new Habilidad();

            Map<String, Integer> resumenCompetencias = compModel.getResumenCompetencias(idUsuario);

            Set<Integer> habilidadesUsuario = habModel.getHabilidadesPorUsuario(idUsuario);
            Map<Integer, Set<Integer>> todosLosRequisitos = rolModel.obtenerTodosLosRequisitos();
            
            List<RolResultado> historialRoles = new ArrayList<>();
            List<Rol> rolesEvaluados = rolModel.getRolesEvaluadosPorUsuario(idUsuario);

            for (Rol rol : rolesEvaluados) {
                Set<Integer> habilidadesRequeridas = todosLosRequisitos.getOrDefault(rol.getId(), Collections.emptySet());

                if (habilidadesRequeridas.isEmpty()) {
                    historialRoles.add(new RolResultado(rol.getNombre(), 100, 0, 0, habilidadesUsuario.size()));
                    continue;
                }

                int totalRequeridas = habilidadesRequeridas.size();
                int fortalezas = 0;

                for (int idHabUsuario : habilidadesUsuario) {
                    if (habilidadesRequeridas.contains(idHabUsuario)) {
                        fortalezas++; 
                    }
                }
                
                int brechas = totalRequeridas - fortalezas;

                int oportunidades = 0;
                for (Integer mySkillId : habilidadesUsuario) {
                    if (!habilidadesRequeridas.contains(mySkillId)) {
                        oportunidades++;
                    }
                }
                
                int afinidad = (totalRequeridas > 0) ? (fortalezas * 100) / totalRequeridas : 0;
                
                historialRoles.add(new RolResultado(rol.getNombre(), afinidad, fortalezas, brechas, oportunidades));
            }

            List<Rol> todosLosRoles = rolModel.getAllRoles(); 
            List<RolResultado> candidatos = new ArrayList<>();

            for (Rol rol : todosLosRoles) {
                Set<Integer> reqs = todosLosRequisitos.getOrDefault(rol.getId(), Collections.emptySet());

                if (reqs.isEmpty()) continue; 

                int matchCount = 0;
                for (Integer reqId : reqs) {
                    if (habilidadesUsuario.contains(reqId)) {
                        matchCount++;
                    }
                }

                int afinidadCalc = (matchCount * 100) / reqs.size();
                if (afinidadCalc > 10) {
                    candidatos.add(new RolResultado(rol.getNombre(), afinidadCalc, matchCount, 0, 0));
                }
            }

            Collections.sort(candidatos, (r1, r2) -> Integer.compare(r2.getAfinidad(), r1.getAfinidad()));
            List<RolResultado> top3Roles = candidatos.stream().limit(3).collect(Collectors.toList());

            Map<String, Object> dashboardData = new HashMap<>();
            dashboardData.put("resumenCompetencias", resumenCompetencias); 
            dashboardData.put("historialRoles", historialRoles);       
            dashboardData.put("topRoles", top3Roles);

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