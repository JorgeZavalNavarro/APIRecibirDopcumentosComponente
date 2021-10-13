package mx.com.syntech.tpe.salesforce.servicedesk.recibir.adjunto.bean.flags;

/**
 * 
 * @author Jorge Zavala Navarro
 */
public class FlagsException extends Exception{

    public FlagsException(String message) {
        super(message);
    }

    public FlagsException(Throwable cause) {
        super(cause);
    }

}
