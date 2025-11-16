package com.cvproy.controller;

import com.cvproy.model.Nivel;
import com.cvproy.model.Usuario;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/guardarNivelServlet") 
public class guardarNivelServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        HttpSession session = request.getSession(false); 

        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("../index.jsp"); 
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        int id_persona = usuario.getId();

        try {
          
            Nivel nivel = new Nivel();
            
           
            nivel.guardarNivelPersona(id_persona);
          

        } catch (Exception e) {
            e.printStackTrace();
       
        }
        
        response.sendRedirect("View/home.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
    

