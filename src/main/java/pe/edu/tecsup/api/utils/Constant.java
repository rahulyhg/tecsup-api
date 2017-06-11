package pe.edu.tecsup.api.utils;

public class Constant {

    public static final String[] GOOGLEPLUS_ALLOW_DOMAINS = {"tecsup.edu.pe"};

    public static final int SEGURIDAD_USUARIO_ACEPTADA = 0;
    public static final int SEGURIDAD_USUARIO_NO_ENCONTRADO = -1;
    public static final int SEGURIDAD_USUARIO_DESHABILITADO = -2;
    public static final int SEGURIDAD_USUARIO_BLOQUEADO = -3;
    public static final int SEGURIDAD_USUARIO_BLOQUEADO_TEMPORALMENTE = -4;
    public static final int SEGURIDAD_USUARIO_PASSWORD_MAL = -5;
    public static final int SEGURIDAD_USUARIO_PASSWORD_DEBE_CAMBIAR = -6;
    public static final int SEGURIDAD_USUARIO_ERROR_DESCONOCIDO = -7;

    public static final String ROL_ADMINISTRADOR_CANVAS = "CAMPUS - Administrador Canvas";

    public static final int ROL_SEVA_DIRECTOR = 142; // SEVA - Director
    public static final int ROL_SEVA_DOCENTE = 143; // SEVA - Docente
    public static final int ROL_SEVA_ESTUDIANTE = 145; // SEVA - Estudiante
    public static final int ROL_SEVA_ESTUDIANTE_ANTIGUO = 45; // SEVA - Estudiante Antiguo
    public static final int ROL_SEVA_JEFE_DEPARTAMENTO = 146; // SEVA - JefedeDepartamento

    public static final String EMAIL_FROM = "campus@tecsup.edu.pe";
    public static final String EMAIL_ADMINISTRATOR = "ebenites@tecsup.edu.pe";

    public static final String FIREBASE_PAYLOAD_GO = "GO";
    public static final String FIREBASE_PAYLOAD_GO_DEBT = "DEBT";

}
