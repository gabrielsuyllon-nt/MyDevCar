package com.cvproy.controller;

import com.cvproy.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "EliminarRolServlet", urlPatterns = {"/EliminarRolServlet"})
public class EliminarRolServlet extends HttpServlet {

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
            int idUsuario = usuario.getId();

            Usuario dao = new Usuario();
            dao.eliminarRol(idUsuario, idRol);

            response.sendRedirect("View/resultados.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("View/home.jsp?error=eliminarRol");
        }
    }
}