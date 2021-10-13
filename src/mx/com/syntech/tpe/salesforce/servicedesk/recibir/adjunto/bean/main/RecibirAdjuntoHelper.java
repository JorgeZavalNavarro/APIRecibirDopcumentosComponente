package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.props.AppPropsBean;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.utils.SerialClaveBean;
import org.apache.log4j.Category;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class RecibirAdjuntoHelper {
    
    private static Category log = Category.getInstance(RecibirAdjuntoHelper.class);
    
    private static final String FORMAT_FECHA_HORA_ARCHIVO = "yyyyMMdd_HHmmssSSS";

    public static String guardarArchivoBase64(String contenidoBase64, RecibirAdjuntoInputVO inputVO) throws RecibirAdjuntoException {

        // Formular el nombre del archivo
        String nombreArchivo = null;

        SimpleDateFormat format = new SimpleDateFormat(FORMAT_FECHA_HORA_ARCHIVO);
        nombreArchivo
                = AppPropsBean.getPropsVO().getPathTemporalRecibidos() 
                + File.separator + inputVO.getNombreArchivo()
                // + "_" + inputVO.getTicketSalesForce()
                // + "_" + inputVO.getTicketServiceDesk()
                + format.format(new java.util.Date())
                + (new SerialClaveBean()).getSerial(10)
                + ".base64";

        // Validar que no exista un archivo con el mismo nombre
        File fileArchivo = new File(nombreArchivo);
        if (fileArchivo.exists()) {
            String error
                    = "::: No se puede procesar debido a que el proceso "
                    + "de nomenclaruta de archivos coincidió con uno que "
                    + "actualmente se esta trabajando. "
                    + "Es importante volver a intentar" + "\n"
                    + "Nombre: " + nombreArchivo;
            log.error(error);
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));
            
        }

        // Guardar el contenido
        log.info("Guardar el conteniodo Base64 de tamaño: " + contenidoBase64.length());
        log.info("En el archivo.." + nombreArchivo);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileArchivo);
            outputStream.write(contenidoBase64.getBytes());
            outputStream.flush();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Exception: No se puede generar el archivo temporal: " + nombreArchivo);
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
            
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error("Throwable: No se puede generar el archivo temporal: " + nombreArchivo);
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
        } finally {
            
            // de cualquier forma cerramos el archivo
            try {
                outputStream.close();
                
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Exception: No se puede generar el archivo temporal: " + nombreArchivo);
                throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
                
            } catch (Throwable ex) {
                ex.printStackTrace();
                log.error("Throwable: No se puede generar el archivo temporal: " + nombreArchivo);
                throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
            }
        }
        return nombreArchivo;
    }

    public static String guardarArchivoBinario(byte[] binario, RecibirAdjuntoInputVO inputVO) throws RecibirAdjuntoException {

        // Formular el nombre del archivo
        String nombreArchivo = null;

        SimpleDateFormat format = new SimpleDateFormat(FORMAT_FECHA_HORA_ARCHIVO);
        nombreArchivo
                = AppPropsBean.getPropsVO().getPathTemporalRecibidos() 
                + File.separator + inputVO.getNombreArchivo() 
                + format.format(new java.util.Date()) 
                + (new SerialClaveBean()).getSerial(10) + ".cache";
        // Validar que no exista un archivo con el mismo nombre
        File fileArchivo = new File(nombreArchivo);
        if (fileArchivo.exists()) {
            String error
                    = "::: No se puede procesar debido a que el proceso "
                    + "de nomenclaruta de archivos coincidió con uno que "
                    + "actualmente se esta trabajando. "
                    + "Es importante volver a intentar" + "\n"
                    + "Nombre: " + nombreArchivo;
            log.error(error);
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, new Exception(error));
            
        }

        // Guardar el contenido
        log.info("Guardar el conteniodo binario de tamaño: " + binario.length);
        log.info("En el archivo.." + nombreArchivo);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileArchivo);
            outputStream.write(binario);
            outputStream.flush();
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Exception: No se puede generar el archivo temporal: " + nombreArchivo);
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
            
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error("Throwable: No se puede generar el archivo temporal: " + nombreArchivo);
            throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
            
        } finally {
            
            // de cualquier forma cerramos el archivo
            try {
                outputStream.flush();
                outputStream.close();
//                outputStream.getChannel().close();
                Thread.sleep(2000);
                
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Exception: No se puede generar el archivo temporal: " + nombreArchivo);
                throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
                
            } catch (Throwable ex) {
                ex.printStackTrace();
                log.error("Throwable: No se puede generar el archivo temporal: " + nombreArchivo);
                throw new RecibirAdjuntoException(CodeKeys.CODE_960_SERVICE_DESK_ERROR_NC, ex);
            }
        }
        
        return nombreArchivo;

    }
   
}
