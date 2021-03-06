package pe.edu.tecsup.api.utils;

public class Constant {

    public static final String[] GOOGLEPLUS_ALLOW_DOMAINS = {"tecsup.edu.pe"};

    public static final String PATH_NEWS = "/var/data/tecsup-api/news/";

    public static final String APP_TECSUP = "TECSUP";
    public static final String APP_TECSUP_DOCENTE = "TECSUP-DOCENTES";
    public static final String APP_TECSUP_SOPORTE = "TECSUP-SOPORTE";
    public static final String APP_TECSUP_PCC = "TECSUP-PCC";

    public static final String USUARIO_TIPO_EMPLEADO = "EMPLEADO";
    public static final String USUARIO_TIPO_ALUMNO = "ALUMNO";
    public static final String USUARIO_TIPO_PARTICIPANTE = "PARTICIPANTE";

    public static final int SEGURIDAD_USUARIO_ACEPTADA = 0;
    public static final int SEGURIDAD_USUARIO_NO_ENCONTRADO = -1;
    public static final int SEGURIDAD_USUARIO_DESHABILITADO = -2;
    public static final int SEGURIDAD_USUARIO_BLOQUEADO = -3;
    public static final int SEGURIDAD_USUARIO_BLOQUEADO_TEMPORALMENTE = -4;
    public static final int SEGURIDAD_USUARIO_PASSWORD_MAL = -5;
    public static final int SEGURIDAD_USUARIO_PASSWORD_DEBE_CAMBIAR = -6;
    public static final int SEGURIDAD_USUARIO_ERROR_DESCONOCIDO = -7;

    public static final String AUTHORITY_SEVA_ADMINISTRADOR = "ROLE_ADMINISTRADOR";
    public static final String AUTHORITY_SEVA_SECRETARIA = "ROLE_SECRETARIA";
    public static final String AUTHORITY_SEVA_DIRECTOR = "ROLE_DIRECTOR";
    public static final String AUTHORITY_SEVA_JEFE_DEPARTAMENTO = "ROLE_JEFE";
    public static final String AUTHORITY_SEVA_DOCENTE = "ROLE_DOCENTE";
    public static final String AUTHORITY_SEVA_ESTUDIANTE = "ROLE_ESTUDIANTE";
    public static final String AUTHORITY_PORTAL_SOPORTE = "ROLE_SOPORTE";

    public static final int ROLE_SEVA_ADMINISTRADOR = 132; // SEVA - Administrador
    public static final int ROLE_SEVA_SECRETARIA = 134; // SEVA - Secretaría Docente
    public static final int ROLE_SEVA_DIRECTOR = 142; // SEVA - Director
    public static final int ROLE_SEVA_JEFE_DEPARTAMENTO = 146; // SEVA - JefedeDepartamento
    public static final int ROLE_SEVA_DOCENTE = 143; // SEVA - Docente
    public static final int ROLE_SEVA_ESTUDIANTE = 145; // SEVA - Estudiante
    public static final int ROLE_SEVA_ESTUDIANTE_ANTIGUO = 45; // SEVA - Estudiante Antiguo
    public static final int ROLE_PORTAL_SOPORTE = 214; // PORTAL - Soporte

    public static final int PAGINATION_LIMIT = 20;

    public static final String EMAIL_FROM = "osi-apps@tecsup.edu.pe";
    public static final String EMAIL_ADMINISTRATOR = "ebenites@tecsup.edu.pe";

    public static final String FIREBASE_PAYLOAD_GO = "GO";
    public static final String FIREBASE_PAYLOAD_GO_DEBT = "DEBT";
    public static final String FIREBASE_PAYLOAD_GO_CALENDAR = "CALENDAR";
    public static final String FIREBASE_PAYLOAD_GO_NEWS = "NEWSLETTER";
    public static final String FIREBASE_PAYLOAD_GO_ALERTS = "ALERTS";

    public static final String FIREBASE_TOPIC_NEWS = "news";
    public static final String FIREBASE_TOPIC_NEWS_LIMA = "news-Lima";
    public static final String FIREBASE_TOPIC_NEWS_AREQUIPA = "news-Arequipa";
    public static final String FIREBASE_TOPIC_NEWS_TRUJILLO = "news-Trujillo";
    public static final String FIREBASE_TOPIC_NEWS_HUANCAYO = "news-Huancayo";

    public static final String INCIDENT_STATUS_PENDIENT = "P";
    public static final String INCIDENT_STATUS_ATENTION = "A";
    public static final String INCIDENT_STATUS_CLOSED = "C";

}
