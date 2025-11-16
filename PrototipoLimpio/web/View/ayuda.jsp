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
    <title>Ayuda - CV Proy</title>
    <link rel="stylesheet" href="style.css">
    <script src="https://unpkg.com/phosphor-icons"></script>
</head>
<body class="body-dashboard">

    <nav class="navbar">
        <div class="navbar-left">
            <i class="ph-question" style="font-size: 28px; color: #007bff;"></i>
            <span class="navbar-title">Ayuda</span>
        </div>
        <div class="navbar-right">
            <a href="home.jsp" class="btn-volver-home">Volver a Inicio</a>
            <div class="user-initials"><%= iniciales %></div>
        </div>
    </nav>

    <main class="main-content help-container">
        
        <div class="help-grid">
            
            <div class="dashboard-card help-card">
                <h3><i class="ph-caret-right"></i> ¿Cómo registro mis competencias?</h3>
                <div class="help-answer">
                    <p style="text-align: justify">En la página principal, presione el botón <strong>"Evaluar competencias"</strong>. Luego, seleccione en la ventana emergente el tipo de competencia que desea registrar. Una vez en la ventana de competencias, verá varias categorías. Presionando en cada una puede ver las competencias que esta contiene para marcar.</p>
                    <p style="text-align: justify">Es necesario que en cada pestaña de checks presione el botón <strong>"Guardar"</strong> para conservar sus elecciones. Tras terminar, presione el botón <strong>"Volver"</strong>.</p>
                    <p><em>Se sugiere encarecidamente registrar en ambos tipos de competencias (blandas y técnicas) para una mejor experiencia de evaluación y recomendación.</em></p>
                </div>
            </div>

            <div class="dashboard-card help-card">
                <h3><i class="ph-caret-right"></i> ¿Cómo registro un nuevo rol?</h3>
                <div class="help-answer">
                    <p style="text-align: justify">En la página principal, presione el botón <strong>"Evaluar para un rol objetivo"</strong>. Verá dos opciones: "Nuevo rol" y "Actualizar rol". Seleccione la opción para un nuevo rol.</p>
                    <p style="text-align: justify">Una vez en la ventana de registro por rol, verá un menú desplegable en la parte superior. Seleccione el rol que desea evaluar. Luego, seleccione en cada categoría del formulario, las habilidades con las que cuenta.</p>
                    <p style="text-align: justify">Cuando haya terminado, presione <strong>"guardar"</strong>.</p>
                </div>
            </div>

            <div class="dashboard-card help-card">
                <h3><i class="ph-caret-right"></i> ¿Cómo actualizo mis competencias y habilidades?</h3>
                <div class="help-answer">
                    <p style="text-align: justify"><strong>Competencias:</strong> Para actualizar una o varias de tus competencias, basta con presionar el botón "Evaluar competencias" en la página principal. Luego selecciona el tipo de competencia y marca/desmarca las competencias que desees actualizar.</p>
                    <p style="text-align: justify"><strong>Habilidades:</strong> En la página principal, presione el botón "Evaluar para un rol objetivo". En la ventana siga los mismos pasos que cuando registró ese rol. Incluso puede desmarcar en los que considere haber cometido equivocación.</p>
                </div>
            </div>

            <div class="dashboard-card help-card">
                <h3><i class="ph-caret-right"></i> ¿Cómo puedo ver el resultado de mis evaluaciones?</h3>
                <div class="help-answer">
                    <p style="text-align: justify">En la página principal, presione el botón <strong>"Mis resultados"</strong>.</p>
                    <p style="text-align: justify">Verá una ventana resumen. Si desea ver más acerca de sus habilidades según rol, presione el botón "Detalles" en la tarjeta del rol correspondiente. Verá una categorización de sus habilidades según brechas, oportunidades y fortalezas identificadas.</p>
                </div>
            </div>

            <div class="dashboard-card help-card">
                <h3><i class="ph-caret-right"></i> ¿Las recomendaciones son TODAS gratuitas?</h3>
                <div class="help-answer">
                    <p style="text-align: justify">No todas las lecturas sugeridas son de libre acceso, varias se encuentran como e-books en Amazon. Los enlaces al material gratuito sí se encuentran disponibles.</p>
                    <p style="text-align: justify">Respecto a los recursos digitales seleccionados, todos cuentan con planes o versiones gratuitas.</p>
                </div>
            </div>

        </div>
    </main>

</body>
</html>