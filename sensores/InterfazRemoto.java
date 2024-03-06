import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazRemoto extends Remote {
    public int getVolumen() throws java.rmi.RemoteException;
    public String getFecha() throws java.rmi.RemoteException;
    public int getLed() throws java.rmi.RemoteException;
    public void setVolumen(int volumen) throws java.rmi.RemoteException,FileNotFoundException,IOException;
    public void setFecha() throws java.rmi.RemoteException,FileNotFoundException,IOException;
    public void setLed(int led) throws java.rmi.RemoteException,FileNotFoundException,IOException;
    
}
