/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author Jorge Zavala Navarro
 */
public class LeerArchivoTOCadenaBase64 {

    public static String leerArchivoTOBase64(String nombreArchivo) {
        Base64 base64 = new Base64();

        /*----------------ARCHIVOS------------------*/
        File file = new File(nombreArchivo);
        byte[] fileArray = new byte[(int) file.length()];
        InputStream inputStream;

        String encodedFile = "";
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(fileArray);
            encodedFile = base64.encodeToString(fileArray);
        } catch (Exception e) {
            // Manejar Error
            e.printStackTrace();
        }
       // System.out.println(encodedFile);

        return encodedFile;
    }
    
    public static void main(String...parm){
        // String archivo = "F:\\Virtuales\\Windows Server 2012 - Service Desk BDD\\vmware-0.log";
        // Archivo de 259751 bytes si se adjuntó correctamente
        // String archivo = "F:\\Software\\DesarrolloSistemas\\Diseño\\PowerDesigner\\CrackPoweDesigner.rar";
        // Archivo de 601418 bytes si se adjuntó correctamente
        // String archivo = "F:\\CA-Technologies\\Anteriores\\CA\\virtualwin2008x64\\vmware.log";
        // Archivo de 881739 Bytes si se adjunto correctamente
        // String archivo = "C:\\Users\\dell\\Documents\\Geometría-y-Trigonometría-Pearson.pdf";
        // Archivo de 25000000 Bytes, NO SE ADJUNTO:  Error: no se puede realizar la operación; límite de política superado
        String archivo = "F:\\CA-Technologies\\Anteriores\\CA\\docs9.1.1\\APM_API_Reference_EN.pdf";
        // Archivo de 1019182 si se adjunto correctamente
        // String archivo = "F:\\CA-Technologies\\Anteriores\\CA\\docs9.1.1\\APM_EP_Agent_Implem_EN.pdf";
        // Archivo de 1110557 bytes, NO SE ADJUNTO:  Error: no se puede realizar la operación; límite de política superado
        // String archivo = "G:\\FFOutput\\Cielo de Tambores-Grupo Niche.mp3";
        callWS(archivo);
        
                
    }
    
    public static String callWS(String archivoFisico){
        String retorno = null;
        try {
            // String archivoFisico = "F:\\CA-Technologies\\Anteriores\\CA\\virtualwin2008x64\\vmware.log";
            String urlWS = "http://192.168.112.90:5050/APIRecibirDocumentos/rest/salesforce/recibir/adjunto";
            String nombreArchivo = "RESPALDO APM_API_Reference_EN.pdf";
            String ticketSalesForce = "0200124318-5003f000009FmqHAAS";
            String ticketServiceDesk = "1048172";
            String usuario = "servicedesk";
            String password = "DeskService01";
            String archivoBase64 = leerArchivoTOBase64(archivoFisico);
            
            String params
                    = "{\"nombreArchivo\": \"" + nombreArchivo + "\","
                    + "\"ticketSalesForce\": \"" + ticketSalesForce + "\","
                    + "\"ticketServiceDesk\": \"" + ticketServiceDesk + "\","
                    + "\"usuario\": \"" + usuario + "\","
                    + "\"password\": \"" + password + "\","
                    + "\"archivoBase64\": \"" + archivoBase64 + "\"}";
            String[] details = {};

            URL line_api_url = new URL(urlWS);
            String payload = params;

            HttpURLConnection linec = (HttpURLConnection) line_api_url.openConnection();
            // linec.setDoInput(true);
            linec.setDoOutput(true);
            linec.setRequestMethod("POST");
            linec.setRequestProperty("Content-Type", "application/json");
            // linec.setRequestProperty("Authorization", "Bearer 1djCb/mXV+KtryMxr6i1bXw");
            // System.out.println(payload);
            // OutputStreamWriter writer = new OutputStreamWriter(linec.getOutputStream(), "UTF-8");
            OutputStream os = linec.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(linec.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
            linec.disconnect();
            
            retorno = "La información se envió al soa-infra satisfactoriamente";

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
            e.printStackTrace();
            
        }
        return retorno;
    }
    

}
