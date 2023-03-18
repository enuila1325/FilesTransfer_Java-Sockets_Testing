package socketstesting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class SocketsTesting_Server {

    public static void main(String[] args) {
        try {

            ServerSocket socketServidor = new ServerSocket(5000);
            JOptionPane.showMessageDialog(null, "Server Conectado");
            Socket cliente = socketServidor.accept();
            cliente.setSoLinger(true, 30);
            ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
            Object mensaje = ois.readObject();

            if (mensaje instanceof RequestFile) {
                sendFile(
                        ((RequestFile) mensaje).fileRequested,
                        new ObjectOutputStream(cliente.getOutputStream()));
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketsTesting_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SocketsTesting_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendFile(String fichero, ObjectOutputStream oos) {
        FileInputStream fis = null;
        try {
            boolean enviadoUltimo = false;
            // Se abre el fichero.
            fis = new FileInputStream(fichero);
            // Se instancia y rellena un mensaje de envio de fichero
            ResponseFile mensaje = new ResponseFile();
            mensaje.fileName = fichero;
            // Se leen los primeros bytes del fichero en un campo del mensaje
            int leidos = fis.read(mensaje.contenidoFichero);

            while (leidos > -1) {
                // Se rellena el número de bytes leidos
                mensaje.bytesValidos = leidos;

                // Si no se han leido el máximo de bytes, es porque el fichero
                // se ha acabado y este es el último mensaje
                if (leidos < ResponseFile.LONGITUD_MAXIMA) {
                    // Se marca que este es el último mensaje
                    mensaje.lastMessage = true;
                    enviadoUltimo = true;
                } else {
                    mensaje.lastMessage = false;
                }

                // Se envía por el socket  
                oos.writeObject(mensaje);

                // Si es el último mensaje, salimos del bucle.
                if (mensaje.lastMessage) {
                    break;
                }

                // Se crea un nuevo mensaje
                mensaje = new ResponseFile();
                mensaje.fileName = fichero;

                // y se leen sus bytes.
                leidos = fis.read(mensaje.contenidoFichero);
            }

            // En caso de que el fichero tenga justo un múltiplo de bytes de MensajeTomaFichero.LONGITUD_MAXIMA,
            // no se habrá enviado el mensaje marcado como último. Lo hacemos ahora.
            if (enviadoUltimo == false) {
                mensaje.lastMessage = true;
                mensaje.bytesValidos = 0;
                oos.writeObject(mensaje);
            }
            // Se cierra el ObjectOutputStream
            oos.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SocketsTesting_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketsTesting_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
