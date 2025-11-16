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
    <title>Recomendaciones por Rol</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/View/style.css">
    <script src="https://unpkg.com/phosphor-icons"></script>
    
    <style>
        body { background: linear-gradient(135deg, #007bff, #00c6ff); display: block; }
        .rec-container { max-width: 1000px; margin: 0 auto; padding-top: 20px; }
        
        .rec-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #f0f0f0;
            padding: 10px 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .rec-header .header-title { display: flex; align-items: center; gap: 15px; }
        .rec-header h1 { font-size: 1.5em; margin: 0; color: #333; }
        .rec-header .icon-bg { background: #007bff; padding: 10px; border-radius: 50%; display: flex; }
        .rec-header .user-initials { /* Tu estilo normal */ }
        .btn-volver-home { /* Tu estilo normal */ }
        
        .rec-body {
            background: rgba(255,255,255,0.1);
            backdrop-filter: blur(5px);
            padding: 20px;
            margin-top: 20px;
            border-radius: 8px;
        }
        
        .rec-dropdown-bar { display: flex; align-items: center; gap: 15px; margin-bottom: 20px; }
        .rec-dropdown-bar h2 { margin: 0; font-size: 1.2em; color: white; }
        
        .rol-select {
            font-size: 1.1em;
            padding: 8px 12px;
            border: 2px solid #333;
            border-radius: 20px;
            background: #fff;
            font-weight: bold;
            cursor: pointer;
        }

        .recomendacion-list {
            max-height: 60vh; /* Altura máxima antes de scroll */
            overflow-y: auto;
            padding: 10px;
            background: rgba(255,255,255,0.8); /* Fondo blanco para las tarjetas */
            border-radius: 10px;
            border: 4px solid #333;
        }
        
        .recomendacion-card {
            display: grid;
            grid-template-columns: 1fr 2fr; /* Columna concepto / Columna links */
            gap: 20px;
            padding: 15px;
            background: #f0f0f0;
            border: 3px solid #333;
            border-radius: 8px;
            margin-bottom: 15px;
        }
        .recomendacion-card:last-child { margin-bottom: 0; }
        
        .rec-card-left h3 {
            margin: 0 0 10px 0;
            background: #D32F2F; /* Rojo */
            color: white;
            padding: 8px;
            border-radius: 5px;
            font-size: 1.1em;
            text-align: center;
        }
        .rec-card-left p { margin: 0; font-size: 0.9em; }
        .btn-ver-detalle { /* Tu estilo normal */ }

        .rec-card-right {
            font-size: 0.9em;
            line-height: 1.6;
            color: #333;
        }
        .rec-card-right strong { display: block; margin-top: 10px; }
        .rec-card-right a { color: #007bff; }
        
        .empty-state {
             padding: 40px; text-align: center; font-size: 1.2em; color: #333; font-weight: bold;
        }
        
        /* === ZONA 1: AÑADIR ESTILOS PARA EL MODAL Y TEXTO TRUNCADO === */
        
        .truncated-text {
            display: -webkit-box;
            -webkit-line-clamp: 2; /* Número de líneas a mostrar */
            -webkit-box-orient: vertical;  
            overflow: hidden;
            text-overflow: ellipsis;
            margin-top: 5px;
        }

        .modal-backdrop {
            display: none; /* Oculto por defecto */
            position: fixed; 
            z-index: 1000; 
            left: 0;
            top: 0;
            width: 100%; 
            height: 100%; 
            overflow: auto; 
            background-color: rgb(0,0,0); 
            background-color: rgba(0,0,0,0.6); /* Fondo oscuro semitransparente */
            padding-top: 60px;
        }

        .modal-content {
            background-color: #fefefe;
            margin: 5% auto; 
            padding: 20px;
            border: 1px solid #888;
            width: 60%; 
            border-radius: 8px;
            box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
            position: relative;
        }
        
        .modal-content h2 {
            margin-top: 0;
            color: #D32F2F;
        }
        
        .modal-content p {
            line-height: 1.6;
            white-space: pre-wrap; /* Respeta los saltos de línea del texto */
        }

        .modal-close-btn {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            position: absolute;
            top: 10px;
            right: 20px;
        }
        .modal-close-btn:hover,
        .modal-close-btn:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
        
    </style>
</head>
<body>

    <div class="rec-container">
    
        <header class="rec-header">
            <div class="header-title">
                <div class="icon-bg">
                    <i class="ph-file-search" style="font-size: 24px; color: white;"></i>
                </div>
                <h1>RECOMENDACIONES POR ROL</h1>
            </div>
            
            <div class="navbar-right">
                <a href="${pageContext.request.contextPath}/View/home.jsp" class="btn-volver-home">Volver a Inicio</a>
                <div class="user-initials"><%= iniciales %></div>
            </div>
        </header>

        <div class="rec-body">
        
            <div class="rec-dropdown-bar">
                <h2>RECOMENDACIONES PARA</h2>
                <form action="RecomendacionesRolServlet" method="GET" id="rolForm">
                    <select name="id_rol_seleccionado" class="rol-select" onchange="document.getElementById('rolForm').submit();">
                        <c:forEach var="rol" items="${listaRoles}">
                            <option value="${rol.id}" <c:if test="${rol.id == idRolSeleccionado}">selected</c:if>>
                                ${rol.nombre}
                            </option>
                        </c:forEach>
                        <c:if test="${empty listaRoles}">
                            <option>No has evaluado roles</option>
                        </c:if>
                    </select>
                </form>
            </div>
            
            <div class="recomendacion-list">
            
                <c:if test="${empty listaRecomendaciones}">
                    <div class="empty-state">
                        <p>¡Buenas noticias! No se encontraron brechas, o no hay recomendaciones registradas para las brechas de este rol.</p>
                    </div>
                </c:if>
                
                <c:forEach var="rec" items="${listaRecomendaciones}">
                    <div class="recomendacion-card">
                        <div class="rec-card-left">
                            <h3>${rec.habilidadNombre}</h3>
                            <p><strong>Concepto clave:</strong></p>
                            <p>${rec.concepto_clave}</p>
                            
                            <button class="btn-ver-detalle" style="margin-top:10px;" onclick="abrirModal(this)">
                                VER DETALLE
                            </button>
                            
                            <div class="modal-data" style="display:none;">
                                <div class="curso-desc-full">${rec.curso_desc}</div>
                                <div class="proy-sug-full">${rec.sugerencia_proy}</div>
                            </div>
                            
                        </div>
                        <div class="rec-card-right">
                            <strong>Curso recomendado:</strong>
                            <a href="${rec.curso_enlace}" target="_blank">${rec.curso_nombre}</a>
                            
                            <strong>Documentacion oficial:</strong>
                            <a href="${rec.doc_enlace}" target="_blank">${rec.doc_nombre}</a>
                            
                            <strong>Sugerencia de mini-proyecto:</strong>
                            <p class="truncated-text">${rec.sugerencia_proy}</p>
                        </div>
                    </div>
                </c:forEach>
                
            </div>
        </div>
    </div>

    <div id="detalleModal" class="modal-backdrop">
        <div class="modal-content">
            <span class="modal-close-btn" onclick="cerrarModal()">&times;</span>
            <h2 id="modalHabilidad"></h2> <hr>
            <h3>Descripción del Curso</h3>
            <p id="modalCursoDesc"></p> <h3>Sugerencia de Mini-Proyecto</h3>
            <p id="modalProySug"></p> </div>
    </div>

    <script>
        
        var modal = document.getElementById('detalleModal');

        
        function abrirModal(boton) {
            
            const card = boton.closest('.recomendacion-card');
            
            
            const habilidad = card.querySelector('h3').innerText;
            const cursoDesc = card.querySelector('.curso-desc-full').innerHTML;
            const proySug = card.querySelector('.proy-sug-full').innerHTML;
            
            
            document.getElementById('modalHabilidad').innerText = habilidad;
            document.getElementById('modalCursoDesc').innerHTML = cursoDesc;
            document.getElementById('modalProySug').innerHTML = proySug;
            
            
            modal.style.display = 'block';
        }

        
        function cerrarModal() {
            modal.style.display = 'none';
        }

        
        window.onclick = function(event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        };
    </script>

</body>
</html>