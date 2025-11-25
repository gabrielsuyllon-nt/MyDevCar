<%@page import="com.cvproy.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="noCache.jsp" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("../login.jsp"); 
        return; 
    }
    String iniciales = usuario.getIniciales();
    String nombreCompleto = usuario.getNombreCompleto(); 
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Detalle de ${nombreRol}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/View/style.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://unpkg.com/phosphor-icons"></script>
        
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.31/jspdf.plugin.autotable.min.js"></script> 
    
    <style>
        body { background: linear-gradient(135deg, #007bff, #00c6ff); display: block; min-height: 100vh;}
        .detalle-container { max-width: 1200px; margin: 20px auto; padding: 20px; }
        .detalle-header { 
            background: #f0f0f0; 
            padding: 15px 25px; 
            border-radius: 10px;
            display: flex; 
            justify-content: space-between; 
            align-items: center;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .detalle-header h1 { margin: 0; font-size: 1.8em; color: #333; }
        
        .detalle-grid { display: grid; grid-template-columns: 1fr 2fr; gap: 20px; }
        .chart-card, .list-card { background: #fff; border-radius: 10px; padding: 20px; box-shadow: 0 4px 10px rgba(0,0,0,0.05); }
        
        .afinidad-box {
            background: #F9A825; 
            border: 4px solid #333;
            border-radius: 5px;
            padding: 15px;
            text-align: center;
            margin-top: 20px;
        }
        .afinidad-box h3 { margin: 0 0 10px 0; color: #333; font-size: 1.1em; }
        .afinidad-box .porcentaje { font-size: 3em; font-weight: 700; color: #000; }
        
        .skills-list-container { display: flex; flex-direction: column; gap: 15px; }
        .skills-list { border: 2px solid; border-radius: 8px; }
        .skills-list h3 { margin: 0; padding: 12px; font-size: 1.2em; color: #fff; }
        .skills-list ul { list-style: none; padding: 10px 15px; margin: 0; max-height: 200px; overflow-y: auto; }
        .skills-list li { padding: 8px 0; border-bottom: 1px solid #eee; color: #333; }
        .skills-list li:last-child { border-bottom: none; }
        
        .fortalezas { border-color: #2E7D32; }
        .fortalezas h3 { background: #4CAF50; } 
        
        .brechas { border-color: #C62828; }
        .brechas h3 { background: #F44336; } 
        
        .oportunidades { border-color: #F9A825; }
        .oportunidades h3 { background: #FFC107; } 
    </style>
</head>
<body>

    <div class="detalle-container">
        
        <header class="detalle-header">
            <h1 id="tituloRol"><i class="ph-chart-pie-slice"></i> RESULTADOS PARA: ${nombreRol}</h1>
            
            <div class="navbar-right">
                <button onclick="generarReporte()" class="btn-volver-home" style="background-color: #dc2626; color: white; padding: 12px 20px; border: none; border-radius: 5px; cursor: pointer; margin-bottom: 0; margin-right: 10px; font-size: 1rem; font-weight: bold;">
                    <i class="ph-file-pdf" style="margin-right: 5px; font-size:18px; vertical-align: middle;"></i> Descargar PDF
                </button>
                
                <a href="${pageContext.request.contextPath}/View/resultados.jsp" class="btn-volver-home">Volver a Resultados</a>
                <div class="user-initials"><%= iniciales %></div>
            </div>
        </header>

        <div class="detalle-grid">
            
            <div class="chart-card">
                <canvas id="detalleChart"></canvas>
                <div class="afinidad-box">
                    <h3>PORCENTAJE DE AFINIDAD</h3>
                    <div class="porcentaje" id="valAfinidad">${afinidad}%</div>
                </div>
            </div>
            
            <div class="list-card skills-list-container">
                
                <div class="skills-list fortalezas">
                    <h3><i class="ph-check-circle"></i> FORTALEZAS</h3>
                    <ul id="listaFortalezas">
                        <c:forEach var="skill" items="${listaFortalezas}"><li>${skill}</li></c:forEach>
                        <c:if test="${empty listaFortalezas}"><li>No se encontraron fortalezas.</li></c:if>
                    </ul>
                </div>
                
                <div class="skills-list brechas">
                    <h3><i class="ph-x-circle"></i> BRECHAS</h3>
                    <ul id="listaBrechas">
                        <c:forEach var="skill" items="${listaBrechas}"><li>${skill}</li></c:forEach>
                        <c:if test="${empty listaBrechas}"><li>¡Felicidades! No tienes brechas.</li></c:if>
                    </ul>
                </div>
                
                <div class="skills-list oportunidades">
                    <h3><i class="ph-lightbulb"></i> OPORTUNIDADES</h3>
                    <ul id="listaOportunidades">
                        <c:forEach var="skill" items="${listaOportunidades}"><li>${skill}</li></c:forEach>
                        <c:if test="${empty listaOportunidades}"><li>No se encontraron oportunidades.</li></c:if>
                    </ul>
                </div>
                 
            </div>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const ctx = document.getElementById('detalleChart').getContext('2d');
            const dataValues = [${porcFortaleza}, ${porcBrecha}, ${porcOportunidad}];
            
            new Chart(ctx, {
                type: 'pie', 
                data: {
                    labels: [
                        'FORTALEZAS (' + ${porcFortaleza} + '%)',
                        'BRECHAS (' + ${porcBrecha} + '%)',
                        'OPORTUNIDADES (' + ${porcOportunidad} + '%)'
                    ],
                    datasets: [{
                        data: dataValues,
                        backgroundColor: ['rgb(75, 192, 192)', 'rgb(255, 99, 132)', 'rgb(255, 205, 86)'],
                        hoverOffset: 4
                    }]
                }
            });
        });

        function generarReporte() {
            const { jsPDF } = window.jspdf;
            const doc = new jsPDF();

            const titulo = document.getElementById('tituloRol').innerText.trim(); 
            const afinidad = document.getElementById('valAfinidad').innerText.trim();
            const usuarioNombre = "<%= nombreCompleto %>";
            const fecha = new Date().toLocaleDateString();

            function leerLista(idLista) {
                const lista = document.getElementById(idLista);
                const items = lista.getElementsByTagName('li');
                let datos = [];
                for (let item of items) {
                    datos.push([item.innerText]);
                }
                return datos;
            }

            const datosFortalezas = leerLista('listaFortalezas');
            const datosBrechas = leerLista('listaBrechas');
            const datosOportunidades = leerLista('listaOportunidades');

            doc.setFontSize(18);
            doc.setTextColor(0, 123, 255); // Azul
            doc.text("Reporte de Evaluación de Rol", 14, 22);

            doc.setFontSize(10);
            doc.setTextColor(100);
            doc.text("Usuario: " + usuarioNombre, 14, 30);
            doc.text("Fecha: " + fecha, 14, 35);
            doc.text("Generado automáticamente por: MyDevCAR",14,40);
            doc.line(14, 40, 196, 45); 

            doc.setFontSize(14);
            doc.setTextColor(0);
            doc.text(titulo, 14, 50);
            
            doc.setFontSize(12);
            doc.text("Afinidad Obtenida: " + afinidad, 14, 58);

            let finalY = 65;

            if (datosFortalezas.length > 0) {
                doc.autoTable({
                    startY: finalY,
                    head: [['Fortalezas Identificadas']],
                    body: datosFortalezas,
                    headStyles: { fillColor: [76, 175, 80] }, 
                    theme: 'grid'
                });
                finalY = doc.lastAutoTable.finalY + 10;
            }

            if (datosBrechas.length > 0) {
                doc.autoTable({
                    startY: finalY,
                    head: [['Brechas a Cubrir']],
                    body: datosBrechas,
                    headStyles: { fillColor: [244, 67, 54] }, 
                    theme: 'grid'
                });
                finalY = doc.lastAutoTable.finalY + 10;
            }

            if (datosOportunidades.length > 0) {
                doc.autoTable({
                    startY: finalY,
                    head: [['Oportunidades de Mejora']],
                    body: datosOportunidades,
                    headStyles: { fillColor: [255, 193, 7], textColor: 50 }, 
                    theme: 'grid'
                });
            }

            const pageCount = doc.internal.getNumberOfPages();
            for(let i = 1; i <= pageCount; i++) {
                doc.setPage(i);
                doc.setFontSize(8);
                doc.text('Página ' + i + ' de ' + pageCount, 196, 285, { align: 'right' });
            }

            doc.save('Reporte_' + titulo.replace(/[^a-zA-Z0-9]/g, '_')+'_' + new Date().toISOString().slice(0,10)+'.pdf');
        }
    </script>

</body>
</html>