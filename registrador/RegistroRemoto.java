import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegistroRemoto extends Remote{
	public void registrarSensor(InterfazRemoto sensor,int id) throws RemoteException;
}
