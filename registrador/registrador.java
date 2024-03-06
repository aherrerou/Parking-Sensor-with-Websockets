import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class registrador extends UnicastRemoteObject
implements RegistroRemoto  {
    
    private Registry registro;
    private InterfazRemoto sonda;

	public registrador(Registry registro) throws RemoteException, IOException {
            super();
            this.registro=registro;
	}


	public void registrarSensor(InterfazRemoto sensor,int id) throws RemoteException
        {
            System.setSecurityManager(new RMISecurityManager());
            String URLRegistro = "Sensor"+id;
            try {
                registro.rebind (URLRegistro, (Remote) sensor);
                //sensor.setVolumen(1);
            }
            catch(Exception ex) {
                System.out.println("Error en: " + ex);
            }
            
	}

}
