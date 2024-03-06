import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Registro {

	public static void main(String[] args) throws Exception {
            
            if (args.length < 3) {
                System.out.println("Debe indicar el puerto de escucha del servidor");
                System.out.println("$./Servidor ip_maquina_local ip_registry y identificador_sensor"); //probar a ver si funciona en diferentes máquinas con esto
                System.exit (1);
            }
            	final String ipl = args[0];
		final String ip = args[1];
System.setProperty("java.rmi.server.hostname",ipl);
                final Registry registro = LocateRegistry.getRegistry(ip, Registry.REGISTRY_PORT);
                String id = args[2];
                int identificador= Integer.parseInt(id);
                RegistroRemoto registrador=null;
		String URLRegistro = "rmi://"+ip+":1099/Registrador";
		Sensor sensor = new Sensor(id);
                System.setSecurityManager(new RMISecurityManager());    
                try 
                {   
					System.out.println("Se queda pensando..");             
                    registrador = (RegistroRemoto) registro.lookup(URLRegistro);
                    System.out.println("Termina de pensar");
                    registrador.registrarSensor(sensor,identificador);
                }            
                catch (Exception ex)            
                {                  
                    System.out.println("Pues no va \n" + ex);            
                }  
	}
}

