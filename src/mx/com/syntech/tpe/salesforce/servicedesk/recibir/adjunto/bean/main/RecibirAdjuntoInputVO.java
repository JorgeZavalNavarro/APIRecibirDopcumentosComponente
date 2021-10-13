package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author Jorge Zavala Navarro
 */
@XmlRootElement
public class RecibirAdjuntoInputVO {
    
    // Parámetros de entrada
    private String usuario = null;
    private String password = null;
    private String tipoArchivo = null;
    private String ticketServiceDesk = null;   // Numero de ticket, como se muestra en el portal
    private String ticketSalesForce = null;
    private String nombreArchivo = null;
    private String archivoBase64 = null;
    private String comentario = null;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTicketServiceDesk() {
        return ticketServiceDesk;
    }

    public void setTicketServiceDesk(String ticketServiceDesk) {
        this.ticketServiceDesk = ticketServiceDesk;
    }

    public String getTicketSalesForce() {
        return ticketSalesForce;
    }

    public void setTicketSalesForce(String ticketSalesForce) {
        this.ticketSalesForce = ticketSalesForce;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getArchivoBase64() {
        return archivoBase64;
    }

    public void setArchivoBase64(String archivoBase64) {
        this.archivoBase64 = archivoBase64;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    
    
    public String json() {
        String retorno = null;
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("{");
        sbuilder.append("\"usuario\"").append(" : \"").append(usuario).append("\",");
        sbuilder.append("\"password\"").append(" : \"").append(password).append("\",");
        sbuilder.append("\"comentario\"").append(" : \"").append(comentario).append("\",");
        sbuilder.append("\"tipoArchivo\"").append(" : \"").append(tipoArchivo).append("\",");
        sbuilder.append("\"ticketServiceDesk\"").append(" : \"").append(ticketServiceDesk).append("\",");
        sbuilder.append("\"ticketSalesForce\"").append(" : \"").append(ticketSalesForce).append("\",");
        sbuilder.append("\"nombreArchivo\"").append(" : \"").append(nombreArchivo).append("\",");
        sbuilder.append("\"archivoBase64\"").append(" : \"").append(archivoBase64).append("\"");
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
