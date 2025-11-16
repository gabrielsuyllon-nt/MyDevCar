package com.cvproy.controller;

import com.cvproy.model.RecCompetencia;
import com.cvproy.model.Usuario;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "RecServlet", urlPatterns = {"/RecServlet"})
public class RecServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendRedirect("../index.jsp");
            return;
        }

        String tipo = request.getParameter("tipo"); 

        if ("general".equals(tipo)) {
            RecCompetencia rec = new RecCompetencia();
            List<RecCompetencia> listaRecomendaciones = rec.obtenerRecomendacionesPorUsuario(usuario.getId());

            request.setAttribute("recomendaciones", listaRecomendaciones);
            
            request.getRequestDispatcher("View/rec_generales.jsp").forward(request, response);
            
        } else {
            response.sendRedirect("View/home.jsp");
        }
    }
}