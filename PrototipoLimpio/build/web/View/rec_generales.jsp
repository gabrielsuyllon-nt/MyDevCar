<%@page import="com.cvproy.model.Usuario"%>
<%@page import="com.cvproy.model.RecCompetencia"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="noCache.jsp" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("../index.jsp");
        return;
    }
    String iniciales = usuario.getIniciales();
    List<RecCompetencia> recomendaciones = (List<RecCompetencia>) request.getAttribute("recomendaciones");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Recomendaciones Generales</title>
    <link rel="stylesheet" href="View/style.css">
    <script src="https://unpkg.com/phosphor-icons"></script>
</head>
<body class="body-dashboard"> 

    <nav class="navbar">
        <div class="navbar-left">
            <i class="ph-lightbulb" style="font-size: 28px; color: #007bff;"></i>
            <span class="navbar-title">Recomendaciones Generales</span>
        </div>
        <div class="navbar-right">
            <a href="View/home.jsp" class="btn-volver-home">Volver a Inicio</a>
            <div class="user-initials"><%= iniciales %></div>
        </div>
    </nav>

    <main class="main-content recomendacion-container">
        
        <% if (recomendaciones == null || recomendaciones.isEmpty()) { %>
            <div class="empty-state">
                <i class="ph-smiley-XE" style="font-size: 64px; color: #ccc;"></i>
                <h2>No tienes recomendaciones pendientes por ahora.</h2>
            </div>
        <% } else { %>
            <div class="recomendaciones-grid">
                <% for (RecCompetencia rec : recomendaciones) { %>
                    <div class="recomendacion-card">
                        <div class="rec-header">
                            <h3><%= rec.getNombreCompetencia() %></h3>
                            <span class="rec-tag <%= rec.getTipoCompetencia().toLowerCase() %>">
                                <%= rec.getTipoCompetencia() %>
                            </span>
                        </div>
                        <div class="rec-body">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px;">
                                <p class="rec-concepto" style="margin-bottom: 0; margin-top: 0;">
                                    <strong>Concepto Clave:</strong> <%= rec.getConceptoClave() %>
                                </p>
                                <%
                                    String dLibro = (rec.getLibroDescripcion() == null) ? "" : rec.getLibroDescripcion().replace("\"", "&quot;");
                                    String dRecurso = (rec.getRecursoDigitalDescripcion() == null) ? "" : rec.getRecursoDigitalDescripcion().replace("\"", "&quot;");
                                %>

                                <i class="ph-info" 
                                   style="font-size: 26px; color: #007bff; cursor: pointer; flex-shrink: 0; margin-left: 15px;" 
                                   title="Ver detalles de los recursos"
                                   data-titulo="<%= rec.getNombreCompetencia() %>"
                                   data-libro="<%= dLibro %>"
                                   data-recurso="<%= dRecurso %>"
                                   onclick="abrirModalSeguro(this)">
                                </i>                               
                               
                            </div>
                            
                            <div class="rec-recursos">
                                <% if (rec.getLibroNombre() != null && !rec.getLibroNombre().isEmpty()) { %>
                                    <div class="recurso-item">
                                        <i class="ph-book-open-text" style="color: #d97706;"></i>
                                        <div>
                                            <strong>Lectura recomendada:</strong><br>
                                            <% if (!rec.getLibroEnlace().equals("N/A") && !rec.getLibroEnlace().isEmpty()) { %>
                                                <a href="<%= rec.getLibroEnlace() %>" target="_blank"><%= rec.getLibroNombre() %></a>
                                            <% } else { %>
                                                <%= rec.getLibroNombre() %>
                                            <% } %>
                                        </div>
                                    </div>
                                <% } %>

                                <% if (rec.getRecursoDigitalNombre() != null && !rec.getRecursoDigitalNombre().isEmpty()) { %>
                                    <div class="recurso-item">
                                        <i class="ph-desktop" style="color: #0ea5e9;"></i>
                                        <div>
                                            <strong>Recurso digital:</strong><br>
                                            <% if (rec.getRecursoDigitalEnlace() != null && !rec.getRecursoDigitalEnlace().isEmpty()) { %>
                                                <a href="<%= rec.getRecursoDigitalEnlace() %>" target="_blank"><%= rec.getRecursoDigitalNombre() %></a>
                                            <% } else { %>
                                                <%= rec.getRecursoDigitalNombre() %>
                                            <% } %>
                                        </div>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } %>

    </main>
        
        
    <div id="modalDetalle" class="modal" onclick="cerrarModal()" 
         style="display: none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); align-items: center; justify-content: center;">

        <div class="modal-content" onclick="event.stopPropagation();" 
             style="position: relative; text-align: left; max-width: 600px; width: 90%; background: white; padding: 25px; border-radius: 12px; box-shadow: 0 5px 15px rgba(0,0,0,0.3);">

            <span onclick="cerrarModal()" style="position: absolute; top: 15px; right: 20px; font-size: 28px; font-weight: bold; color: #aaa; cursor: pointer;">&times;</span>

            <h2 id="modalTitulo" style="margin-top: 0; color: #333; padding-right: 30px;"></h2>
            <hr style="border: none; border-top: 1px solid #eee; margin: 15px 0;">

            <div class="modal-body" style="line-height: 1.6; color: #555;">
                <h4 style="margin-bottom: 10px; color: #d97706; display: flex; align-items: center;">
                    <i style="margin-right: 8px; font-size: 20px;"></i> Sobre la lectura:
                </h4>
                <p id="descLibro" style="margin-top: 0; margin-bottom: 20px;"></p>

                <h4 style="margin-bottom: 10px; color: #0ea5e9; display: flex; align-items: center;">
                    <i style="margin-right: 8px; font-size: 20px;"></i> Sobre el recurso digital:
                </h4>
                <p id="descRecurso" style="margin-top: 0;"></p>
            </div>

            <div style="text-align: right; margin-top: 25px;">
                 <button onclick="cerrarModal()" style="padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 6px; cursor: pointer;">Entendido</button>
            </div>
        </div>
    </div>
        
    <script>
        function abrirModalSeguro(elemento) {
            const titulo = elemento.getAttribute('data-titulo');
            let descLibro = elemento.getAttribute('data-libro');
            let descRecurso = elemento.getAttribute('data-recurso');

            if (!descLibro || descLibro.trim() === '' || descLibro === 'null') {
                 descLibro = 'No hay información detallada disponible para esta lectura.';
            }
            if (!descRecurso || descRecurso.trim() === '' || descRecurso === 'null') {
                 descRecurso = 'No hay información detallada disponible para este recurso digital.';
            }

            document.getElementById('modalTitulo').innerText = titulo;
            document.getElementById('descLibro').innerText = descLibro;
            document.getElementById('descRecurso').innerText = descRecurso;

            document.getElementById('modalDetalle').style.display = 'flex';
        }

        function cerrarModal() {
            document.getElementById('modalDetalle').style.display = 'none';
        }
    </script>
</body>
</html>