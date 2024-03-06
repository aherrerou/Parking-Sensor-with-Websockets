import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class servidorWeb
{
        public static void main(String[] args) {	
        String puertoWeb;
        String ipController;
        int puertoController;

        try
        {
            if (args.length < 3) {
                System.out.println("Debe indicar el puerto de escucha del servidor");
                System.out.println("$./Servidor puerto_Web,ip_controller, puerto_controller ");
                System.exit (1);
            }
            puertoWeb = args[0];
            ipController=args[1];
            puertoController=Integer.parseInt(args[2]);
            
            ServerSocket skServidor = new ServerSocket(Integer.parseInt(puertoWeb));
            System.out.println("Escucho el puerto web: " + puertoWeb);
            System.out.println("Ip del controller: " + ipController + " , puerto del controller: " + puertoController);
            /*
             * Mantenemos la comunicacion con el cliente
            */	
            for(;;)
            {
                /*
                * Se espera un cliente que quiera conectarse
                */
                Socket skCliente = skServidor.accept();
                System.out.println("Sirviendo cliente...");

                paginaWeb pagina = new paginaWeb(skCliente,ipController,puertoController);
                pagina.start();
            }
        }
        catch(NumberFormatException | IOException e) {
            System.out.println("Error1: " + e.toString());

        }
    }	
}

class paginaWeb extends Thread
{	

    private Socket skcliente = null; 
    private PrintWriter datos = null; 
    private String ip;
    private int puerto;
    protected String mensajeServidor; //Mensajes entrantes (recibidos) en el servidor
    protected DataOutputStream salidaServidor; //Datos de salida

    public paginaWeb(Socket skcliente,String ip,int puerto)
    {
        this.skcliente = skcliente;
        this.ip=ip;
        this.puerto=puerto;
    }

    @Override
    public void run() {
        String cadena;
        try {

            datos = new PrintWriter(new OutputStreamWriter(skcliente.getOutputStream(),"8859_1"),true) ;
            BufferedReader read = new BufferedReader (new InputStreamReader(skcliente.getInputStream()));

            cadena = read.readLine();
            StringTokenizer valores = new StringTokenizer(cadena);    
            
            if (valores.nextToken().equals("GET"))  // Comprobamos el error de si no es por GET
                mostrarPagina(valores.nextToken());
            else {
                datos.println("Error 405: Metodo no permitido");
            }
        }
        catch (Exception e) {
            datos.println("Error 409: No se ha podido establecer conexion con el controlador");
        }
            datos.close();
        try {
            skcliente.close();
        } catch (IOException ex) {
            Logger.getLogger(paginaWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void mostrarPagina(String valores) throws IOException {
        
        String pagina=valores.substring(valores.indexOf("/")+1,valores.length());
        String resultado;
        if (valores.length()==1) {
            pagina="index.html";
        }
        
        String palabra="controladorSD";
        String linea;
        datos.println("GET HTTP/1.1\n");
        
        if (valores.contains(palabra)) {
            resultado=Cliente(pagina);
            resultado=resultado.substring(2,resultado.length());
            datos.println(resultado);
            
        } else {        
            try
            {
                File fichero = new File(pagina);
                if (!fichero.exists()) { 
			pagina="error404.html";
			fichero = new File(pagina);
                } 
                       BufferedReader paginaHtml = new BufferedReader(new FileReader(fichero));
                        do  // Lee el archivo y lo muestra
                        {
                            linea = paginaHtml.readLine();
                            if (linea != null ){
                                datos.println(linea);
                            }
                        }
                        while (linea != null);



            }
            catch(Exception e) {
               datos.println("Error 404: Recurso no accesible"); 
            }
        }
    }	

    
    
    public String Cliente(String url) throws IOException {  
        Socket skCliente;
        String resultado="";
        try {
            
            skCliente = new Socket(this.ip, this.puerto);
            
            //Datos servidor
            DataOutputStream salidaServidor = new DataOutputStream(skCliente.getOutputStream());

            BufferedReader mensajeLeer = new BufferedReader(new InputStreamReader(skCliente.getInputStream()));

            //La primera iteracion no muestra nada, por eso es necesario un segundo envio
            for (int i = 0; i < 1; i++)
            {
                salidaServidor.writeUTF( url + "\n");  
            }
           
            resultado = mensajeLeer.readLine();
            
            salidaServidor.close();
            mensajeLeer.close();
            skCliente.close();

        }
        catch(NumberFormatException e) {
            System.out.println("Error2: " + e.toString());

        }
        return resultado;
        
    }
}
