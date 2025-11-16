package com.cvproy.controller;

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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DetalleRolServlet", urlPatterns = {"/DetalleRolServlet"})
public class DetalleRolServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        String nombreRol = request.getParameter("rol");

        if (usuario == null || nombreRol == null || nombreRol.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros.");
            return;
        }

        nombreRol = URLDecoder.decode(nombreRol, "UTF-8");

        try {
            int idUsuario = usuario.getId();
            
            Rol rolModel = new Rol();
            Habilidad habModel = new Habilidad();

            int idRol = rolModel.getIdPorNombre(nombreRol);
            if (idRol == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rol no encontrado.");
                return;
            }

            Map<Integer, String> habilidadesUsuario = habModel.getHabilidadesMapPorUsuario(idUsuario);
            Map<Integer, String> habilidadesRequeridas = rolModel.getHabilidadesMapPorRol(idRol);

            List<String> listaFortalezas = new ArrayList<>();
            List<String> listaBrechas = new ArrayList<>();
            List<String> listaOportunidades = new ArrayList<>();

            for (Map.Entry<Integer, String> entry : habilidadesUsuario.entrySet()) {
                if (habilidadesRequeridas.containsKey(entry.getKey())) {
                    listaFortalezas.add(entry.getValue());
                } else {
                    listaOportunidades.add(entry.getValue());
                }
            }

            for (Map.Entry<Integer, String> entry : habilidadesRequeridas.entrySet()) {
                if (!habilidadesUsuario.containsKey(entry.getKey())) {
                    listaBrechas.add(entry.getValue());
                }
            }

            int totalRequeridas = habilidadesRequeridas.size();
            int numFortalezas = listaFortalezas.size();
            int numBrechas = listaBrechas.size();
            int numOportunidades = listaOportunidades.size();

            int afinidad = (totalRequeridas > 0) ? (numFortalezas * 100) / totalRequeridas : 100;

            int totalPie = numFortalezas + numBrechas + numOportunidades;
            int porcFortaleza = (totalPie > 0) ? (numFortalezas * 100) / totalPie : 0;
            int porcBrecha = (totalPie > 0) ? (numBrechas * 100) / totalPie : 0;
            int porcOportunidad = 100 - porcFortaleza - porcBrecha; 

            request.setAttribute("nombreRol", nombreRol);
            request.setAttribute("afinidad", afinidad);
            
            request.setAttribute("listaFortalezas", listaFortalezas);
            request.setAttribute("listaBrechas", listaBrechas);
            request.setAttribute("listaOportunidades", listaOportunidades);
            
            request.setAttribute("porcFortaleza", porcFortaleza);
            request.setAttribute("porcBrecha", porcBrecha);
            request.setAttribute("porcOportunidad", porcOportunidad);
            
            request.getRequestDispatcher("View/detalleRol.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al calcular los detalles.");
        }
    }
}