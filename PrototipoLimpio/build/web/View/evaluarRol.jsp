<%@page import="com.cvproy.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@include file="noCache.jsp" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("../index.jsp");
        return;
    }
    String iniciales = usuario.getIniciales();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Evaluar Rol Objetivo</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/View/style.css">
    
    <link href="https://cdnjs.cloudflare.com/ajax/libs/slim-select/2.8.2/slimselect.min.css" rel="stylesheet"></link>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/slim-select/2.8.2/slimselect.min.js"></script>
    
</head>

<body class="body-home"> 
    
    <nav class="navbar">
        <div class="navbar-left">
            <span class="navbar-title">Evaluar Rol Objetivo</span>
        </div>
        <div class="navbar-right">
            <a href="${pageContext.request.contextPath}/View/home.jsp" class="btn-volver-home">Volver a Inicio</a>
            <div class="user-initials"><%= iniciales %></div>
        </div>
    </nav>

    <main class="evaluar-rol-container">
        <h1>Registrar nuevo rol</h1>
        
        <form action="${pageContext.request.contextPath}/guardarHabilidadServlet" method="POST">
            
            <div class="form-group">
                <label for="rol-select">Seleccione aquí el rol a evaluar:</label>
                <select id="rol-select" name="rol_id" required>
                    <option value="" data-placeholder="true"></option>
                    
                    <c:forEach var="rol" items="${listaRoles}">
                        <option value="${rol.id}">${rol.nombre}</option>
                    </c:forEach>
                </select>
            </div>

            <hr style="margin-bottom: 2rem;">

            <c:forEach var="categoria" items="${habilidadesAgrupadas}">
                <div class="form-group">
                    <label>${categoria.key}:</label>
                    
                    <select name="habilidades" multiple>
                        <option value="" data-placeholder="true"></option>
                        
                        <c:forEach var="habilidad" items="${categoria.value}">
                            <option value="${habilidad.id_habilidad}" ${habilidadesUsuario.contains(habilidad.id_habilidad) ? 'selected' : ''}>
                                ${habilidad.nombre}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:forEach>

            <div class="action-buttons">

                <button type="submit" class="btn-action btn-eliminar" 
                        formaction="${pageContext.request.contextPath}/EliminarRolServlet"
                        formmethod="POST"
                        onclick="return confirm('¿Estás seguro de que quieres eliminar este rol de tu historial?');">
                    Eliminar
                </button>

                <button type="submit" class="btn-action btn-guardar">
                    Guardar 
                </button>

            </div>
            
        </form>
    </main>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            
            new SlimSelect({
                select: '#rol-select',
                settings: {
                    placeholderText: 'Elija un rol...',
                    allowDeselect: true
                }
            });

            const selectoresHabilidades = document.querySelectorAll('select[name="habilidades"]');
            
            selectoresHabilidades.forEach(selector => {
                new SlimSelect({
                    select: selector,
                    settings: {
                        placeholderText: 'Seleccione una o más habilidades...',
                        closeOnSelect: false,
                        showCheckBoxes: true
                    }
                });
            });
            
        });
    </script>
    </body>
</html>