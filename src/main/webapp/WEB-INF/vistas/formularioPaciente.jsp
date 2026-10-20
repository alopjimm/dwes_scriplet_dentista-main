<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Nuevo paciente</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos.css" />
  </head>
  <body>
    <h1>Nuevo paciente</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/pacientes">
      <input type="hidden" name="accion" value="crear"/>
      Nombre <input type="text" name="nombre" required/><br/>
      Apellidos <input type="text" name="apellidos" required/><br/>
      DNI <input type="text" name="dni" maxlength="9" required/><br/>
      Fecha de nacimiento <input type="date" name="fecha_nacimiento"/><br/>
      Teléfono <input type="text" name="telefono"/><br/>
      Correo electrónico <input type="email" name="email"/><br/>
      Dirección <input type="text" name="direccion"/><br/>
      Alergias o notas <input type="text" name="alergias"/><br/>
      <input type="submit" value="Guardar"/>
    </form>
    <p><a href="${pageContext.request.contextPath}/index.jsp">Volver al inicio</a></p>
  </body>
</html>