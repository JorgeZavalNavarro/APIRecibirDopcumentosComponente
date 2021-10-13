package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class RecibirAdjuntoBeanMain {

    public static void main(String... params) {
        try {
            String rutaArchivo = "F:\\CA-Technologies\\Anteriores\\Wily Introspective - Java Agent Guide.pdf";
            String nombreArchivo = "Wily Introspective - Java Agent Guide.pdf";
            
            RecibirAdjuntoInputVO inputVO = new RecibirAdjuntoInputVO();
            inputVO.setArchivoBase64(leerArchivoBase64(rutaArchivo));
            inputVO.setComentario("Prueba de archivo para adjuntar al ticket correspondiente");
            inputVO.setNombreArchivo(nombreArchivo);
            inputVO.setPassword("DeskService01");
            inputVO.setTicketSalesForce("0200124318-5003f000009FmqHAAS");
            inputVO.setTicketServiceDesk("1048172");
            inputVO.setTipoArchivo("rar");
            inputVO.setUsuario("servicedesk");
            RecibirAdjuntoBean bean = new RecibirAdjuntoBean();
            bean.adjuntar(inputVO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String leerArchivoBase64(String rutaArchivoBase64) throws RecibirAdjuntoException {
        String retorno = null;
        if (rutaArchivoBase64 != null && !rutaArchivoBase64.isEmpty()) {
            File file64 = new File(rutaArchivoBase64);
            if (!file64.exists() || !file64.canRead()) {
                throw new RecibirAdjuntoException("", new Exception("El archivo : " + rutaArchivoBase64 + " no existe o no se puede leer. Intente con otro archivo."));
            }

            // Intentamos leer el archivo en un arreglo de bytes
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file64);
                byte[] data = new byte[Integer.valueOf("" + file64.length())];
                inputStream.read(data);
                
                // Convertimos a base 64 el arreglo de los bytes
                retorno = Base64.getEncoder().encodeToString(data);
                
                
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RecibirAdjuntoException("", ex);

            } catch (Throwable ex) {
                ex.printStackTrace();
                throw new RecibirAdjuntoException("", ex);

            } finally {
                try {
                    inputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new RecibirAdjuntoException("", ex);

                } catch (Throwable ex) {
                    ex.printStackTrace();
                    throw new RecibirAdjuntoException("", ex);

                }
            }

        } else {
            throw new RecibirAdjuntoException("", new Exception("No se está recibiendo archvo para leer en base 64"));
        }
        return retorno;
    }

}
