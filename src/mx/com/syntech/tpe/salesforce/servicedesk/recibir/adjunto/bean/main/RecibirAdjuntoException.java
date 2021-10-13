package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.main;

import mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.core.CoreException;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class RecibirAdjuntoException extends CoreException{
    
    public RecibirAdjuntoException(String idCode, Throwable th){
        super(idCode, th);
    }

}
