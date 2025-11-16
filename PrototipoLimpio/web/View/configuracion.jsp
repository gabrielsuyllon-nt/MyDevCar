<%@page import="com.cvproy.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="noCache.jsp" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        // Redirige al login si no hay sesión
        response.sendRedirect("../login.jsp");
        return;
    }
    
    String iniciales = usuario.getIniciales();

    String msg = (String) session.getAttribute("configMsg");
    String msgType = (String) session.getAttribute("configMsgType");
    
    session.removeAttribute("configMsg");
    session.removeAttribute("configMsgType");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Configuración - CV Proy</title>
    <link rel="stylesheet" href="style.css">
    <script src="https://unpkg.com/phosphor-icons"></script>
</head>
<body class="body-dashboard">

    <nav class="navbar">
        <div class="navbar-left">
            <i class="ph-gear" style="font-size: 28px; color: #007bff;"></i>
            <span class="navbar-title">Configuración de Cuenta</span>
        </div>
        <div class="navbar-right">
            <a href="home.jsp" class="btn-volver-home">Volver a Inicio</a>
            <div class="user-initials"><%= iniciales %></div>
        </div>
    </nav>

    <main class="main-content recomendacion-container">
        
        <% if (msg != null) { %>
            <div class="alert <%= "exito".equals(msgType) ? "alert-success" : "alert-error" %>">
                <%-- Icono dinámico según el tipo de mensaje --%>
                <i class="<%= "exito".equals(msgType) ? "ph-check-circle" : "ph-warning-circle" %>" 
                   style="margin-right: 8px; font-size: 1.2em; vertical-align: middle;"></i>
                <%= msg %>
            </div>
        <% } %>

        <div class="dashboard-grid" style="padding-top: 40px">
            
            <div class="dashboard-card">
                <div class="rec-header" style="background: none; padding-left: 0; border-bottom: 2px solid #f0f0f0;">
                    <h3><i class="ph-envelope-simple" style="margin-right: 10px; color: #007bff;"></i> Actualizar Correo</h3>
                </div>
                
                <form action="${pageContext.request.contextPath}/ConfiguracionServlet" method="post" class="config-form">
                    <input type="hidden" name="accion" value="actualizarEmail">
                    
                    <div style="margin-top: 20px;">
                        <label>Correo Actual:</label>
                        <input type="text" value="<%= usuario.getEmail() %>" disabled 
                               style="background: #f1f5f9; color: #64748b; cursor: not-allowed;">
                    </div>
                    
                    <div>
                        <label>Nuevo Correo:</label>
                        <input type="email" name="nuevoEmail" required placeholder="nuevo@correo.com">
                    </div>
                    
                    <button type="submit" class="btn-guardar" 
                            style="margin-top: 20px; width: 100%; background-color: #007bff; color: white; border: none;">
                        Guardar Nuevo Correo
                    </button>
                </form>
            </div>

            <div class="dashboard-card">
                <div class="rec-header" style="background: none; padding-left: 0; border-bottom: 2px solid #f0f0f0;">
                    <h3><i class="ph-lock-key" style="margin-right: 10px; color: #007bff;"></i> Cambiar Contraseña</h3>
                </div>

                <form action="${pageContext.request.contextPath}/ConfiguracionServlet" method="post" class="config-form">
                    <input type="hidden" name="accion" value="actualizarPass">
                    
                    <div style="margin-top: 20px;">
                        <label>Contraseña Actual:</label>
                        <input type="password" name="passActual" required placeholder="••••••••">
                    </div>
                    
                    <div>
                        <label>Nueva Contraseña:</label>
                        <input type="password" name="passNuevo" required placeholder="Nueva contraseña segura">
                    </div>

                    <div>
                        <label>Confirmar Nueva Contraseña:</label>
                        <input type="password" name="passConfirm" required placeholder="Repite la nueva contraseña">
                    </div>
                    
                    <button type="submit" class="btn-guardar" 
                            style="margin-top: 20px; width: 100%; background-color: #007bff; color: white; border: none;">
                        Actualizar Contraseña
                    </button>
                </form>
            </div>
            
        </div>
    </main>

</body>
</html>