# Proyecto Clínica Dental

Aplicación web para gestionar pacientes y citas de una clínica dental. Está implementada
con **Servlets, JSP, JSTL y JDBC** sobre MySQL siguiendo una estructura MVC sencilla y es
compatible con **Tomcat 9**.

## Estructura

```text
src/main/
├── java/es/dwes/dentista/
│   ├── dao/ClinicaDAO.java              # Consultas y conexiones JDBC
│   ├── model/                           # JavaBeans para pacientes, citas y odontólogos
│   └── web/                             # PacientesServlet y CitasServlet
├── resources/dentista.sql               # Creación y datos de ejemplo
└── webapp/
    ├── index.jsp                        # Menú principal
    ├── estilos.css
    └── WEB-INF/
        ├── web.xml
        └── vistas/                      # Vistas JSP; no accesibles directamente
```

Los servlets reciben las peticiones, llaman al DAO y envían los datos a las vistas. Las JSP
usan EL/JSTL para presentar la información; no contienen scriptlets ni consultas SQL. Los
formularios usan `POST` para altas y borrados y, tras procesarlos, los servlets redirigen
para evitar repetir operaciones al recargar.

## Funcionalidad y rutas

- `/pacientes`: listado de pacientes y número de citas.
- `/pacientes?nuevo=1`: formulario de alta de paciente.
- `/citas`: próximas citas; `/citas?todas=1` muestra el histórico.
- `/citas?nueva=1`: formulario de cita. Admite `&paciente=ID` para preseleccionar paciente.
- Los formularios permiten crear citas/pacientes y anular citas/eliminar pacientes.
- Al borrar un paciente, MySQL elimina sus citas asociadas mediante `ON DELETE CASCADE`.

## Base de datos

La base `dentista` contiene las tablas `paciente`, `odontologos` y `cita`. Para crearla e
importar los datos de ejemplo:

```bash
mysql -u usuario -pusuario < src/main/resources/dentista.sql
```

Las citas de ejemplo se generan con `DATE_ADD(CURDATE(), INTERVAL n DAY)`, de modo que el
conjunto siempre contiene citas próximas y pasadas.

Por defecto, la aplicación conecta con MySQL en `localhost:3306`, base `dentista`, usuario
`usuario` y contraseña `usuario`. La configuración está centralizada en `ClinicaDAO` y se
puede sobrescribir mediante variables de entorno:

| Variable | Valor predeterminado |
|---|---|
| `DENTISTA_DB_URL` | `jdbc:mysql://localhost:3306/dentista?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8` |
| `DENTISTA_DB_USER` | `usuario` |
| `DENTISTA_DB_PASSWORD` | `usuario` |

## Requisitos y despliegue

- Java 21 y Maven 3.8 o posterior.
- MySQL Server accesible con la base de datos y las credenciales configuradas.
- **Apache Tomcat 9.0.x**. Tomcat 9 implementa Servlet 4.0/JSP 2.3 con `javax.*`; Tomcat 10
  usa `jakarta.*` y no es compatible con este proyecto.

Construir el WAR:

```bash
mvn clean package
```

El archivo generado es `target/scriplet_dentista.war`. Cópialo al directorio `webapps` de
Tomcat 9; la aplicación quedará disponible en `/scriplet_dentista`.

## Entrega

Para preparar el ZIP solicitado, comprime el proyecto excluyendo `target/` y cualquier
carpeta `build/`. Publica el repositorio en la plataforma elegida y entrega su enlace junto
con el ZIP en Classroom.
