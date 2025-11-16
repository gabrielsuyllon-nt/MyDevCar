<%@page import="com.cvproy.model.Usuario"%>
<%@page import="com.cvproy.model.Competencia"%> <%-- La "caja" de habilidades --%>
<%@page import="java.util.List"%>
<%@include file="noCache.jsp" %>

<%
    
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("../index.jsp");
        return; 
    }
    
    
    int id_categoria = 0;
    String tipo = request.getParameter("tipo"); 
    
    try {
        id_categoria = Integer.parseInt(request.getParameter("id_cat"));
    } catch (NumberFormatException e) {
       
        response.sendRedirect("home.jsp");
        return;
    }
    
   
    Competencia objComp = new Competencia();
    List<Competencia> competencias = objComp.getCompetenciasPorCategoria(usuario.getId(), id_categoria);
    String nombreCategoria = request.getParameter("nombre");
    if (nombreCategoria == null || nombreCategoria.isEmpty()) {
        nombreCategoria = "Habilidades";
    
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Habilidades</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body class="body-competencias"> 

    <div class="modal-backdrop-habilidades">
        <div class="modal-content-habilidades">
            
            
            <span class="modal-close" 
                  onclick="window.location.href='categorias.jsp?tipo=<%= tipo %>'">
                  &times;
            </span> 
            
            <h2 class="habilidades-title"><%= nombreCategoria %></h2> 
            
            <form action="../GuardarCompetenciasServlet" method="POST" class="habilidades-form">
                
                <input type="hidden" name="id_categoria" value="<%= id_categoria %>">
                <input type="hidden" name="tipo" value="<%= tipo %>">
                
                <div class="habilidades-list">
              
                <%
                if (competencias != null && !competencias.isEmpty()) {
                    for (Competencia comp : competencias) {
                       
                        String estaMarcada = comp.isMarcada() ? "checked" : "";
                %>
                        <div class="habilidad-item">
                            <label for="comp_<%= comp.getId() %>">
                                <%= comp.getNombre() %> 
                            </label>
                            <input type="checkbox" 
                                   name="competencias_marcadas" 
                                   value="<%= comp.getId() %>" 
                                   id="comp_<%= comp.getId() %>"
                                   <%= estaMarcada %>>
                        </div>
                <%
                    }
                } else {
                %>
                    <p class="no-habilidades">No hay habilidades para esta categoría.</p>
                <%
                }
                %>
                </div> 
                
                <div class="habilidades-footer">
                    <button type="submit" class="btn-guardar">GUARDAR</button>
                </div>
                
            </form>
            
        </div>
    </div>

</body>
</html>