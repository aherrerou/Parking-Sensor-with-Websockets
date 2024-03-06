import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable; 
import java.rmi.*;
import java.rmi.server.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sensor con el que trabajaremos
 */
public class Sensor extends UnicastRemoteObject
implements InterfazRemoto, Serializable 
{     
    /**
     * Identificador del sensor
     */
    private String id;
    
    /**
     * Volumen que detecta el sensor
     */
    private int volumen;
    
    /**
     * Ultima vez que se modifico algo del sensor
     */
    private String fecha;
    
    /**
     * Color del led
     */
    private int led;
    
    public Sensor(String id) throws RemoteException, IOException 
    { 
        super();
        this.id=id;
        String directorio= "";
        String texto;
        boolean existe=true;
        File fichero;
        Date fechaActual = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
        directorio="./sensor"+id+".txt";
        fichero = new File(directorio);
        if (!fichero.exists()) {
            this.volumen=30;
            this.fecha=formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
            this.led=4500;
            FileWriter escribir=new FileWriter(fichero,true);      
            escribir.write("Volumen="+this.volumen+"\nUltimaFecha="+this.fecha+"\nLed="+this.led);
            escribir.close();
            existe=false;
        }
        else {
            FileReader lector=new FileReader(directorio);
            BufferedReader contenido=new BufferedReader(lector);
            int i=0;
            while((texto=contenido.readLine())!=null)
            {
                String[] separada = texto.split("=",0); 
                if (i==0)
                    this.volumen=Integer.parseInt(separada[1]);
                else if (i==1)
                    this.fecha=separada[1];
                else if (i==2)
                    this.led=Integer.parseInt(separada[1]);
                i++;
            }
        }
    }

    public int getVolumen() {
        return volumen;
    }

    public String getFecha() {
        return fecha;
    }

    public int getLed() {
        return led;
    }
    
    
    public void setVolumen(int volumen) throws FileNotFoundException, IOException {
        this.volumen = volumen;
        String sled=String.valueOf(this.volumen);
        setFecha();
        escribirTexto(sled,"Volumen=");
     }

    public void setFecha() throws FileNotFoundException, IOException {
        Date fechaActual = new Date();
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        this.fecha=formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
        escribirTexto(this.fecha,"UltimaFecha=");
    }

    public void setLed(int led) throws FileNotFoundException, IOException {
        this.led = led;
        String sled=String.valueOf(this.led);
        setFecha();
        escribirTexto(sled,"Led=");

    }
    
    private void escribirTexto(String valor,String linea) throws FileNotFoundException, IOException {
        String directorio="./sensor"+this.id+".txt";      
        String directorioaux="./s"+this.id+".txt";   
        String texto;
        File fichero = new File(directorio);
        File ficheroaux = new File(directorioaux);
        FileReader lector=new FileReader(directorio);
        BufferedReader contenido=new BufferedReader(lector);
        FileWriter escribir=new FileWriter(ficheroaux,true);   

        
        try {
            if(fichero.exists()){
                BufferedReader Flee= new BufferedReader(new FileReader(directorio));
                String Slinea;
                while((texto=contenido.readLine())!=null) { 
                    if (texto.contains(linea)) {
                        escribir.write(linea+valor+"\n");
                    }else{
                        /*Escribo la linea antigua*/
                        escribir.write(texto+"\n");
                    }             
                }
                fichero.delete();
                ficheroaux.renameTo(fichero);
                /*Cierro el flujo de lectura*/
                escribir.close();
            }else{
                System.out.println("Fichero No Existe");
            }
        } catch (Exception ex) {
            /*Captura un posible error y le imprime en pantalla*/ 
             System.out.println(ex.getMessage());
        }
    }
}
