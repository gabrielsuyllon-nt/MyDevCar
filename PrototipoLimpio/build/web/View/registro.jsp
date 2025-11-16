<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Usuario</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <div class="register-layout">
        <div class="register-left-panel">
            <div class="header-icon">
                <img src="images/LogoSec.png" alt="Icono de página">
            </div>
            <div class="page-description">
                <h2>¿Qué es MyDevCAR?</h2><br>Es una herramienta en línea para la orientación profesional en diversas áreas de TI, especialmente desarrollo de software. Permite al usuario identificar las brechas y oportunidades que tiene.
            </div>
        </div>
        
        <div class="register-right-panel">
            <h2 class="register-title">Nuevo usuario</h2>
            <form method="post" action="../RegistrarServlet">
                <input type="text" name="nombre" placeholder="Nombre" required>
                <input type="text" name="apellido" placeholder="Apellido" required>
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="password" name="password" placeholder="Contraseña" required>
                <input type="password" name="password_rep" placeholder="Repetir contraseña" required>
                
                <button type="submit" class="btn-submit">Registrarse</button>
            </form>
            <a href="../index.jsp" class="reg_login_link">Ya tengo una cuenta</a>
        </div>
    </div>
</body>
</html>
