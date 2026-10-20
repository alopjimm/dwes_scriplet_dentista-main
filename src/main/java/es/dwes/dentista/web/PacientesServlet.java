package es.dwes.dentista.web;

import es.dwes.dentista.dao.ClinicaDAO;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PacientesServlet extends HttpServlet {
    private final ClinicaDAO clinicaDAO = new ClinicaDAO();
    private static final String VISTA_LISTADO = "/WEB-INF/vistas/listadoPacientes.jsp";
    private static final String VISTA_FORMULARIO = "/WEB-INF/vistas/formularioPaciente.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (request.getParameter("nuevo") != null) {
            request.getRequestDispatcher(VISTA_FORMULARIO).forward(request, response);
            return;
        }
        try {
            request.setAttribute("pacientes", clinicaDAO.listarPacientes());
            request.getRequestDispatcher(VISTA_LISTADO).forward(request, response);
        } catch (Exception e) {
            log("No se pudo cargar el listado de pacientes", e);
            request.setAttribute("error", "No se ha podido obtener el listado de pacientes.");
            request.getRequestDispatcher(VISTA_LISTADO).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String accion = request.getParameter("accion");
            if ("crear".equals(accion)) {
                String fecha = request.getParameter("fecha_nacimiento");
                Date fechaNacimiento = fecha == null || fecha.isBlank() ? null : Date.valueOf(fecha);
                clinicaDAO.crearPaciente(request.getParameter("nombre"), request.getParameter("apellidos"),
                        request.getParameter("dni"), fechaNacimiento, request.getParameter("telefono"),
                        request.getParameter("email"), request.getParameter("direccion"),
                        request.getParameter("alergias"));
                response.sendRedirect(request.getContextPath() + "/pacientes?mensaje=creado");
            } else if ("borrar".equals(accion)) {
                clinicaDAO.borrarPaciente(Integer.parseInt(request.getParameter("id_paciente")));
                response.sendRedirect(request.getContextPath() + "/pacientes?mensaje=borrado");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            log("No se pudo procesar la operación de pacientes", e);
            request.setAttribute("error", "No se ha podido completar la operación. Compruebe los datos y vuelva a intentarlo.");
            request.setAttribute("pacientes", safeListarPacientes());
            request.getRequestDispatcher(VISTA_LISTADO).forward(request, response);
        }
    }

    private Object safeListarPacientes() {
        try {
            return clinicaDAO.listarPacientes();
        } catch (Exception e) {
            log("No se pudo recuperar el listado de pacientes", e);
            return java.util.Collections.emptyList();
        }
    }
}