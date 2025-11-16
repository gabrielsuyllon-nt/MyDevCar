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

@WebServlet(name = "ConfiguracionServlet", urlPatterns = {"/ConfiguracionServlet"})
public class ConfiguracionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        String mensaje = "";
        String tipoMensaje = "error";

        if ("actualizarEmail".equals(accion)) {
            String nuevoEmail = request.getParameter("nuevoEmail");
      
            if (usuario.actualizarCorreo(usuario.getId(), nuevoEmail)) {
                usuario.setEmail(nuevoEmail); 
                session.setAttribute("usuarioLogueado", usuario);
                mensaje = "Correo actualizado correctamente.";
                tipoMensaje = "exito";
            } else {
                mensaje = "Error al actualizar el correo. Puede que ya esté en uso.";
            }

        } else if ("actualizarPass".equals(accion)) {
            String passActual = request.getParameter("passActual");
            String passNuevo = request.getParameter("passNuevo");
            String passConfirm = request.getParameter("passConfirm");

            if (!passNuevo.equals(passConfirm)) {
                mensaje = "Las nuevas contraseñas no coinciden.";
            } else if (!usuario.verificarPassword(usuario.getId(), passActual)) {
                mensaje = "La contraseña actual es incorrecta.";
            } else {
                if (usuario.actualizarPassword(usuario.getId(), passNuevo)) {
                    mensaje = "Contraseña actualizada correctamente.";
                    tipoMensaje = "exito";
                } else {
                    mensaje = "Error al actualizar la contraseña.";
                }
            }
        }

        session.setAttribute("configMsg", mensaje);
        session.setAttribute("configMsgType", tipoMensaje);
        response.sendRedirect("View/configuracion.jsp");
    }
}