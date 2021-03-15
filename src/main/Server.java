/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author gabhs
 */
public class Server {
    private static final String _IP ="192.168.1.69";
    private static final int _PUERTO = 12345;
    private static final int _BACKLOG =50;
    
    public static void main(String args[]) throws UnknownHostException {
        
        InetAddress ip = InetAddress.getByName(_IP);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            System.out.println("Ip del localhost = " + InetAddress.getLocalHost().toString());
            System.out.println("\nEScuchando en : ");
            System.out.println("IP HOST =" + ip.getHostAddress());
            System.out.println("Puerto = " + _PUERTO);
            
        } catch (UnknownHostException uhe) {
            System.err.println("No puede saber la direccion ip local :" +uhe);
        }
        
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(_PUERTO,_BACKLOG,ip);
        } catch (IOException ioe) {
            System.out.println("Error al abrir el socket de servidor: " + ioe);
            System.exit(-1);
        }
        
        double entradaAltura,entradaRadio,PI=3.1416;
        double salidaV,salidaA,salidaAEsfera;
        
        while(true){
            try {
                Socket socketPeticion = serverSocket.accept();
                DataInputStream datosEntradaAltura = new DataInputStream(socketPeticion.getInputStream());
                DataInputStream datosEntradaRadio = new DataInputStream(socketPeticion.getInputStream());
                DataOutputStream datosSalidaV = new DataOutputStream(socketPeticion.getOutputStream());
                DataOutputStream datosSalidaA = new DataOutputStream(socketPeticion.getOutputStream());
                int puertoRemitente = socketPeticion.getPort();
                
                InetAddress ipRemitente = socketPeticion.getInetAddress();
                
                entradaAltura = datosEntradaAltura.readDouble();
                entradaRadio = datosEntradaRadio.readDouble();
     
                salidaV = (PI * (entradaRadio * entradaRadio)) * entradaAltura;
                salidaA= (PI * 2 * entradaRadio) * (entradaAltura+entradaRadio);

                datosSalidaV.writeDouble(salidaV);
                datosSalidaA.writeDouble(salidaA);
                
                datosEntradaAltura.close();
                datosEntradaRadio.close();
                 datosSalidaV.close();
                datosSalidaA.close();
                socketPeticion.close();
                
//                System.out.println(formatter.format(new Date()) + 
//                        "Cliente =" + ipRemitente + ":" + puertoRemitente +
//                         "\tEntrada = " + entrada
//                         + "\tSalida =" + salida
//                        );

            } catch (Exception e) {
                System.err.println("Se ha producido la excepcion " + e);
            }
        }
    }
}
