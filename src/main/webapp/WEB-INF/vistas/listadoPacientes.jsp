<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Pacientes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos.css" />
  </head>
  <body>
    <h1>Listado de pacientes</h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <c:if test="${param.mensaje == 'creado'}"><p class="nota">Paciente dado de alta.</p></c:if>
    <c:if test="${param.mensaje == 'borrado'}"><p class="nota">Paciente eliminado.</p></c:if>
    <p><a href="${pageContext.request.contextPath}/pacientes?nuevo=1">Registrar paciente</a></p>
    <table>
      <thead><tr><th>Id</th><th>Nombre</th><th>Apellidos</th><th>DNI</th><th>Fecha nacimiento</th><th>Teléfono</th><th>Email</th><th>Dirección</th><th>Alergias</th><th>Citas</th><th>Acciones</th></tr></thead>
      <tbody>
        <c:forEach var="paciente" items="${pacientes}">
          <tr>
            <td><c:out value="${paciente.id}"/></td>
            <td><c:out value="${paciente.nombre}"/></td>
            <td><c:out value="${paciente.apellidos}"/></td>
            <td><c:out value="${paciente.dni}"/></td>
            <td><c:out value="${paciente.fechaNacimiento}"/></td>
            <td><c:out value="${paciente.telefono}"/></td>
            <td><c:out value="${paciente.email}"/></td>
            <td><c:out value="${paciente.direccion}"/></td>
            <td><c:out value="${paciente.alergias}"/></td>
            <td><c:out value="${paciente.numeroCitas}"/></td>
            <td>
              <a href="${pageContext.request.contextPath}/citas?nueva=1&amp;paciente=${paciente.id}">Nueva cita</a>
              <form method="post" action="${pageContext.request.contextPath}/pacientes">
                <input type="hidden" name="accion" value="borrar"/>
                <input type="hidden" name="id_paciente" value="${paciente.id}"/>
                <input type="submit" value="Borrar"/>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <p><a href="${pageContext.request.contextPath}/index.jsp">Volver al inicio</a></p>
  </body>
</html>