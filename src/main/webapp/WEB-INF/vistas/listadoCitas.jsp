<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Citas</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/estilos.css" />
  </head>
  <body>
    <h1><c:choose><c:when test="${todas}">Todas las citas</c:when><c:otherwise>Próximas citas</c:otherwise></c:choose></h1>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
    <c:if test="${param.mensaje == 'creada'}"><p class="nota">Cita creada.</p></c:if>
    <c:if test="${param.mensaje == 'borrada'}"><p class="nota">Cita anulada.</p></c:if>
    <table>
      <thead><tr><th>Id cita</th><th>Fecha</th><th>Hora</th><th>Paciente</th><th>Motivo</th><th>Odontólogo</th><th>Acciones</th></tr></thead>
      <tbody>
        <c:forEach var="cita" items="${citas}">
          <tr>
            <td><c:out value="${cita.id}"/></td>
            <td><c:out value="${cita.fecha}"/></td>
            <td><c:out value="${cita.hora}"/></td>
            <td><c:out value="${cita.apellidosPaciente}"/>, <c:out value="${cita.nombrePaciente}"/></td>
            <td><c:out value="${cita.motivo}"/></td>
            <td><c:out value="${cita.odontologo}"/></td>
            <td>
              <form method="post" action="${pageContext.request.contextPath}/citas">
                <input type="hidden" name="accion" value="borrar"/>
                <input type="hidden" name="id_cita" value="${cita.id}"/>
                <input type="submit" value="Anular"/>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <p>
      <c:choose>
        <c:when test="${todas}"><a href="${pageContext.request.contextPath}/citas">Ver próximas citas</a></c:when>
        <c:otherwise><a href="${pageContext.request.contextPath}/citas?todas=1">Ver histórico completo</a></c:otherwise>
      </c:choose>
      | <a href="${pageContext.request.contextPath}/citas?nueva=1">Nueva cita</a>
      | <a href="${pageContext.request.contextPath}/index.jsp">Volver al inicio</a>
    </p>
  </body>
</html>