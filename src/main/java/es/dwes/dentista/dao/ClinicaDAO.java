package es.dwes.dentista.dao;

import es.dwes.dentista.model.Cita;
import es.dwes.dentista.model.Odontologo;
import es.dwes.dentista.model.Paciente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ClinicaDAO {
    private static final String URL = System.getenv().getOrDefault(
            "DENTISTA_DB_URL",
            "jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8");
    private static final String USUARIO = System.getenv().getOrDefault("DENTISTA_DB_USER", "usuario");
    private static final String CONTRASENA = System.getenv().getOrDefault("DENTISTA_DB_PASSWORD", "usuario");

    private Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encuentra el driver MySQL Connector/J", e);
        }
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    public List<Paciente> listarPacientes() throws SQLException {
        String sql = "SELECT p.id_paciente, p.nombre, p.apellidos, p.dni, p.fecha_nacimiento, "
                   + "p.telefono, p.email, p.direccion, p.alergias, "
                   + "(SELECT COUNT(*) FROM cita c WHERE c.id_paciente = p.id_paciente) AS numero_citas "
                   + "FROM paciente p ORDER BY p.apellidos, p.nombre";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                pacientes.add(new Paciente(
                        resultado.getInt("id_paciente"), resultado.getString("nombre"),
                        resultado.getString("apellidos"), resultado.getString("dni"),
                        resultado.getDate("fecha_nacimiento"), resultado.getString("telefono"),
                        resultado.getString("email"), resultado.getString("direccion"),
                        resultado.getString("alergias"), resultado.getInt("numero_citas")));
            }
        }
        return pacientes;
    }

    public List<Paciente> listarPacientesParaCita() throws SQLException {
        String sql = "SELECT id_paciente, nombre, apellidos FROM paciente ORDER BY apellidos, nombre";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                pacientes.add(new Paciente(resultado.getInt("id_paciente"), resultado.getString("nombre"),
                        resultado.getString("apellidos"), null, null, null, null, null, null, 0));
            }
        }
        return pacientes;
    }

    public List<Odontologo> listarOdontologos() throws SQLException {
        String sql = "SELECT id_odontologo, nombre, especialidad FROM odontologos ORDER BY nombre";
        List<Odontologo> odontologos = new ArrayList<>();
        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                odontologos.add(new Odontologo(resultado.getInt("id_odontologo"),
                        resultado.getString("nombre"), resultado.getString("especialidad")));
            }
        }
        return odontologos;
    }

    public List<Cita> listarCitas(boolean todas) throws SQLException {
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, p.nombre, p.apellidos, o.nombre AS odontologo "
                   + "FROM cita c JOIN paciente p ON p.id_paciente = c.id_paciente "
                   + "JOIN odontologos o ON o.id_odontologo = c.id_odontologo "
                   + (todas ? "" : "WHERE c.fecha >= CURDATE() ") + "ORDER BY c.fecha, c.hora";
        List<Cita> citas = new ArrayList<>();
        try (Connection conexion = conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                citas.add(new Cita(resultado.getInt("id_cita"), resultado.getDate("fecha"),
                        resultado.getTime("hora"), resultado.getString("nombre"),
                        resultado.getString("apellidos"), resultado.getString("motivo"),
                        resultado.getString("odontologo")));
            }
        }
        return citas;
    }

    public void crearPaciente(String nombre, String apellidos, String dni, Date fechaNacimiento,
                              String telefono, String email, String direccion, String alergias) throws SQLException {
        String sql = "INSERT INTO paciente (nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conectar(); PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nombre);
            sentencia.setString(2, apellidos);
            sentencia.setString(3, dni);
            if (fechaNacimiento == null) {
                sentencia.setNull(4, java.sql.Types.DATE);
            } else {
                sentencia.setDate(4, fechaNacimiento);
            }
            sentencia.setString(5, telefono);
            sentencia.setString(6, email);
            sentencia.setString(7, direccion);
            sentencia.setString(8, alergias);
            sentencia.executeUpdate();
        }
    }

    public void crearCita(Date fecha, Time hora, int idPaciente, int idOdontologo, String motivo) throws SQLException {
        String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = conectar(); PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setDate(1, fecha);
            sentencia.setTime(2, hora);
            sentencia.setInt(3, idPaciente);
            sentencia.setInt(4, idOdontologo);
            sentencia.setString(5, motivo);
            sentencia.executeUpdate();
        }
    }

    public void borrarPaciente(int id) throws SQLException {
        borrarPorId("DELETE FROM paciente WHERE id_paciente = ?", id);
    }

    public void borrarCita(int id) throws SQLException {
        borrarPorId("DELETE FROM cita WHERE id_cita = ?", id);
    }

    private void borrarPorId(String sql, int id) throws SQLException {
        try (Connection conexion = conectar(); PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }
}