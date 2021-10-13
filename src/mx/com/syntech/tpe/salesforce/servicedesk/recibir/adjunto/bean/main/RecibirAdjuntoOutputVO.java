package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.core.CoreVO;

/**
 * 
 * @author Jorge Zavala Navarro
 */
@XmlRootElement
public class RecibirAdjuntoOutputVO extends CoreVO{
    
    // propiedades de la clase
    private String codigoRespuesta = null;
    private String respuestaBoolean = null;
    private String descripcionRespuesta = null;
    private String mensajeServicio = null;
    private String folioDocumento = null;
    private String fechaSolicitud = null;
    private String fechaRespuesta = null;

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getRespuestaBoolean() {
        return respuestaBoolean;
    }

    public void setRespuestaBoolean(String respuestaBoolean) {
        this.respuestaBoolean = respuestaBoolean;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public String getMensajeServicio() {
        return mensajeServicio;
    }

    public void setMensajeServicio(String mensajeServicio) {
        this.mensajeServicio = mensajeServicio;
    }

    public String getFolioDocumento() {
        return folioDocumento;
    }

    public void setFolioDocumento(String folioDocumento) {
        this.folioDocumento = folioDocumento;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String json() {
        String retorno = null;
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("{");
        sbuilder.append("\"respuestaBoolean\"").append(" : \"").append(respuestaBoolean).append("\",");
        sbuilder.append("\"codigoRespuesta\"").append(" : \"").append(codigoRespuesta).append("\",");
        sbuilder.append("\"descripcionRespuesta\"").append(" : \"").append(descripcionRespuesta).append("\",");
        sbuilder.append("\"mensajeServicio\"").append(" : \"").append(mensajeServicio).append("\",");
        sbuilder.append("\"fechaSolicitud\"").append(" : \"").append(fechaSolicitud).append("\",");
        sbuilder.append("\"fechaRespuesta\"").append(" : \"").append(fechaRespuesta).append("\",");
        sbuilder.append("\"folioDocumento\"").append(" : \"").append(folioDocumento).append("\"");
        sbuilder.append("}");
        retorno = sbuilder.toString();
        return retorno;
    }

    public static String json(List<RecibirAdjuntoInputVO> list) {
        String retorno = null;
        StringBuilder sbuilder = new StringBuilder();
        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    sbuilder.append("[").append(list.get(i).json());
                } else {
                    sbuilder.append(",").append(list.get(i).json());
                }
            }

            sbuilder.append("]");
        } else {
            sbuilder.append("sin informacion");
        }

        retorno = sbuilder.toString();
        return retorno;
    }    
    

}
