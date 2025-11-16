package com.cvproy.controller;

import com.cvproy.model.Habilidad;
import com.cvproy.model.Rol;
import com.cvproy.model.Usuario; 
import jakarta.servlet.RequestDispatcher; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "NavegarEvaluarRolServlet", urlPatterns = {"/NavegarEvaluarRolServlet"})
public class NavegarEvaluarRolServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            response.sendRedirect("../index.jsp"); 
            return;
        }

        Rol rolModel = new Rol();
        Habilidad habilidadModel = new Habilidad();

        try {
            int idUsuario = usuario.getId();
            
            List<Rol> listaRoles = rolModel.getAllRoles();
            Map<String, List<Habilidad>> habilidadesAgrupadas = habilidadModel.getHabilidadesAgrupadas();
            
            Set<Integer> habilidadesUsuario = habilidadModel.getHabilidadesPorUsuario(idUsuario);

            request.setAttribute("listaRoles", listaRoles);
            request.setAttribute("habilidadesAgrupadas", habilidadesAgrupadas);
            request.setAttribute("habilidadesUsuario", habilidadesUsuario);

            RequestDispatcher dispatcher = request.getRequestDispatcher("View/evaluarRol.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("View/home.jsp?error=cargaRol");
        }
    }
}