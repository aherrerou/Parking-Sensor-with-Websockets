import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import java.io.*;

public class Registro {         
    public static void main (String args[])     
    {            
        String URLRegistro;
        try           
        {   
            if (args.length < 1) {
                System.out.println("La ip de donde está esta maquina");
                System.out.println("$./Servidor ip_registry");
                System.exit (1);
            }
            Registry registro = LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
            System.setSecurityManager(new RMISecurityManager());
System.setProperty("java.rmi.server.hostname",args[0]); // para que funcione en diferentes máquinas virtuales o locales
            String ip = args[0];
            registrador objetoRemoto = new registrador(registro);   
            URLRegistro = "rmi://"+ip+":1099/Registrador";
            registro.rebind (URLRegistro, (Remote) objetoRemoto);       
            System.out.println("Servidor de objeto preparado.");
        }            
        catch (Exception ex)            
        {                  
            System.out.println(ex);            
        }     
    }
}



/*public class Registro {         
    public static void main (String args[])     
    {            
        String nombreRegistro;
        try {
            if (args.length < 2) {
                System.out.println("Debe indicar el puerto de escucha del servidor");
                System.out.println("$./Servidor ip_registry y identificador_sensor");
                System.exit (1);
            }
	    String ip=args[0];
            int puerto=1099;
            //int id = Integer.parseInt(args[1]);
            try           
            {   
                System.setSecurityManager(new RMISecurityManager());
                Sensor sensor = new Sensor(1);                  
                nombreRegistro = "/Sensor";
                Naming.rebind (nombreRegistro, sensor);            
                System.out.println("Servidor de objeto preparado."); 
            }            
            catch (Exception ex)
            {                  
                System.out.println("No se ha podido conectar correctamente: "+ex);            
            }    
        }
        catch(NumberFormatException e) {
            System.out.println("Error:  " + e.toString());

        }
    }
}*/
