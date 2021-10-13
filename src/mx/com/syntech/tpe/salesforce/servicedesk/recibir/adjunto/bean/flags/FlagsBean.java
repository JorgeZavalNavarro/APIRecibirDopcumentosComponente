package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.flags;

/**
 * Clase con la definición de las banderas para activar o desactivar procedimientos
 * @author Jorge Zavala Navarro
 */
public class FlagsBean {
    
    public static FlagsResultadoVO enVentanaMantenimiento(String...params){
        FlagsResultadoVO retorno = null;
        retorno = new FlagsResultadoVO();
        retorno.setEnMantenimiento(Boolean.TRUE);
        return retorno;
    }

}
