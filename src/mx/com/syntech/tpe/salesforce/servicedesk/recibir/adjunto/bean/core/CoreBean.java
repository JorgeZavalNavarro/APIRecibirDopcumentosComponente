package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.core;

import java.text.SimpleDateFormat;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class CoreBean {
    
    private static final String FORMATO_FECHA_RESPONSE_TPE = "yyyy-MM-dd HH:mm:ss";
    
    public static String fechaActual(){
        String retorno = null;
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA_RESPONSE_TPE);
        java.util.Date fechaActual = new java.util.Date();
        retorno = format.format(fechaActual);
        return retorno;
    }

}
