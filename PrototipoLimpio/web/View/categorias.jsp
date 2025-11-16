<%@page import="com.cvproy.model.Usuario"%>
<%@page import="com.cvproy.model.Competencia"%>
<%@page import="com.cvproy.model.Categoria"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@include file="noCache.jsp" %>
<%
   
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("../index.jsp");
        return; 
    }
    
    
    String tipo = request.getParameter("tipo");
    if (tipo == null || tipo.isEmpty()) {
       
        response.sendRedirect("View/home.jsp");
        return;
    }
    
    
    String tituloPagina = "Registro de competencias " + (tipo.equals("tecnica") ? "técnicas" : "blandas");

   
    Competencia objComp = new Competencia();
    List<Categoria> categorias = objComp.getCategoriasParaUsuario(usuario.getId(), tipo);
    
    
    String iniciales = usuario.getIniciales();
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><%= tituloPagina %></title>
    <link rel="stylesheet" type="text/css" href="style.css">
    <script src="https://unpkg.com/phosphor-icons"></script>
</head>
<body class="body-competencias"> 

    <nav class="competencias-header">
        <div class="header-left">
            <i class="ph-note-pencil" style="font-size: 24px; color: #007bff;"></i>
            <span class="header-page-title"><%= tituloPagina %></span>
        </div>
        
        <div class="header-right">
            <div class="user-initials"><%= iniciales %></div>
            
        </div>
    </nav>

    <main class="cat-grid-container">
        
        <div class="cat-grid-title">Categorías</div>
        
        <div class="cat-grid">
            <% 
            if (categorias != null && !categorias.isEmpty()) {
                for (Categoria cat : categorias) {
                    
                    String claseColor = cat.isCompletada() ? "completed" : "";
            %>
                    <a href="competencias.jsp?id_cat=<%= cat.getId() %>&tipo=<%= tipo %>&nombre=<%= URLEncoder.encode(cat.getNombre(), "UTF-8") %>" 
                       class="cat-btn <%= claseColor %>">
                        <%= cat.getNombre() %>
                    </a>
            <%
                }
            } else {
            %>
                <p class="no-categorias">No hay categorías para mostrar.</p>
            <%
            }
            %>
        </div>
        
        
        <div class="cat-grid-footer">
            <form action="../guardarNivelServlet" method="POST">
                <button type="submit" class="cat-btn-registrar">VOLVER</button>
            </form>
        </div>
        
    </main>

</body>
</html>