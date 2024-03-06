import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class controller
{
    protected String mensajeServidor; //Mensajes entrantes (recibidos) en el servidor
    protected DataOutputStream salidaServidor; //Flujo de datos de salida
    
    
    public static void main(String[] args) throws IOException
    {  
        String puerto;
        String ip;
        
        try {
            if (args.length < 2) {
                System.out.println("Debe indicar el puerto de escucha del servidor");
                System.out.println("$./Servidor puerto_servidor ; ip de la maquina de los sensores");
                System.exit (1);
            }
            puerto = args[0];
            ip=args[1];
            
            ServerSocket skServidor = new ServerSocket(Integer.parseInt(puerto));
            System.out.println("Escucho el puerto " + puerto);
            
            for(;;)
            {
                /*
                * Se espera un cliente que quiera conectarse
                */
                Socket skCliente = skServidor.accept();
                System.out.println("Sirviendo cliente...");

                ConexionServidor server = new ConexionServidor(skCliente,ip);
                server.start(); 
            }
        }
        catch(NumberFormatException e) {
            System.out.println("Error: " + e.toString());

        }
    }
}


class ConexionServidor extends Thread //Se hereda de conexión para hacer uso de los sockets y demás
{
    private Socket skCliente;
    private String ip;

    
    public ConexionServidor(Socket skCliente,String ip) {
        this.skCliente=skCliente;
        this.ip=ip;
    } 

    public void run()
    {
        String mensaje;
        String peticion;
        String sonda;
        String id;
        int identificador;
        boolean funciona=false;
        clienteRMI cliente;
        String resultado;
        String index;
            
        try
        {
            // Para enviar mensajes al cliente
            DataOutputStream entradaMensaje = new DataOutputStream(skCliente.getOutputStream());
            //Se le confirma que ha llegado
            
            BufferedReader mensajeLeer = new BufferedReader(new InputStreamReader(skCliente.getInputStream()));
            mensaje=mensajeLeer.readLine();

            try {
                index=mensaje.substring(mensaje.indexOf("/")+1,mensaje.length());
                if (index.equals("index.html")){
                    Registry registry = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
                    resultado="";
                    String sensores[]=registry.list();
                    for (int k=0;k<sensores.length;k++)
                    {  
                        if (sensores[k].contains("Sensor"))
                            resultado=resultado+sensores[k]+"  ";
                    }
                    
                }
                else {
                    peticion=mensaje.substring(mensaje.indexOf("/")+1,mensaje.indexOf("?"));
                    sonda=mensaje.substring(mensaje.indexOf("?")+1,mensaje.length());
                    id=sonda.substring(sonda.indexOf("="),sonda.length());

                    id=id.substring(1,id.length());  // Sirve para que la conversion a int funcione

                    identificador=Integer.parseInt(id);

                    cliente = new clienteRMI(ip,peticion,identificador);

                    resultado=cliente.datosSensor();   
                }
                entradaMensaje.writeUTF(resultado);
                mensajeLeer.close();
                entradaMensaje.close();
                skCliente.close();
            }
            catch(Exception e) {
                resultado="Variable no valida";            
                entradaMensaje.writeUTF(resultado);
                mensajeLeer.close();
                entradaMensaje.close();
                skCliente.close();
            }
                
            
        }
        catch(Exception e) {
           System.out.println("Error Conectando"); 
        }
        try {
            skCliente.close();
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class clienteRMI {    

    private String ip;
    private String peticion;
    private int id;
    
    public clienteRMI(String host, String peticion,int id) {
        this.ip=host;
        this.peticion=peticion;
        this.id=id; 
    }

    public String datosSensor()
    {
        
        String resultado="";
        InterfazRemoto sensor = null;   
        String valor;
        String cambiar;
        int luz;
        
        String servidor = "rmi://"+this.ip+":1099/Sensor"+this.id;
        
        try
        {
            System.setSecurityManager(new RMISecurityManager());            	
            sensor = (InterfazRemoto) Naming.lookup(servidor);
        }
        catch(Exception ex)
        {
            resultado="Sensor no valido";
        }
 
        try
        {
            resultado="Variable no valida";
            
            if (peticion.contains("=")){
                cambiar=peticion.substring(0,peticion.indexOf("="));
                valor=peticion.substring(peticion.indexOf("=")+1,peticion.length());
                luz=Integer.parseInt(valor);
                
                if (cambiar.equals("setluz"))
                {
                    sensor.setLed(luz);
                    resultado="Variable cambiada";
                }
                else if(cambiar.equals("setvolumen")) {
                    sensor.setVolumen(luz);
                    resultado="variable cambiada";
                }
                
            }
            else {

                if (peticion.equals("volumen")) {
                    resultado="El sensor " + this.id +" tiene un volumen de " +String.valueOf(sensor.getVolumen());
                }
                else if (peticion.equals("fecha")) {
                    java.util.Date fecha = new Date();
                    resultado=String.valueOf(fecha);
                }
                else if (peticion.equals("ultimafecha")) {
                    resultado="El sensor " + this.id +" no es modificado desde " + sensor.getFecha();
                }
                else if (peticion.equals("luz")) {
                    resultado="El sensor " + this.id +" tiene un led de " + String.valueOf(sensor.getLed()) + " color";
                }
            }
            
        }
        catch(Exception exc)
        {
            System.out.println("Error al realizar la operacion "+exc);
        }
        
        sensor = null;
        return resultado;
    }
    

}
