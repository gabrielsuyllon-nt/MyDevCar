<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Iniciar Sesión</title>
    <link rel="stylesheet" type="text/css" href="View/style.css">
</head>
<body>
    <div class="login-container">
        <div class="header-icon">
             <img src="View/images/LogoP.png" alt="Icono de página">
        </div>
        <div class="page-name">
            MyDevCAR
        </div>
        
        <form method="post" action="LoginServlet">
            <h2 class="log_title">Inicio de sesión</h2>
            
            <%
                // Verifica si la URL tiene el parámetro ?error=true
                String error = request.getParameter("error");
                if (error != null && error.equals("true")) {
            %>
                <div style="color: #dc2626; background-color: #fee2e2; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center; border: 1px solid #fca5a5;">
                    <strong>¡Error!</strong> Correo o contraseña incorrectos.
                </div>
            <%
                }
            %>            
            
            <input type="email" name="email" placeholder="Correo Electronico" required>
            <input type="password" name="password" placeholder="Contraseña" required>
            
            <button type="submit" class="btn-submit">Ingresar</button>
        </form>
        
        <a href="View/registro.jsp" class="btn-register-link">Registrarse</a>
    </div>
</body>
</html>