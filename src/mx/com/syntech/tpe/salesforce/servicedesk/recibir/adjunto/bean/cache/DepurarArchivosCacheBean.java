package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.cache;

import java.io.File;
import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.props.AppPropsBean;
import org.apache.log4j.Category;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class DepurarArchivosCacheBean {

    private static final Category log = Category.getInstance(DepurarArchivosCacheBean.class);

    /**
     * Método para intentar borrar todos los archivos cache cuya longitud sea
     * cero
     */
    public void depurarCache() {

        int total = 0;
        int enuso = 0;
        int eliminado = 0;
        log.debug("Limpiando cache...");
        AppPropsBean.getPropsVO().getBddClassDriver();
        String carpetaCache = AppPropsBean.getPropsVO().getPathTemporalRecibidos();
        log.debug("Carpeta cache: " + carpetaCache);
        File fileCarpetaCache = new File(carpetaCache);
        File[] fileCache = fileCarpetaCache.listFiles();
        if (fileCache != null && fileCache.length > 0) {
            log.debug("Escaneando " + fileCache.length + " archivos para eliminar !!");

            for (int i = 0; i < fileCache.length; i++) {
                total++;
                log.debug("Eliminando " + fileCache[i].getAbsolutePath() + "...");
                if (fileCache[i].length()== 0) {
                    log.debug("Archivo vacio, intentamos eliminarlo...");
                    if (fileCache[i].delete()) {
                        log.debug("Eliminado !!");
                        eliminado++;
                    } else {
                        log.debug("Todavia esta en uso.");
                        enuso++;
                    }
                }else{
                    log.debug("Archivo con información: " + fileCache[i].length() + " no se puede eliminar hasta que se encuentre vacio.");
                }
            }
            log.debug("Resumen: Total=" + total + ", Eliminados:" + eliminado + ", En uso:" + enuso);
        }

    }
    
    public static void main(String...params){
        DepurarArchivosCacheBean bean = new DepurarArchivosCacheBean();
        bean.depurarCache();
    }

}
