package com.cvproy.controller;

import com.cvproy.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "guardarHabilidadServlet", urlPatterns = {"/guardarHabilidadServlet"})
public class guardarHabilidadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión no válida");
            return;
        }

        try {

            int idRol = Integer.parseInt(request.getParameter("rol_id")); 
            String[] idsHabilidades = request.getParameterValues("habilidades"); 
            int idUsuario = usuario.getId();
            

            Usuario dao = new Usuario();
            

            boolean guardadoExitoso = dao.guardarEvaluacionRol(idUsuario, idRol, idsHabilidades);

            if (guardadoExitoso) {
                response.sendRedirect(request.getContextPath() + "/View/resultados.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/View/home.jsp?error=guardarRol");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al guardar la evaluación");
        }
    }
}