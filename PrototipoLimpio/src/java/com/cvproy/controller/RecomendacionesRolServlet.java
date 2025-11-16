package com.cvproy.controller;

import com.cvproy.model.RecRol;
import com.cvproy.model.Habilidad;
import com.cvproy.model.Rol;
import com.cvproy.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RecomendacionesRolServlet", urlPatterns = {"/RecomendacionesRolServlet"})
public class RecomendacionesRolServlet extends HttpServlet {

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
            Rol rolModel = new Rol();
            Habilidad habModel = new Habilidad();

            List<Rol> rolesEvaluados = rolModel.getRolesEvaluadosPorUsuario(idUsuario);

            int idRolSeleccionado = 0;
            String paramRol = request.getParameter("id_rol_seleccionado");
            
            if (paramRol != null && !paramRol.isEmpty()) {

                idRolSeleccionado = Integer.parseInt(paramRol);
            } else if (!rolesEvaluados.isEmpty()) {

                idRolSeleccionado = rolesEvaluados.get(0).getId();
            }

            List<Integer> listaBrechas = new ArrayList<>();
            if (idRolSeleccionado > 0) {
                Map<Integer, String> habilidadesUsuario = habModel.getHabilidadesMapPorUsuario(idUsuario);
                Map<Integer, String> habilidadesRequeridas = rolModel.getHabilidadesMapPorRol(idRolSeleccionado);
                
                for (Map.Entry<Integer, String> entry : habilidadesRequeridas.entrySet()) {
                    if (!habilidadesUsuario.containsKey(entry.getKey())) {
                        listaBrechas.add(entry.getKey()); 
                    }
                }
            }

            RecRol recModel = new RecRol(); 
            List<Map<String, String>> listaRecomendaciones = recModel.getRecomendacionesPorHabilidades(listaBrechas);


            request.setAttribute("listaRoles", rolesEvaluados);
            request.setAttribute("idRolSeleccionado", idRolSeleccionado);
            request.setAttribute("listaRecomendaciones", listaRecomendaciones); 

            request.getRequestDispatcher("View/rec_rol.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar las recomendaciones.");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}