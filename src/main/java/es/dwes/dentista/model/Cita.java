package es.dwes.dentista.model;

import java.sql.Date;
import java.sql.Time;

public class Cita {
    private final int id;
    private final Date fecha;
    private final Time hora;
    private final String nombrePaciente;
    private final String apellidosPaciente;
    private final String motivo;
    private final String odontologo;

    public Cita(int id, Date fecha, Time hora, String nombrePaciente, String apellidosPaciente,
                String motivo, String odontologo) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.nombrePaciente = nombrePaciente;
        this.apellidosPaciente = apellidosPaciente;
        this.motivo = motivo;
        this.odontologo = odontologo;
    }

    public int getId() { return id; }
    public Date getFecha() { return fecha; }
    public Time getHora() { return hora; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getApellidosPaciente() { return apellidosPaciente; }
    public String getMotivo() { return motivo; }
    public String getOdontologo() { return odontologo; }
}