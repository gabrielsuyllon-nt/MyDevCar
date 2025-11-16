package com.cvproy.controller;

import com.cvproy.model.Competencia;
import com.cvproy.model.Usuario;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/GuardarCompetenciasServlet")
public class GuardarCompetencias extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        try {
            
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
            int id_persona = usuario.getId();
            
            
            int id_categoria = Integer.parseInt(request.getParameter("id_categoria"));
            String tipo = request.getParameter("tipo");
            
            
            String[] idsCompetenciasMarcadas = request.getParameterValues("competencias_marcadas");
            
            
            Competencia objComp = new Competencia();
            boolean exito = objComp.guardarCompetenciasUsuario(id_persona, id_categoria, idsCompetenciasMarcadas);
            
            
            response.sendRedirect("View/categorias.jsp?tipo=" + tipo);
            
        } catch (Exception e) {
            e.printStackTrace();
            
            response.sendRedirect("View/home.jsp");
        }
    }
}