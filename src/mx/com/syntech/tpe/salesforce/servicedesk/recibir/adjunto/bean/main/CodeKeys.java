package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import java.util.Hashtable;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class CodeKeys {
    
    // Definición de los códigos de retorno
    public static final String CODE_000_OK = "000";
    
    public static final String CODE_100_DATO_REQUERIDO_FALTANTE = "010";
    public static final String CODE_110_SIN_CREDENCIALES = "110";
    public static final String CODE_120_CREDENCIALES_INCORRECTAS = "120";
    public static final String CODE_130_NUMERO_TICKET_NO_EXISTE = "130";
    public static final String CODE_140_TICKETS_SFSD_SIN_RELACION = "140";
    public static final String CODE_150_TICKETS_SD_SIN_TICKET_SF = "150";
    public static final String CODE_160_ARCHIVO_EXCEDE_LONG_MAXIMA = "160";

    
    public static final String CODE_210_SERVICE_DESK_UNREACHABLE = "210";
    public static final String CODE_220_DATABASE_UNREACHABLE = "220";
    public static final String CODE_230_SERVICIO_UNREACHABLE = "230";
    
    public static final String CODE_310_SERVICE_DESK_WSERROR = "310";
    public static final String CODE_320_DATABASE_SQLERROR = "320";
    public static final String CODE_330_SERVICE_DESK_WSTIMEOUT = "330";
    public static final String CODE_340_DATABASE_SQLTIMEOUT = "340";
    public static final String CODE_350_DATABASE_QUERYERROR = "350";
    
    public static final String CODE_410_SERVICEDESK_MANTENIMIENTO = "410";
    public static final String CODE_420_SALESFORCE_MANTENIMIENTO = "420";
    
    public static final String CODE_950_SERVICE_DESK_WSERROR_CREDS = "950";
    public static final String CODE_960_SERVICE_DESK_ERROR_NC = "960";
    public static final String CODE_970_DATABASE_ERROR_NC = "970";
    public static final String CODE_980_ERROR = "980";

    
}
