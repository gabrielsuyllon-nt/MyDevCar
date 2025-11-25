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
    String nombreCompleto=usuario.getNombreCompleto();
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mis Resultados</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/View/style.css">
    
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://unpkg.com/phosphor-icons"></script>
    <%--PDF--%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.31/jspdf.plugin.autotable.min.js"></script> 
</head>

<body class="body-dashboard">

    
    <nav class="navbar">
        <div class="navbar-left">
            <i class="ph-chart-bar" style="font-size: 36px; color: #007bff;"></i>
            <span class="navbar-title">Resultados</span>
        </div>
        <div class="navbar-right">
            <%--PDF--%>
            <button onclick="generarReporte()" class="btn-volver-home" style="background-color: #dc2626; color: white; padding: 12px 20px; border: none; border-radius: 5px; cursor: pointer; margin-bottom: 20px; font-size: 1rem;">
                <i class="ph-file-pdf" style="margin-right: 5px;"></i> Descargar Reporte en PDF
            </button>
            
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
            
            <div class="dashboard-card full-width-card">
                <h2>Roles Sugeridos para Ti</h2>
                <p style="text-align:center; color:#666; margin-bottom:20px;">
                    Basado en tus habilidades actuales, tienes alta compatibilidad con:
                </p>
                
                <div id="topRolesContainer" class="top-roles-grid">
                    </div>
            </div>                      
        </div>
    </main>

    
    <script>
        let nombreUsuario="<%= nombreCompleto %>";
        let datosGlobalesReporte = null; 
        
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
                    
                    datosGlobalesReporte = data; 
                    
                    if (data.resumenCompetencias) {
                        renderizarGrafico(data.resumenCompetencias);
                    }
                    
                    if (data.historialRoles){
                        renderizarHistorial(data.historialRoles);
                    }
                    //TOP3
                    if (data.topRoles && data.topRoles.length > 0) {
                            renderizarTopRoles(data.topRoles);
                    } else {
                        document.getElementById('topRolesContainer').innerHTML = "<p style='text-align:center'>Registra más habilidades para obtener sugerencias.</p>";
                    }              
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

        function renderizarTopRoles(topRoles) {
                const container = document.getElementById('topRolesContainer');
                let html = '';


                const medallas = ['🥇', '🥈', '🥉']; 

                topRoles.forEach((rol, index) => {
                    let colorBarra = '#10b981'; 
                    if(rol.afinidad < 50) colorBarra = '#f59e0b'; 

                    let medalEmoji = medallas[index] || '';

                    html += `
                        <div class="role-match-card">
                            <div class="medal">\${medalEmoji}</div>
                            <h3>\${rol.nombreRol}</h3>

                            <div class="progress-container">
                                <div class="progress-bar" style="width: \${rol.afinidad}%; background: \${colorBarra};"></div>
                            </div>
                            <div class="match-score">\${rol.afinidad}% Compatible</div>

                            <p style="font-size:0.85em; color:#666; margin-top:5px;">
                                \${rol.fortalezas} fortalezas encontradas
                            </p>
                        </div>
                    `;
                });
                container.innerHTML = html;
            }
  
        function generarReporte() {
            if (!datosGlobalesReporte) {
                alert("Espera a que carguen los datos...");
                return;
            }

            const { jsPDF } = window.jspdf;
            const doc = new jsPDF();

            doc.setFontSize(20);
            doc.setTextColor(0, 123, 255);
            doc.text("Reporte de Perfil Profesional", 14, 22);

            doc.setFontSize(10);
            doc.setTextColor(100);
            doc.text('Usuario: '+nombreUsuario,14, 26);
            doc.text(`Fecha de emisión: `+new Date().toISOString().slice(0, 10), 14, 30);
            doc.text("Generado por: MyDevCAR", 14, 35);

            doc.setDrawColor(200);
            doc.line(14, 40, 196, 40);

            doc.setFontSize(14);
            doc.setTextColor(0);
            doc.text("1. Resumen de Competencias", 14, 50);

            const resumenData = datosGlobalesReporte.resumenCompetencias;
            const filasResumen = Object.keys(resumenData).map(key => [key.toUpperCase(), resumenData[key]]);

            doc.autoTable({
                startY: 55,
                head: [['Nivel', 'Cantidad']],
                body: filasResumen,
                theme: 'grid',
                headStyles: { fillColor: [0, 123, 255] },
                tableWidth: 'wrap'
            });

            let finalY = doc.lastAutoTable.finalY + 15; 

            doc.setFontSize(14);
            doc.text("2. Roles Sugeridos (Top 3)", 14, finalY);

            const topRoles = datosGlobalesReporte.topRoles || [];

            if (topRoles.length > 0) {
                const filasTop = topRoles.map((rol, index) => [
                    (index + 1) + "° Lugar", 
                    rol.nombreRol,
                    rol.afinidad + "% Compatible",
                    rol.fortalezas + " coincidencias"
                ]);

                doc.autoTable({
                    startY: finalY + 5,
                    head: [['Ranking', 'Rol Objetivo', 'Afinidad', 'Fortalezas']],
                    body: filasTop,
                    theme: 'striped',
                    headStyles: { fillColor: [16, 185, 129] },
                    styles: { halign: 'center' },
                    columnStyles: { 
                        0: { fontStyle: 'bold', halign: 'center', cellWidth: 30 },
                        1: { halign: 'left', fontStyle: 'bold' }
                    }
                });
                finalY = doc.lastAutoTable.finalY + 15;
            } else {
                doc.setFontSize(10);
                doc.setTextColor(100);
                doc.text("No hay suficientes datos para sugerir roles.", 14, finalY + 7);
                finalY += 20;
            }

            doc.setFontSize(14);
            doc.setTextColor(0);
            doc.text("3. Historial de Evaluaciones", 14, finalY);

            const historial = datosGlobalesReporte.historialRoles || [];

            if (historial.length === 0) {
                doc.setFontSize(10);
                doc.text("No hay evaluaciones registradas.", 14, finalY + 10);
            } else {
                const filasHistorial = historial.map(rol => [
                    rol.nombreRol,
                    rol.afinidad + "%",
                    rol.fortalezas,
                    rol.brechas,
                    rol.oportunidades
                ]);

                doc.autoTable({
                    startY: finalY + 5,
                    head: [['Rol Evaluado', 'Afinidad', 'Fortalezas', 'Brechas', 'Oportunidades']],
                    body: filasHistorial,
                    theme: 'plain', 
                    headStyles: { fillColor: [52, 58, 64], textColor: 255 }, 
                    styles: { halign: 'center', fontSize: 9 },
                    columnStyles: { 0: { halign: 'left', fontStyle: 'bold' } }
                });
            }

            const pageCount = doc.internal.getNumberOfPages();
            for(let i = 1; i <= pageCount; i++) {
                doc.setPage(i);
                doc.setFontSize(8);
                doc.setTextColor(150);
                doc.text('Página ' + i + ' de ' + pageCount, 196, 285, { align: 'right' });
            }

            doc.save('Reporte_Profesional_CVProy'+new Date().toISOString().slice(0, 10)+'.pdf');
        }
    </script>

</body>
</html>