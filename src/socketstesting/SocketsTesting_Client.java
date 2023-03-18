package socketstesting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SocketsTesting_Client {

    public static void main(String[] args) {

        while (true) {
            Scanner leer = new Scanner(System.in);
            System.out.println("1. Descagar 2. Subir\n ->");
            int opc = leer.nextInt();
            if (opc == 1) {
                try {
                    ObjectOutputStream oos = null;
                    Socket sc = new Socket("localhost", 5000);
                    oos = new ObjectOutputStream(sc.getOutputStream());
                    RequestFile mensaje = new RequestFile();
                    JOptionPane.showMessageDialog(null, "Seleccione el archivo que desea descargar");
                    JFileChooser jfc = new JFileChooser("C:\\Users\\user1\\Documents\\UNITEC\\SISTEMAS\\CONCURRENCIA\\testing\\");
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(SocketsTesting_Client.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                try {

                    ObjectOutputStream oos = null;
                    Socket sc = new Socket("localhost", 5000);
                    oos = new ObjectOutputStream(sc.getOutputStream());
                    RequestFile mensaje = new RequestFile();
                    JOptionPane.showMessageDialog(null, "Seleccione el archivo que desea subir");
                    JFileChooser jfc = new JFileChooser();
                    int userSelection = jfc.showOpenDialog(null);
                    String fichero = "";
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        fichero = jfc.getSelectedFile().getPath();
                        /*C:\Users\\user1\Desktop\Prueba.txt */
                    }
                    mensaje.fileRequested = fichero;
                    oos.writeObject(mensaje);
                    //_copia para no arruinar y perder el original ya que estamos probando en local
                    String aux = mensaje.fileRequested.substring(22);
                    FileOutputStream fos = new FileOutputStream("C:\\Users\\user1\\Documents\\UNITEC\\SISTEMAS\\CONCURRENCIA\\testing" + aux);
                    ObjectInputStream ois = new ObjectInputStream(sc.getInputStream());

                    ResponseFile mensajeRecibido = null;
                    do {
                        // Se lee el mensaje en una variabla auxiliar
                        Object mensajeAux = ois.readObject();

                        // Si es del tipo esperado, se trata
                        if (mensajeAux instanceof ResponseFile) {
                            mensajeRecibido = (ResponseFile) mensajeAux;
                            // Se escribe en pantalla y en el fichero
                            System.out.print(new String(
                                    mensajeRecibido.contenidoFichero, 0,
                                    mensajeRecibido.bytesValidos));
                            fos.write(mensajeRecibido.contenidoFichero, 0,
                                    mensajeRecibido.bytesValidos);
                        } else {
                            // Si no es del tipo esperado, se marca error y se termina
                            // el bucle
                            System.err.println("Mensaje no esperado "
                                    + mensajeAux.getClass().getName());
                            break;
                        }
                    } while (!mensajeRecibido.lastMessage);

                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(SocketsTesting_Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

}
