package com.cvproy.controller; 

import com.cvproy.model.Usuario; 

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
       
        Usuario uc = new Usuario();
        
        
        boolean esValido = uc.validarLogin(email, password);
        
        
        if (esValido) {
            
            Usuario usuario = uc.getUsuarioPorEmail(email);
            
            
            if (usuario != null) {
                
                HttpSession session = request.getSession();
                
              
                session.setAttribute("usuarioLogueado", usuario);
                
               
                response.sendRedirect("View/home.jsp");
                
            } else {
                
                response.sendRedirect("index.jsp?error=true");
            }
            
        } else {
            
            response.sendRedirect("index.jsp?error=true");
        }
    }

}