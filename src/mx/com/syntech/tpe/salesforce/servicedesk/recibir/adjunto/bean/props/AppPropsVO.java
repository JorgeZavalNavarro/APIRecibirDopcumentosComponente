package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.props;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class AppPropsVO {

    // Propiedades de la clase
    private String principalAmbiente = null;
    private String urlServicedeskWs = null;
    private String bddClassDriver = null;
    private String bddUrlFabricante = null;
    private String bddConexionServidor = null;
    private String bddConexionPuerto = null;
    private String bddConexionBasedatos = null;
    private String bddConexionUsuario = null;
    private String bddConexionPassword = null;
    private String queryTimeoutSecs = null;
    private String wssdTimeoutConect = null;
    private String wssdTimeoutRead = null;
    private String pathConfigLogs = null;
    private String pathTemporalRecibidos = null;
    private String formatoArchivoTemporal = null;
    private String tammaxArchivoBytes = null;
    private String comandoPdmCacheRefreshAttachment = null;
    private String adjuntoMarcaRecibidoSalesforce = null;

    public String getUrlServicedeskWs() {
        return urlServicedeskWs;
    }

    public void setUrlServicedeskWs(String urlServicedeskWs) {
        this.urlServicedeskWs = urlServicedeskWs;
    }

    public String getBddClassDriver() {
        return bddClassDriver;
    }

    public void setBddClassDriver(String bddClassDriver) {
        this.bddClassDriver = bddClassDriver;
    }

    public String getBddUrlFabricante() {
        return bddUrlFabricante;
    }

    public void setBddUrlFabricante(String bddUrlFabricante) {
        this.bddUrlFabricante = bddUrlFabricante;
    }

    public String getBddConexionServidor() {
        return bddConexionServidor;
    }

    public void setBddConexionServidor(String bddConexionServidor) {
        this.bddConexionServidor = bddConexionServidor;
    }

    public String getBddConexionPuerto() {
        return bddConexionPuerto;
    }

    public void setBddConexionPuerto(String bddConexionPuerto) {
        this.bddConexionPuerto = bddConexionPuerto;
    }

    public String getBddConexionBasedatos() {
        return bddConexionBasedatos;
    }

    public void setBddConexionBasedatos(String bddConexionBasedatos) {
        this.bddConexionBasedatos = bddConexionBasedatos;
    }

    public String getBddConexionUsuario() {
        return bddConexionUsuario;
    }

    public void setBddConexionUsuario(String bddConexionUsuario) {
        this.bddConexionUsuario = bddConexionUsuario;
    }

    public String getBddConexionPassword() {
        return bddConexionPassword;
    }

    public void setBddConexionPassword(String bddConexionPassword) {
        this.bddConexionPassword = bddConexionPassword;
    }

    public String getQueryTimeoutSecs() {
        return queryTimeoutSecs;
    }

    public void setQueryTimeoutSecs(String queryTimeoutSecs) {
        this.queryTimeoutSecs = queryTimeoutSecs;
    }

    public String getWssdTimeoutConect() {
        return wssdTimeoutConect;
    }

    public void setWssdTimeoutConect(String wssdTimeoutConect) {
        this.wssdTimeoutConect = wssdTimeoutConect;
    }

    public String getWssdTimeoutRead() {
        return wssdTimeoutRead;
    }

    public void setWssdTimeoutRead(String wssdTimeoutRead) {
        this.wssdTimeoutRead = wssdTimeoutRead;
    }

    public String getPathConfigLogs() {
        return pathConfigLogs;
    }

    public void setPathConfigLogs(String pathConfigLogs) {
        this.pathConfigLogs = pathConfigLogs;
    }

    public String getPrincipalAmbiente() {
        return principalAmbiente;
    }

    public void setPrincipalAmbiente(String principalAmbiente) {
        this.principalAmbiente = principalAmbiente;
    }

    public String getPathTemporalRecibidos() {
        return pathTemporalRecibidos;
    }

    public void setPathTemporalRecibidos(String pathTemporalRecibidos) {
        this.pathTemporalRecibidos = pathTemporalRecibidos;
    }

    public String getFormatoArchivoTemporal() {
        return formatoArchivoTemporal;
    }

    public void setFormatoArchivoTemporal(String formatoArchivoTemporal) {
        this.formatoArchivoTemporal = formatoArchivoTemporal;
    }

    public String getTammaxArchivoBytes() {
        return tammaxArchivoBytes;
    }

    public void setTammaxArchivoBytes(String tammaxArchivoBytes) {
        this.tammaxArchivoBytes = tammaxArchivoBytes;
    }

    public String getComandoPdmCacheRefreshAttachment() {
        return comandoPdmCacheRefreshAttachment;
    }

    public void setComandoPdmCacheRefreshAttachment(String comandoPdmCacheRefreshAttachment) {
        this.comandoPdmCacheRefreshAttachment = comandoPdmCacheRefreshAttachment;
    }

    public String getAdjuntoMarcaRecibidoSalesforce() {
        return adjuntoMarcaRecibidoSalesforce;
    }

    public void setAdjuntoMarcaRecibidoSalesforce(String adjuntoMarcaRecibidoSalesforce) {
        this.adjuntoMarcaRecibidoSalesforce = adjuntoMarcaRecibidoSalesforce;
    }

    @Override
    public String toString() {
        return "AppPropsVO{" + "principalAmbiente=" + principalAmbiente + ", urlServicedeskWs=" + urlServicedeskWs + ", bddClassDriver=" + bddClassDriver + ", bddUrlFabricante=" + bddUrlFabricante + ", bddConexionServidor=" + bddConexionServidor + ", bddConexionPuerto=" + bddConexionPuerto + ", bddConexionBasedatos=" + bddConexionBasedatos + ", bddConexionUsuario=" + bddConexionUsuario + ", bddConexionPassword=" + bddConexionPassword + ", queryTimeoutSecs=" + queryTimeoutSecs + ", wssdTimeoutConect=" + wssdTimeoutConect + ", wssdTimeoutRead=" + wssdTimeoutRead + ", pathConfigLogs=" + pathConfigLogs + ", pathTemporalRecibidos=" + pathTemporalRecibidos + ", formatoArchivoTemporal=" + formatoArchivoTemporal + ", tammaxArchivoBytes=" + tammaxArchivoBytes + ", comandoPdmCacheRefreshAttachment=" + comandoPdmCacheRefreshAttachment + ", adjuntoMarcaRecibidoSalesforce=" + adjuntoMarcaRecibidoSalesforce + '}';
    }
    


}
