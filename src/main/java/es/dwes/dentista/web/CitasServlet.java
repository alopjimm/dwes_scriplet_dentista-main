package es.dwes.dentista.web;

import es.dwes.dentista.dao.ClinicaDAO;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CitasServlet extends HttpServlet {
    private final ClinicaDAO clinicaDAO = new ClinicaDAO();
    private static final String VISTA_LISTADO = "/WEB-INF/vistas/listadoCitas.jsp";
    private static final String VISTA_FORMULARIO = "/WEB-INF/vistas/formularioCita.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (request.getParameter("nueva") != null) {
            try {
                request.setAttribute("pacientes", clinicaDAO.listarPacientesParaCita());
                request.setAttribute("odontologos", clinicaDAO.listarOdontologos());
                request.setAttribute("pacienteSeleccionado", request.getParameter("paciente"));
            } catch (Exception e) {
                log("No se pudieron cargar los datos del formulario de cita", e);
                request.setAttribute("error", "No se han podido cargar pacientes y odontólogos.");
            }
            request.getRequestDispatcher(VISTA_FORMULARIO).forward(request, response);
            return;
        }

        boolean todas = "1".equals(request.getParameter("todas"));
        request.setAttribute("todas", todas);
        try {
            request.setAttribute("citas", clinicaDAO.listarCitas(todas));
        } catch (Exception e) {
            log("No se pudo cargar el listado de citas", e);
            request.setAttribute("error", "No se ha podido obtener el listado de citas.");
        }
        request.getRequestDispatcher(VISTA_LISTADO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            String accion = request.getParameter("accion");
            if ("crear".equals(accion)) {
                String hora = request.getParameter("hora");
                if (hora != null && hora.length() == 5) {
                    hora += ":00";
                }
                clinicaDAO.crearCita(Date.valueOf(request.getParameter("fecha")), Time.valueOf(hora),
                        Integer.parseInt(request.getParameter("id_paciente")),
                        Integer.parseInt(request.getParameter("id_odontologo")), request.getParameter("motivo"));
                response.sendRedirect(request.getContextPath() + "/citas?mensaje=creada");
            } else if ("borrar".equals(accion)) {
                clinicaDAO.borrarCita(Integer.parseInt(request.getParameter("id_cita")));
                response.sendRedirect(request.getContextPath() + "/citas?mensaje=borrada");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            log("No se pudo procesar la operación de citas", e);
            request.setAttribute("error", "No se ha podido completar la operación. Compruebe los datos y vuelva a intentarlo.");
            request.setAttribute("todas", false);
            try {
                request.setAttribute("citas", clinicaDAO.listarCitas(false));
            } catch (Exception errorListado) {
                log("No se pudo recuperar el listado de citas", errorListado);
                request.setAttribute("citas", java.util.Collections.emptyList());
            }
            request.getRequestDispatcher(VISTA_LISTADO).forward(request, response);
        }
    }
}