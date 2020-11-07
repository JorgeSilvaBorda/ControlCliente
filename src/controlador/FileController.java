package controlador;

import etl.FilaNormal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Conexion;
import modelo.Util;

public class FileController extends HttpServlet {

    private static final String RUTA_PROPERTIES = System.getenv("RUTA_PROPERTIES");
    private static final String SHELL_GENERA_ARCHIVO = "lecturas-remarcador-mes.sh";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //PrintWriter out = response.getWriter();
        try {
            int idremarcador = Integer.parseInt(request.getParameter("idremarcador"));
            int numremarcador = Integer.parseInt(request.getParameter("numremarcador"));
            int messolo = Integer.parseInt(request.getParameter("messolo"));
            int aniosolo = Integer.parseInt(request.getParameter("aniosolo"));
            String nomsalida = "Lecturas-ID" + numremarcador + "-" + messolo + "-" + aniosolo + ".csv";
            InputStream inStream = new FileInputStream(RUTA_PROPERTIES);
            Properties prop = new Properties();
            prop.load(inStream);
            String rutabatch = prop.getProperty("proc.dir");
            //String nomarchivo = getRegistrosMesRemarcadorBatch(idremarcador, aniosolo, messolo, rutabatch);
            String nomarchivo = getRegistrosMesRemarcadorBatch(numremarcador, aniosolo, messolo, rutabatch);
            File archivo = new File(nomarchivo);
            ServletOutputStream outservlet = response.getOutputStream();
            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            response.setHeader("Content-Length", Integer.toString((int) archivo.length()));
            response.setHeader("Content-Disposition", "attachment;filename=\"" + nomsalida + "\"");
            FileInputStream fis = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                outservlet.write(buffer, 0, length);
            }
            fis.close();
            //Borrar archivo en el origen---------------------------------------
            archivo.delete();
        } catch (Exception ex) {
            System.out.println("No se puede responder el archivo de listado de lecturas del remarcador.");
            System.out.println(ex);
            ex.printStackTrace();
        }

    }

    private String getRegistrosMesRemarcadorBatch(int id, int anio, int mes, String dirbatch) {
        //ProcessBuilder pb = new ProcessBuilder("bash", "-c", dirbatch + "/" + SHELL_GENERA_ARCHIVO + " " + id + " " + anio + " " + mes);
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        
        String nomarchivo = dirbatch + "/" + id + "_" + anio + "_" + mes + "_" + day + "_" + hour + "_" + minute + "_" + second + ".csv";
        
        String fechaini = "";
        String fechafin = "";
        Conexion c = new Conexion();
        String query = "CALL SP_GET_FECHAS_INIFIN_MES(" + mes + ", " + anio + ")";
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        System.out.println(query);
        try {
            while (rs.next()) {
                fechaini = rs.getString("FECHAINI");
                fechafin = rs.getString("FECHAFIN");
            }
            System.out.println("Fecha ini: " + fechaini);
            System.out.println("Fecha fin: " + fechafin);
        } catch (SQLException ex) {
            System.out.println("No se puede obtener la fecha de inicio y fin del mes.");
            System.out.println(ex);
        }
        c.cerrar();
        FilaNormal[] filas = etl.ETL.getDatasetRemarcador(id, fechaini, fechafin);
        try {
            File salida = new File(nomarchivo);
            FileWriter fr = new FileWriter(salida, true);
            DecimalFormat formato = new DecimalFormat("#.##");

            String cabecera = "TIMESTAMP;FECHA;HORA;ID REMARCADOR;ENERGIA CONSUMIDA PROYECTADA;POTENCIA ACTIVA;LECTURA REAL;LECTURA MANUAL" + System.getProperty("line.separator");
            fr.write(cabecera);
            for (FilaNormal fila : filas) {
                String registro = fila.fechahora + ";" + Util.invertirFecha(fila.fecha) + ";" + fila.hora + ";" + fila.idremarcador + ";" + (int) fila.lecturaproyectada + ";" + formato.format(fila.potencia).replace(",", ".") + ";" + (int) fila.lecturareal + ";" + fila.lecturamanual + System.getProperty("line.separator");
                fr.write(registro);
            }
            fr.close();
            return nomarchivo;
        } catch (Exception ex) {
            System.out.println("No se puede generar el archivo de lecturas.");
            System.out.println(ex);
            ex.printStackTrace();
        }

        return "";
    }

}
