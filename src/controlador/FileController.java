package controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileController extends HttpServlet {
    private static final String RUTA_PROPERTIES = System.getenv("RUTA_PROPERTIES");
    private static final String SHELL_GENERA_ARCHIVO = "lecturas-remarcador-mes.sh";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //PrintWriter out = response.getWriter();
        try{
            int idremarcador = Integer.parseInt(request.getParameter("idremarcador"));
            int numremarcador = Integer.parseInt(request.getParameter("numremarcador"));
            int messolo = Integer.parseInt(request.getParameter("messolo"));
            int aniosolo = Integer.parseInt(request.getParameter("aniosolo"));
            String nomsalida = "Lecturas-ID" + numremarcador + "-" + messolo + "-" + aniosolo + ".csv";
            InputStream inStream = new FileInputStream(RUTA_PROPERTIES);
            Properties prop = new Properties();
            prop.load(inStream);
            String rutabatch = prop.getProperty("proc.dir");
            String nomarchivo = getRegistrosMesRemarcadorBatch(idremarcador, aniosolo, messolo, rutabatch);
            File archivo = new File(nomarchivo);
            ServletOutputStream outservlet = response.getOutputStream();
            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setHeader("Content-Length", Integer.toString((int)archivo.length()));
            response.setHeader("Content-Disposition", "attachment;filename=\"" + nomsalida + "\"");
            FileInputStream fis = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int length;
            while((length = fis.read(buffer)) > 0){
                outservlet.write(buffer, 0, length);
            }
            fis.close();
            //Borrar archivo en el origen---------------------------------------
            archivo.delete();
        }catch (Exception ex) {
            System.out.println("No se puede responder el archivo de listado de lecturas del remarcador.");
            System.out.println(ex);
        }
        
    }

    private String getRegistrosMesRemarcadorBatch(int id, int anio, int mes, String dirbatch) {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", dirbatch + "/" + SHELL_GENERA_ARCHIVO + " " + id + " " + anio + " " + mes);
        System.out.println("Comando: " + SHELL_GENERA_ARCHIVO + " " + id + " " + anio + " " + mes);
        try {
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String lineRead;
            while ((lineRead = br.readLine()) != null) {
                return lineRead;
            }
            int rc = p.waitFor();
        } catch (Exception e) {
            System.out.println("Problemas al obtener el listado de registros remarcador Batch.");
            System.out.println(e);
        }
        return "";
    }

}
