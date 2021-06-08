package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Conexion;
import modelo.lectura.Registro;
import modelo.lectura.RegistroPotencia;

public class FileController extends HttpServlet {

    private static final String RUTA_PROPERTIES = System.getenv("RUTA_PROPERTIES");
    private static final String SHELL_GENERA_ARCHIVO = "lecturas-remarcador-mes.sh";
    private static String SEP;

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
            SEP = prop.getProperty("csv.separator");
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
        String sep = SEP;
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
        LinkedList<Registro> registros = modelo.LecturaController.getRegistrosMesRemarcadorNumremarcador(id, anio, mes);
        LinkedList<RegistroPotencia> registrosPotencia = modelo.LecturaController.getRegistrosPotenciaMesRemarcadorNumremarcador(id, anio, mes);
        try {
            File salida = new File(nomarchivo);
            FileWriter fr = new FileWriter(salida, true);
            DecimalFormat formato = new DecimalFormat("#.##");

            String cabecera = "TIMESTAMP" + sep + "FECHA" + sep + "HORA" + sep + "ID REMARCADOR" + sep + "ENERGIA CONSUMIDA PROYECTADA" + sep + "POTENCIA ACTIVA" + sep + "LECTURA REAL" + sep + "LECTURA MANUAL" + System.getProperty("line.separator");
            fr.write(cabecera);
            float proyectada = 0f;
            BigDecimal potencia = new BigDecimal(0.0);;
            for (Registro reg : registros) {
                //System.out.println("PROYECTADA: [" + proyectada + " + " + reg.delta + "] = " + (proyectada + reg.delta));
                proyectada = proyectada + reg.delta;
               
                String fechahoracortaregistro = reg.timestamp.substring(0, 16);
                //System.out.println(fechahoracortaregistro);
                for (RegistroPotencia regpo : registrosPotencia) {
                    potencia = new BigDecimal(0.0);
                    String fechahoracortaregistropotencia = regpo.timestamp.substring(0, 16);
                    //if(fechahoracortaregistro.equals(fechahoracortaregistropotencia)){
                    if (reg.timestamp.equals(regpo.timestamp)) {
                        //System.out.println("coincide");
                        potencia = regpo.potencia;
                        break;
                    }
                }
                
                String fecha = reg.timestamp.split(" ")[0];
                String hora = reg.timestamp.split(" ")[1];
                String fechaLocal = fecha.split("-")[2] + "-" + fecha.split("-")[1] + "-" + fecha.split("-")[0];

                String registro
                        = reg.timestamp
                        + sep
                        + fechaLocal
                        + sep
                        + hora
                        + sep
                        + reg.numremarcador
                        + sep
                        + proyectada
                        + sep
                        + formato.format(potencia).replace(",", ".")
                        + sep
                        + reg.lectura
                        + sep
                        + reg.lecturaManual
                        + System.getProperty("line.separator");
                //System.out.print(registro);
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
