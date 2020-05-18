package modelo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    public static int generaRandom(int ini, int fin) {
        return (int) Math.floor(Math.random() * (fin - ini + 1) + ini);
    }

    public static String hashMD5(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Problemas al cifrar el mensaje");
            System.out.println(ex);
            return "";
        }

    }

    public static String armarSelect(ResultSet rs, String primerItemValue, String primerItemText, String colValues, String colTextos) {
        String salida = "<option value='" + primerItemValue + "'>" + primerItemText + "</option>";
        try {
            while (rs.next()) {
                salida += "<option value='" + rs.getString(colValues) + "'>" + rs.getString(colTextos) + "</option>";
            }
        } catch (SQLException ex) {
            System.out.println("No se puede armar el select.");
            System.out.println(ex);
        }
        return salida;
    }

    public static float redondearDecimales(float numero, int cantDecimales) {
        BigDecimal bd = new BigDecimal(Float.toString(numero));
        bd = bd.setScale(cantDecimales, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static BigDecimal redondear(float numero, int cantDecimales) {
        BigDecimal bd = new BigDecimal(Float.toString(numero));
        bd = bd.setScale(cantDecimales, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static String formatRut(String rut) {

        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        format = "-" + rut.substring(rut.length() - 1);
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
    }

    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    public static String formatMiles(String numero) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1, len = builder.length(); i < len; i++) {
            if (i % 4 == 0) {
                builder.insert(len = builder.length() - i, '.');
                len = builder.length();
            }
        }
        return builder.toString();
    }

    public static String armarBody(ResultSet rs, String[] arrCampos) throws SQLException {
        String salida = "<thead><tr>";
        for (int i = 0; i < arrCampos.length; i++) {
            salida += "<th>" + arrCampos[i] + "</th>";
        }
        salida += "</tr></thead>";
        salida += "<tbody>";
        while (rs.next()) {
            salida += "<tr>" + rs.getRef(salida);
            for (int x = 0; x < arrCampos.length; x++) {
                salida += "<td>" + rs.getRef(arrCampos[x]) + "</td>";
            }
            salida += "</tr>";
        }
        salida += "</tbody>";
        return salida;
    }

    public static String getProperty(String attr) {
        String rutaProperties = System.getenv("RUTA_PROPERTIES"); //Habilitar para lectura desde variable de entorno
        if (rutaProperties == null) {
            return "";
        }
        try {
            InputStream entrada = new FileInputStream(rutaProperties);
            Properties prop = new Properties();
            prop.load(entrada);
            return prop.getProperty(attr);
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Ocurrió un problema al obtener un valor del archivo properties.");
            System.out.println(ex);
            return "";
        }
    }

    public static String capitalizarString(String frase) {
        frase = frase.toLowerCase();
        String[] palabras = frase.split("\\s");
        String capitalizada = "";
        for (String palabra : palabras) {
            String first = palabra.substring(0, 1);
            String afterfirst = palabra.substring(1);
            capitalizada += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizada.trim();
    }
    
    public static String invertirFecha(String fecha){
        String[] campos = fecha.split("-");
        return campos[2] + "-" + campos[1] + "-" + campos[0];
    }
}
