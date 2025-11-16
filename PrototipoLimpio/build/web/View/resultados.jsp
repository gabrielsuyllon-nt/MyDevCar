<%@page import="com.cvproy.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <title>Mis Resultados</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/View/style.css">
    
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://unpkg.com/phosphor-icons"></script>
</head>

<body class="body-dashboard">

    
    <nav class="navbar">
        <div class="navbar-left">
            
            <i class="ph-chart-bar" style="font-size: 24px; color: #007bff;"></i>
            <span class="navbar-title">Resultados</span>
        </div>
        <div class="navbar-right">
            
            <a href="${pageContext.request.contextPath}/View/home.jsp" class="btn-volver-home">Volver a Inicio</a>
            
            <div class="user-initials"><%= iniciales %></div>
        </div>
    </nav>

    
    <main class="main-content-d">
        
        <div class="dashboard-grid">
            
            
            <div class="dashboard-card">
                <h2>Resumen de Competencias</h2>
                <div id="chart-container">
                    <canvas id="resumenChart"></canvas>
                </div>
                </div>
            
            
            <div class="dashboard-card">
                <h2>Historial de Evaluaciones</h2>
                <div id="rolesListContainer" class="roles-list">
                    <p>Cargando historial...</p>
                </div>
            </div>
            
        </div>
    </main>

    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            
            fetch('${pageContext.request.contextPath}/ResultadosServlet') 
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error al cargar datos: ' + response.statusText);
                    }
                    return response.json(); 
                })
                .then(data => {
                    console.log("Datos recibidos:", data); 
                    
                    if (data.resumenCompetencias) {
                        renderizarGrafico(data.resumenCompetencias);
                    }
                    
                    renderizarHistorial(data.historialRoles); 
                })
                .catch(error => {
                    console.error(error);
                    document.getElementById('chart-container').innerHTML = "<p style='color:red;'>Error al cargar el gráfico.</p>";
                    document.getElementById('rolesListContainer').innerHTML = "<p style='color:red;'>Error al cargar el historial.</p>";
                });
        });

        function renderizarGrafico(resumenData) {
            const ctx = document.getElementById('resumenChart').getContext('2d');
            
            const labels = Object.keys(resumenData);
            const dataValues = Object.values(resumenData);
            const total = dataValues.reduce((sum, value) => sum + value, 0);
            
            document.getElementById('chart-container').insertAdjacentHTML('afterend', 
             '<h3 style="text-align:center; margin-top: 1rem;">Total de Competencias: ' + total + '</h3>');

            if (total === 0) {
                 document.getElementById('chart-container').innerHTML = "<p style='text-align:center;'>Aún no tienes competencias registradas.</p>";
                 return;
            }

            new Chart(ctx, {
                type: 'pie', 
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Competencias',
                        data: dataValues,
                        backgroundColor: [
                            'rgb(54, 162, 235)', // excepcional
                            'rgb(75, 192, 192)', // eficiente
                            'rgb(255, 205, 86)'  // basico
                        ],
                        hoverOffset: 4
                    }]
                }
            });
        }
        
        function renderizarHistorial(historialArray) {
        const container = document.getElementById('rolesListContainer');
        if (!container) return; 
        container.innerHTML = ''; 

        if (historialArray.length === 0) {
            container.innerHTML = '<p>No hay historial disponible.</p>';
            return;
        }

        historialArray.forEach((item) => {

            let afin = item.afinidad || 0;
            let fort = item.fortalezas || 0;
            let brec = item.brechas || 0;
            let opor = item.oportunidades || 0;
            let nombre = item.nombreRol || 'Rol sin nombre';


            const html = `
                <div class="rol-item">
                    <div class="rol-header">
                        <h3>\${nombre} - <span style="color: #007bff;">\${afin}% AFINIDAD</span></h3>
                        <button class="btn-ver-detalle" onclick="verDetalleRol('\${nombre}')">Ver Detalle</button>
                    </div>
                    <div class="rol-stats">
                        <span>✅ \${fort} Fortalezas</span>
                        <span>❌ \${brec} Brechas</span>
                        <span>💡 \${opor} Oportunidades</span>
                    </div>
                </div>
            `;
            container.insertAdjacentHTML('beforeend', html);
        });
    }

 
        function verDetalleRol(nombreRol) {
            window.location.href = '${pageContext.request.contextPath}/DetalleRolServlet?rol=' + encodeURIComponent(nombreRol);
    }
    </script>

</body>
</html>