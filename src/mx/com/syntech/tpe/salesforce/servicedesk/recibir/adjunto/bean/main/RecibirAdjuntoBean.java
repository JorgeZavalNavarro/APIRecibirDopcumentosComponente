package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceLocator;
import com.ca.www.UnicenterServicePlus.ServiceDesk.USD_WebServiceSoap;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.rpc.ServiceException;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.conectbdd.ConnectorBDDConsultasBean;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.conectbdd.ConnectorBDDConsultasException;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.core.CoreBean;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.flags.FlagsBean;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.props.AppPropsBean;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.utils.SerialClaveBean;
import org.apache.axis.client.Call;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.log4j.Category;
import org.springframework.util.FileSystemUtils;

/**
 * DeskService01
 *
 * Actualizacion : 03/06/2021 Autor : Jorge Zavala Navarro Descripción : ** 1 Se
 * limita el tamaño del archivo que se recibe a 5M (5242880) el cual queda
 * configurable desde el archivo de propoerties con la propiedad de
 * {ambiente}.tammax.archivo.bytes=5242880 ** 2 Se implementa la validación de
 * la recepción del documento, si el documento ecede esta longitud máxima
 * permitida no lo adjunte y agregue un mensaje en el log activity del ticket
 * correspondiente
 *
 * @author Jorge Zavala Navarro
 */
public class RecibirAdjuntoBean extends CoreBean {

    private static Category log = Category.getInstance(RecibirAdjuntoBean.class);
    private static final String uNoLock = "  WITH (NOLOCK) ";
    private static java.net.URL url = null;

    public RecibirAdjuntoBean() throws MalformedURLException {
        if (url == null) {
            url = new URL(AppPropsBean.getPropsVO().getUrlServicedeskWs());
        }
    }

    public RecibirAdjuntoOutputVO adjuntar(RecibirAdjuntoInputVO inputVO) throws RecibirAdjuntoException {
        RecibirAdjuntoOutputVO retorno = new RecibirAdjuntoOutputVO();
        retorno.setFechaSolicitud(fechaActual());
        String archivoUsar = null;
        System.out.println(AppPropsBean.getPropsVO().getBddClassDriver());
        
        // validamos si hay ventana de mantenimiento abierta
        if(FlagsBean.enVentanaMantenimiento().getEnMantenimiento()){
            // El sistema se encuentra en mantenimiento por lo tanto no procede
            String errorDescripcion = "Se detecta una ventana de mantenimiento. Por el momento no se puede ejecutar esta funcionalidad debido a la ventana de mantenimiento que se encuentra arriba. Por favor intente mas tarde.";
            String errorCodigo = CodeKeys.CODE_410_SERVICEDESK_MANTENIMIENTO;
            throw new RecibirAdjuntoException(errorCodigo, new Exception(errorDescripcion));
        }

        if (inputVO != null) {
            int sid = 0;
            FileDataSource fds = null;
            DataHandler dhandler = null;
            Connection conn = null;
            String persidTicket = null;
            String folioSalesfor = null;

            try {

                // Inicializamos el elemento de retorno
                retorno = new RecibirAdjuntoOutputVO();
                conn = ConnectorBDDConsultasBean.getConectionServiceDesk();

                log.info("::: Logeando el usuario. Obtenemos el SID");
                sid = login(inputVO.getUsuario(), inputVO.getPassword());
                log.info("::: SID del usuario: " + sid);

                /**
                 * VERIFICAMOS LA INFORMACIÓN DEL TICKET DE SALESFORCE *
                 */
                log.info(" ::: Validando la información del ticket de Salesforce...");
                if (inputVO.getTicketSalesForce() == null || inputVO.getTicketSalesForce().isEmpty()) {
                    log.error("::: No se está rercibiendo el número de ticket de Salesforce");
                    throw new RecibirAdjuntoException(CodeKeys.CODE_100_DATO_REQUERIDO_FALTANTE, new Exception("No se está recibiendo el número de ticket de Salesforce"));
                }

                /**
                 * VERIFICAMOS LA INFORMACIÓN DEL TICKET DESDE SERVICEDESK*
                 */
                if (inputVO.getTicketServiceDesk() == null || inputVO.getTicketServiceDesk().isEmpty()) {
                    String error = "   ::: No se está recibiendo el número de ticket de Service Desk";
                    log.error(error);
                    throw new RecibirAdjuntoException(CodeKeys.CODE_100_DATO_REQUERIDO_FALTANTE, new Exception(error));

                } else {

                    // Buscar el persid del ticket que del cual se recive el refnum
                    log.info("   ::: Validar la existencia del ticket ref_num = " + inputVO.getTicketServiceDesk());
                    String sqlBuscarNumeroTicket
                            = "SELECT TOP 1 REF_NUM AS NUMERO_TICKET,  \n"
                            + "       PERSID AS PERSISTENT_ID,   \n"
                            // + "	  CONVERT(NVARCHAR(30) ,EXTERNAL_SYSTEM_TICKET) AS REF_SALES_FORCE  \n"
                            // Se cambia el nombre del campo del tickeyt externo 22/06/2021
                            // por el campo de zfolio_dbw_sf el cual se va a registrar el numero
                            // de ticket correspondiente a salesforce
                            + "	  zfolio_dbw_sf AS REF_SALES_FORCE  \n"
                            
                            + "  FROM CALL_REQ  " + uNoLock + "  \n"
                            + " WHERE REF_NUM = ? AND    \n"
                            // + "       EXTERNAL_SYSTEM_TICKET IS NOT NULL AND \n"
                            // + "	  CONVERT(NVARCHAR(30) ,EXTERNAL_SYSTEM_TICKET) AS REF_SALES_FORCE  \n"
                            // Se cambia el nombre del campo del tickeyt externo 22/06/2021
                            // por el campo de zfolio_dbw_sf el cual se va a registrar el numero
                            // de ticket correspondiente a salesforce
                            + "       zfolio_dbw_sf IS NOT NULL AND \n"
                            + "	      zfolio_dbw_sf <> ''  \n"
                            + " ORDER BY OPEN_DATE DESC";
                    log.debug("Validar con el query");
                    log.debug(sqlBuscarNumeroTicket);
                    PreparedStatement psBuscarNumeroTicket = conn.prepareCall(sqlBuscarNumeroTicket);
                    psBuscarNumeroTicket.setString(1, inputVO.getTicketServiceDesk());
                    ResultSet rsBuscarNumeroTicket = psBuscarNumeroTicket.executeQuery();

                    // Validamos la información 
                    if (rsBuscarNumeroTicket.next()) {

                        // Validamos que tenga asignado el numero de folio que se recibe de salesfor
                        folioSalesfor = rsBuscarNumeroTicket.getString("REF_SALES_FORCE");
                        persidTicket = rsBuscarNumeroTicket.getString("PERSISTENT_ID");
                        log.info("   ::: Persid ticket" + persidTicket);
                        if (folioSalesfor == null || folioSalesfor.isEmpty()) {
                            String error = "   ::: El ticket ref_num = " + inputVO.getTicketServiceDesk() + " no cuenta con ninguna referencia externa de Salesfor";
                            log.error(error);
                            throw new RecibirAdjuntoException(CodeKeys.CODE_150_TICKETS_SD_SIN_TICKET_SF, new Exception(error));

                        } else {
                            // Validamos que las referencias exiatn y sean iguales
                            if (!folioSalesfor.trim().equals(inputVO.getTicketSalesForce().trim())) {
                                String error = "   ::: No coinciden los numeros de referencia externos para el ticket: " + inputVO.getTicketServiceDesk();
                                log.error(error);
                                throw new RecibirAdjuntoException(CodeKeys.CODE_150_TICKETS_SD_SIN_TICKET_SF, new Exception(error));
                            }
                        }
                    } else {
                        // No existe el ticket
                        String error = "No se encontro el ticket con el ref_num = " + inputVO.getTicketServiceDesk();
                        log.error(error);
                        throw new RecibirAdjuntoException(CodeKeys.CODE_130_NUMERO_TICKET_NO_EXISTE, new Exception(error));
                    }
                }

                log.info(" ::: Validando el contenido del archivo Base64...");
                if (inputVO.getArchivoBase64() == null || inputVO.getArchivoBase64().isEmpty()) {
                    log.error("::: No se está recibiendo el contenido del archivo");
                    throw new RecibirAdjuntoException(CodeKeys.CODE_100_DATO_REQUERIDO_FALTANTE, new Exception("No se está recibiendo el contenido del archivo"));
                } else {
                    log.info(" ::: Validando el tamaño del stream base64 (limite max:" + AppPropsBean.getPropsVO().getTammaxArchivoBytes() + ")");
                    int longMax = 0;
                    try {
                        longMax = Integer.valueOf(AppPropsBean.getPropsVO().getTammaxArchivoBytes()).intValue();
                    } catch (NumberFormatException ex) {
                        longMax = RecibirAdjuntoKeys.VAL_TAMMAX_ARCHIVO_BYTES;
                        log.warn("No se tiene bien establecido el limitante del string den archivo base64");
                        log.warn(ex.getMessage());
                        log.warn("Se utilizará el valor por default 5M (" + longMax + " Bytes)");
                    }

                    if (inputVO.getArchivoBase64().length() > longMax) {
                        // El archivo excede el maximo tamaño permitido
                        String error = "   ::: El contenido base64 del archivo que se está intentando agregar "
                                + inputVO.getNombreArchivo()
                                + " excede el limite máximo permitido: " + longMax
                                + ". No se agregó el archivo.";
                        log.error(error);
                        log.error("   ::: Agregar al log de actividades del ticket...");
                        log.error("   ::: Obtener el creator de " + inputVO.getUsuario() + "...");
                        String creator = this.getHandleForUserid(sid, inputVO.getUsuario());
                        log.error("   ::: Creator obtenido " + creator);

                        // Agregamos en el arcivity log
                        log.error("   ::: Creando el log activity....");
                        createActivityLog(sid, creator, persidTicket, error, "LOG", 0, Boolean.FALSE);

                        // Lanzar exception
                        throw new RecibirAdjuntoException(CodeKeys.CODE_160_ARCHIVO_EXCEDE_LONG_MAXIMA, new Exception(error));
                    }

                }

                /**
                 * Comentar la validación de tipo de archivo requerida
                 * log.info("::: Validando el tipo de archivo..."); if
                 * (inputVO.getTipoArchivo() == null ||
                 * inputVO.getTipoArchivo().isEmpty()) { String error = "::: No
                 * se está recibiendo el tipo de archivo"; log.error(error);
                 * throw new
                 * RecibirAdjuntoException(CodeKeys.CODE_100_DATO_REQUERIDO_FALTANTE,
                 * new Exception(error)); } *
                 */
                log.info("::: Validar numero de ticket de servicedesk y de salesforce...");
                if ((inputVO.getTicketServiceDesk() == null || inputVO.getTicketServiceDesk().isEmpty())
                        && (inputVO.getTicketSalesForce() == null || inputVO.getTicketSalesForce().isEmpty())) {
                    String error = "::: No se está recibiendo el numero de ticket de salesforce ni el numero de ticket de servicedesk";
                    log.error(error);
                    throw new RecibirAdjuntoException(CodeKeys.CODE_100_DATO_REQUERIDO_FALTANTE, new Exception(error));
                }

                log.info("::: Decodificar el contenido del archivo Base64 ==> Original");
                byte[] bytesArchivo = Base64.getDecoder().decode(inputVO.getArchivoBase64());
                log.info("::: Contenido de bytes: " + bytesArchivo.length);

                /**
                 * Guardar el base64 ebn un archivo *
                 */
                archivoUsar = RecibirAdjuntoHelper.guardarArchivoBinario(bytesArchivo, inputVO);
                // Hasta aqui el archivo no queda bloqueado

                fds = new FileDataSource(archivoUsar);                // Hasta aqui el archivo no queda bloqueado                
                dhandler = new DataHandler(fds);     // Hasta aqui el archivo no queda bloqueado

                log.info("::: Obtenemos el handle del usuario");
                String usuarioHandle = getHandleForUserid(sid, inputVO.getUsuario());
                log.info("::: Handle: " + usuarioHandle);

                String ServiceDeskDocumentRepository = "doc_rep:1002";
                log.info("::: Definir el repositorio temporal:" + ServiceDeskDocumentRepository);

                log.info("::: Crear el adjunto al ticket correspondiente...");
                mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService(url);
                mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
                USD_WebServiceLocator ws = new USD_WebServiceLocator();
                USD_WebServiceSoap usd = ws.getUSD_WebServiceSoap(url);
                // ((org.apache.axis.client.Stub) usd)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_DIME);
                ((org.apache.axis.client.Stub) usd).addAttachment(dhandler);   // Hasta aqui el archivo no está bloqueado
                String attmntHandle = usd.createAttachment(sid, ServiceDeskDocumentRepository, persidTicket, inputVO.getComentario(), inputVO.getNombreArchivo());  // AQUI QUEDA BLOQUEADO EL ARCHIVO
                String attmntPersid = attmntHandle;
                Thread.sleep(2000);
                if (attmntHandle.length() > 0) {
                    // Preparamos los elementos de retorno
                    log.info("   ::: Successfully uploaded the file to the Request, attachment handle: " + attmntHandle + "\n");
                    // quitamos del handle la parte del attmnt: del attmnt:1557461
                    
                    attmntHandle = attmntHandle.substring(7);
                    log.info("   ::: Handle a regresar: " + attmntHandle);
                    retorno.setFolioDocumento(attmntHandle);

                } else {
                    throw new RecibirAdjuntoException(CodeKeys.CODE_310_SERVICE_DESK_WSERROR, new Exception("No se puedo adjuntar el documento. Por favor consulte con el personal autorizado para consultar los registros del sistema."));
                }

                // ((org.apache.axis.client.Stub) usd).clearAttachments();

                log.info("   ::: El adjunto se creó satisfactoriamente !!!");
                log.info("   ::: Marcar el adjunto... ");
                String sqlMarcarAdjunto
                        = "UPDATE attmnt " + "\n"
                        + "   SET description = CONCAT(description, ' ', ? )" + "\n"
                        + " WHERE persid = ?";
                PreparedStatement psMarcarAdjunto = conn.prepareCall(sqlMarcarAdjunto);
                log.info("Ejecutar el query: " + sqlMarcarAdjunto);
                log.info("Marca a aplicar:" + AppPropsBean.getPropsVO().getAdjuntoMarcaRecibidoSalesforce());
                psMarcarAdjunto.setString(1, AppPropsBean.getPropsVO().getAdjuntoMarcaRecibidoSalesforce());
                log.info("ID del adjunto:" + attmntPersid);
                psMarcarAdjunto.setString(2, attmntPersid);
                psMarcarAdjunto.executeUpdate();
                
                // Commiteamos la transacción
                conn.commit();
                
                // Ejecutamos la instrucción para actualizar los adjuntos en el portal
                log.info("Ejecutamos el pdm");
                this.ejecutarPDM();
                log.info("Proceso completado");

            } catch (ConnectorBDDConsultasException ex) {
                log.error("Error de conexión a la base de datos");
                log.error(ex.getMensaje(), ex);
                throw new RecibirAdjuntoException(ex.getIdError(), ex);

            } catch (SQLException ex) {
                log.error("Error de consulta a la base de datos");
                log.error(ex.getMessage(), ex);
                throw new RecibirAdjuntoException(CodeKeys.CODE_350_DATABASE_QUERYERROR, ex);
            } catch (MalformedURLException ex) {
                log.error("Error de Conexión de Web service");
                log.error(ex.getMessage(), ex);
                throw new RecibirAdjuntoException(CodeKeys.CODE_310_SERVICE_DESK_WSERROR, ex);
            } catch (ServiceException ex) {
                log.error("Error de Servicio");
                log.error(ex.getMessage(), ex);
                throw new RecibirAdjuntoException(CodeKeys.CODE_230_SERVICIO_UNREACHABLE, ex);
            } catch (RemoteException ex) {
                log.error("Error de Servicio");
                log.error(ex.getMessage(), ex);
                throw new RecibirAdjuntoException(CodeKeys.CODE_230_SERVICIO_UNREACHABLE, ex);
            } catch (Exception ex) {
                log.error("Error");
                log.error(ex.getMessage(), ex);
                throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
            } catch (Throwable ex) {
                log.error("Error Throwable");
                log.error(ex.getMessage(), ex);
                throw new RecibirAdjuntoException(CodeKeys.CODE_980_ERROR, ex);

            } finally {

                try {

                    log.info("   ::: Cerrar la conexión a la base de datos");
                    conn.close();

                    if (sid != 0) {
                        log.info("   ::: Cerrar la sesión del usuario.");
                        logout(sid);
                    }
                    /**
                     * if(!fds.getFile().delete()){ log.warn("Archivo no se
                     * borro por fds"); }
                     *
                     */

                    if (fds != null) {

                        log.info("   ::: Liberar fds");
                        if (fds.getOutputStream() != null) {
                            fds.getOutputStream().flush();
                            fds.getOutputStream().close();
                            Thread.sleep(2000);
                        }

                        fds.getInputStream().close();
                    }

                    if (dhandler != null) {

                        log.info("   ::: Liberar y cerrar handlers");
                        dhandler.getInputStream().close();
                        dhandler.getOutputStream().flush();
                        dhandler.getOutputStream().close();
                        Thread.sleep(2000);

                    }

                    if (archivoUsar != null && !archivoUsar.isEmpty()) {
                        log.warn("   ::: Eliminando archivo temporal...");
                        int cont = 0;
                        int max = 5;
                        fds.getFile().delete();
                        Thread.sleep(2000);
                        while (cont < max && ((new File(archivoUsar)).exists())) {
                            cont++;
                            log.info("   ::: Archivo: " + archivoUsar);
                            File fileUsar = new File(archivoUsar);
                            if (!fileUsar.delete()) {
                                log.info("   ::: Archivo " + fileUsar.getAbsoluteFile() + "no se pudo borrar");
                            }

                            if (fileUsar.exists()) {
                                log.info("   ::: Archivo " + archivoUsar + " aun en uso... reintentando " + cont + "/" + max);
                                Thread.sleep(1000);
                            } else {
                                log.info("   ::: Eliminado !!");
                            }
                        }

                        // Renombrar el archivo en caso de que todavia exista
                        File fileUsar = new File(archivoUsar);
                        fileUsar.delete();
                        if (fileUsar.exists()) {

                            String archivoNuevo = fileUsar.getParentFile() + "\\" + archivoCache();
                            //log.info("Renombrando a " + archivoNuevo);
                            if (!fileUsar.renameTo(new File(archivoNuevo))) {
                                //  log.info("   ::: No se pudo renombrar el archivo a: ");

                                // Forzamos el borrado del archivo
                                try {
                                    FileDeleteStrategy.FORCE.delete(fds.getFile());
                                } catch (Exception ex) {
                                    log.error(ex.getMessage());
                                } catch (Throwable th) {
                                    log.error(th.getMessage());
                                }
                            }
                        }
                    }

                } catch (Exception ex) {
                    log.error("   ::: Error en el proceso !!");
                    log.error("   ::: " + ex.getMessage());
                    ex.printStackTrace();
                    throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);

                } catch (Throwable ex) {
                    log.error("   ::: Error en el proceso !!");
                    log.error("   ::: " + ex.getMessage());
                    ex.printStackTrace();
                    throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
                }
            }
        } else {
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception("No se está recibiendo la información para procesar"));
        }
        retorno.setFechaRespuesta(fechaActual());
        return retorno;
    }

    private int login(java.lang.String username, java.lang.String password) throws MalformedURLException {
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService(url);
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.login(username, password);
    }

    private void logout(int sid) throws MalformedURLException {
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService(url);
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        port.logout(sid);
    }

    private String getHandleForUserid(int sid, java.lang.String userID) throws MalformedURLException {
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService(url);
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.getHandleForUserid(sid, userID);
    }

    private String createActivityLog(int sid, java.lang.String creator, java.lang.String objectHandle, java.lang.String description, java.lang.String logType, int timeSpent, boolean internal) {
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService service = new mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebService(url);
        mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.wssd.client.USDWebServiceSoap port = service.getUSDWebServiceSoap();
        return port.createActivityLog(sid, creator, objectHandle, description, logType, timeSpent, internal);
    }

    private String archivoCache() {
        String retorno = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        retorno = "AAA" + format.format(new java.util.Date())
                + "89030843" + (new SerialClaveBean()).getSerial(11)
                + ".$$$cache";

        return retorno;
    }

    /**
     * Ejecutar el programa para actualizar los registros de los adjuntos de SDM
     */
    public void ejecutarPDM() {

        // Obtener el nombre del comando a ejecutar
        String comandoEjecutar = null;
        log.info("   ::: Ejecución del comando para actualizar la información de los adjuntos...");

        try {

            comandoEjecutar = AppPropsBean.getPropsVO().getComandoPdmCacheRefreshAttachment();
            // comandoEjecutar = "cmd /c dir";
            log.info("   ::: Comando a ejecutar: " + comandoEjecutar);

            // Lanzar programa (se usa "cmd /c dir" para lanzar un comando del sistema operativo)
            log.info("   ::: Ejecutando...");
            Process p = Runtime.getRuntime().exec(comandoEjecutar);

            /**
             * Obtenemos la salida del comando *
             */
            log.info("   ::: Recibieno respuesta...");
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Se lee la primera linea
            String aux = br.readLine();
            // log.info("     -->" + aux);

            // Mientras se haya leido alguna linea
            while (aux != null) {
                // Se escribe la linea en pantalla
                System.out.println(aux);
                log.info("     -->" + aux);

                // y se lee la siguiente.
                aux = br.readLine();
            }
            log.info("   ::: Ejecución terminada");

        } catch (Exception ex) {
            // Excepciones si hay algún problema al arrancar el ejecutable o al leer su salida.*/
            log.error("No se pudo ejecutar el programa: " + comandoEjecutar);
            log.error(ex.getLocalizedMessage());
            log.error(ex.getMessage());
            log.error("Trasa del error....", ex);
        }
    }

    public static void main(String[] params) {
        try {
            RecibirAdjuntoBean bean = new RecibirAdjuntoBean();
            bean.ejecutarPDM();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
