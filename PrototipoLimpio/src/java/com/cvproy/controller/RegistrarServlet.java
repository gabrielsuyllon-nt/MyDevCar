package com.cvproy.controller;

import com.cvproy.model.Usuario;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; 
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/RegistrarServlet")
public class RegistrarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String pass1 = request.getParameter("password");
        String pass2 = request.getParameter("password_rep");
        
        
        if (!pass1.equals(pass2)) {
            
            response.sendRedirect("View/registro.jsp?error=pass"); 
            return; 
        }
        
        
        Usuario uc = new Usuario(); 
        boolean exito = uc.registrarUsuario(nombre, apellido, email, pass1);
        
        
        if (exito) {
            
            response.sendRedirect("index.jsp?exito=true");
        } else {
            
            response.sendRedirect("View/registro.jsp?error=email");
        }
    }
}