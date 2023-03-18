package socketstesting;

import java.io.Serializable;

public class ResponseFile implements Serializable{

    public String fileName = "";
    public boolean lastMessage = true;
    public int bytesValidos = 0;
    public byte[] contenidoFichero = new byte[LONGITUD_MAXIMA];
    public final static int LONGITUD_MAXIMA = 100;
    
    
}
