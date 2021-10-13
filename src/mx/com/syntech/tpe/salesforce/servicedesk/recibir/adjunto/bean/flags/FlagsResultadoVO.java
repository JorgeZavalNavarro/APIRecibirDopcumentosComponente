package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.flags;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class FlagsResultadoVO{
    
    // Propiedades de la clase
    private Boolean enMantenimiento = null;    // true]: Existe ventana demantenimiento.

    /** METODOS GETTERS Y SETTERS **/
    public Boolean getEnMantenimiento() {
        return enMantenimiento;
    }

    public void setEnMantenimiento(Boolean enMantenimiento) {
        this.enMantenimiento = enMantenimiento;
    }
    
    
    
    
}
