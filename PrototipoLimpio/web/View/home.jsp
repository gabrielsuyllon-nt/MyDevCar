<%@page import="com.cvproy.model.Usuario"%>
<%@include file="noCache.jsp" %>

<%
    
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

    
    if (usuario == null) {
        response.sendRedirect("../index.jsp");
        return; 
    }
    
    String iniciales = usuario.getIniciales();
    String nombreCompleto = usuario.getNombreCompleto();
    
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Página Principal</title>
    
    
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body class="body-home"> 

    <nav class="navbar">
        <div class="navbar-left">
            <div class="menu-icon" id="menuBtn">&#9776;</div> <div class="user-initials"><%= iniciales %></div>
            <div class="user-name"><%= nombreCompleto %></div>
        </div>
        </nav>

    <div class="sidebar" id="sidebar">
        <a href="configuracion.jsp">Configuración</a>
        <a href="ayuda.jsp">Ayuda</a>
        
        <a href="../LogoutServlet">Cerrar sesión</a> 
    </div>

    <main class="main-content">
        <div class="center-icon">
            <img src="images/LogoP.png" alt="Icono Principal">
            <h2>MyDevCar</h2>
        </div>
        
        <div class="options-grid">
            <button class="option-btn primary" id="btnEvaluarCompetencias">Evaluar competencias</button>
            
            <a href="../NavegarEvaluarRolServlet" class="option-btn secondarya">Evaluar para un rol objetivo</a>
            
            <a href="resultados.jsp" class="option-btn secondaryb">
                Mis resultados
            </a>
            <button class="option-btn secondaryc" id="btnRecomendaciones">Mis recomendaciones</button>
        </div>
    </main>


    <div class="modal-backdrop" id="evaluarModal">

        <div class="modal-content-evaluar">
            <span class="modal-close" id="closeEvaluarModal">&times;</span> 
            <h2 style="text-align:center; margin-top:0;">Tipo de Competencia</h2>
            
            <div class="modal-opcion">
                
                <a href="categorias.jsp?tipo=tecnica" class="modal-link">
                    Registrar competencias técnicas
                </a>
                
                
            </div>

            <div class="modal-opcion">
                
                <a href="categorias.jsp?tipo=blanda" class="modal-link">
                    Registrar competencias blandas
                </a>
                
                
            </div>
        </div>
    </div>
        
    <div class="modal-backdrop" id="recomendacionesModal">
        <div class="modal-content-evaluar">
            <span class="modal-close" id="closeRecomendacionesModal">&times;</span> 
            
            <h2 style="text-align:center; margin-top:0;">Tipo de Recomendación</h2>

            <div class="modal-opcion">
                <a href="../RecServlet?tipo=general" class="modal-link">
                    Generales
                </a>
            </div>

            <div class="modal-opcion">
    <a href="${pageContext.request.contextPath}/RecomendacionesRolServlet" class="modal-link">
        Por Rol Objetivo
    </a>
</div>
        </div>
    </div>
        
    <script>
        
        document.getElementById("menuBtn").onclick = function() {
            
            document.getElementById("sidebar").classList.toggle("active");
        };
        
        const evaluarModal = document.getElementById("evaluarModal");
        const btnEvaluar = document.getElementById("btnEvaluarCompetencias");
        const closeModal = document.getElementById("closeEvaluarModal");
        
        const recModal = document.getElementById("recomendacionesModal");
        const btnRec = document.getElementById("btnRecomendaciones");
        const closeRec = document.getElementById("closeRecomendacionesModal");
        
        btnEvaluar.onclick = function() {
            evaluarModal.style.display = "flex"; 
        }
        
        closeModal.onclick = function() {
            evaluarModal.style.display = "none"; 
        }
        
        btnRec.onclick = function() {
            recModal.style.display = "flex"; 
        }
        closeRec.onclick = function() {
            recModal.style.display = "none"; 
        }
        
        window.onclick = function(event) {
            if (event.target == evaluarModal) {
                evaluarModal.style.display = "none";
            }
            if (event.target == recModal){
                recModal.style.display = "none";
            }
        }
    </script>
    
    </body>
</html>