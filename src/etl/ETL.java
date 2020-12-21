package etl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import modelo.Conexion;

/**
 * Clase creada para obtener dataset completo de los remarcadores y procesarlos
 * fuera de SQL
 *
 * @author Jorge Silva Borda
 */
public class ETL {

    /**
     * Obtiene el listado de ID de remarcadores y su tabla de origen
     *
     * @return {@code String[][]}. En donde: [0] = ORIGEN. [1] = ID REMARCADOR.
     */
    private static String[][] getOrigenesRemarcador() {
        String query = "CALL SP_GET_ORIGEN_REMARCADORES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        System.out.println(query);
        try {
            LinkedList<String[]> filas = new LinkedList();
            while (rs.next()) {
                String[] fila = new String[2];
                fila[0] = rs.getString("ORIGEN");
                fila[1] = Integer.toString(rs.getInt("EQUIPO_ID"));
                filas.add(fila);
            }
            String[][] salida = new String[filas.size()][2];
            for (int x = 0; x < filas.size(); x++) {
                salida[x] = filas.get(x);
            }
            c.cerrar();
            return salida;
        } catch (SQLException ex) {
            System.out.println("No se puede obtener el listado de orígenes de los remarcadores.");
            System.out.println(ex);
            c.cerrar();
        }
        c.cerrar();
        return null;
    }

    /**
     * Construye la consulta a la base de datos para obtener los campos de la
     * tabla que corresponda al tipo de remarcador. Esta función incorpora el
     * relleno de las lecturas vacías en la tabla origen del remarcador
     * seleccionado. Devuelve un arreglo con todas las filas del rango de fechas
     * ya procesado y con continuidad ajustada.
     *
     * @param numremarcador {@code int}. Corresponde al ID REMARCADOR.
     * @param fechaDesde {@code String}. Corresponde a la fecha de inicio de la
     * búsqueda.
     * @param fechaHasta {@code String}. Corresponde a la fecha de térmimo de la
     * búsqueda.
     * @return {@code Array} {@link etl.FilaNormal} con los campos deseados y
     * transformados.
     */
    public static FilaNormal[] getDatasetRemarcador(int numremarcador, String fechaDesde, String fechaHasta) {
        String[][] origenes = getOrigenesRemarcador();
        String tabla = "";
        for (String[] fila : origenes) {
            if (Integer.parseInt(fila[1]) == numremarcador) {
                tabla = fila[0];
            }
        }
        String query = "";
        int campos = 0;
        switch (tabla) {
            case "circutorcvmC10":
                query = "SELECT CONVERT(A.TIMESTAMP, CHAR) TIMESTAMP, A.EQUIPO_ID, TRIM(A.ITEM49) ITEM49, TRIM(A.ITEM50) ITEM50, TRIM(A.ITEM95) ITEM95, TRIM(A.ITEM96) ITEM96, CASE WHEN B.NUMREMARCADOR IS NULL THEN 'NO' ELSE 'SI' END AS ESMANUAL, CASE WHEN B.NUMREMARCADOR IS NULL THEN 0 ELSE B.LECTURA END AS LECTURAMANUAL FROM " + tabla + " A LEFT JOIN LECTURAMANUAL B ON A.EQUIPO_ID = B.NUMREMARCADOR AND A.FECHA = B.FECHA AND A.HORA = B.HORA WHERE A.EQUIPO_ID = " + numremarcador + " AND A.FECHA >= '" + fechaDesde + "' AND A.FECHA <= '" + fechaHasta + "' ORDER BY A.TIMESTAMP ASC";
                campos = 8;
                break;
            case "schneiderPM710":
                query = "SELECT CONVERT(A.TIMESTAMP, CHAR) TIMESTAMP, A.EQUIPO_ID, TRIM(A.ITEM7) ITEM7, TRIM(A.ITEM108) ITEM108, TRIM(A.ITEM1) ITEM1, TRIM(A.ITEM2) ITEM2, TRIM(A.ITEM109) ITEM109, CASE WHEN B.NUMREMARCADOR IS NULL THEN 'NO' ELSE 'SI' END AS ESMANUAL, CASE WHEN B.NUMREMARCADOR IS NULL THEN 0 ELSE B.LECTURA END AS LECTURAMANUAL  FROM " + tabla + " A LEFT JOIN LECTURAMANUAL B ON A.EQUIPO_ID = B.NUMREMARCADOR AND A.FECHA = B.FECHA AND A.HORA = B.HORA WHERE A.EQUIPO_ID = " + numremarcador + " AND A.FECHA >= '" + fechaDesde + "' AND A.FECHA <= '" + fechaHasta + "' ORDER BY A.TIMESTAMP ASC";
                campos = 9;
                break;
            case "schneiderPM5300":
                query = "SELECT CONVERT(A.TIMESTAMP, CHAR) TIMESTAMP, A.EQUIPO_ID, TRIM(A.ITEM1) ITEM1, TRIM(A.ITEM43) ITEM43, CASE WHEN B.NUMREMARCADOR IS NULL THEN 'NO' ELSE 'SI' END AS ESMANUAL, CASE WHEN B.NUMREMARCADOR IS NULL THEN 0 ELSE B.LECTURA END AS LECTURAMANUAL  FROM " + tabla + " A LEFT JOIN LECTURAMANUAL B ON A.EQUIPO_ID = B.NUMREMARCADOR AND A.FECHA = B.FECHA AND A.HORA = B.HORA WHERE A.EQUIPO_ID = " + numremarcador + " AND A.FECHA >= '" + fechaDesde + "' AND A.FECHA <= '" + fechaHasta + "' ORDER BY A.TIMESTAMP ASC";
                campos = 6;
                break;
        }
        LinkedList<String[]> filas = new LinkedList();
        int cont = 0;
        Conexion c = new Conexion();
        c.abrir();
        System.out.println(query);
        ResultSet rs = c.ejecutarQuery(query);

        try {
            while (rs.next()) {
                String[] fila = new String[campos + 1];
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    fila[i] = rs.getObject(i + 1).toString().trim();
                }
                fila[fila.length - 1] = "";
                filas.add(fila);
                cont++;
            }
        } catch (SQLException ex) {
            System.out.println("No se puede procesar las filas para el remarcador");
            System.out.println(ex);
        }
        String[][] salida = new String[cont][campos + 1];
        for (int x = 0; x < filas.size(); x++) {
            salida[x] = filas.get(x);
        }
        System.out.println("Tabla cruda procesada");
        c.cerrar();
        switch (tabla) {
            case "circutorcvmC10":
                return getTablaCircutor(salida);
            case "schneiderPM710":
                return getTablaSchneiderPM710(salida);
            case "schneiderPM5300":
                return getTablaSchneiderPM5300(salida);
        }
        return null;
    }

    /**
     * Construye la consulta a la base de datos para obtener los campos de la
     * tabla que corresponda al tipo de remarcador. Esta función incorpora el
     * relleno de las lecturas vacías en la tabla origen del remarcador
     * seleccionado. Devuelve un arreglo con todas las filas del mes-anio ya
     * procesado y con continuidad ajustada.
     *
     * @param numremarcador {@code int}. Corresponde al ID REMARCADOR.
     * @param mes {@code int}. Corresponde al mes de la consulta.
     * @param anio {@code int}. Corresponde al año de la consulta.
     * @return {@code Array} {@link etl.FilaNormal} con los campos deseados y
     * transformados.
     */
    public static FilaNormal[] getDatasetRemarcador(int numremarcador, int mes, int anio) {
        String[][] origenes = getOrigenesRemarcador();
        String tabla = "";
        for (String[] fila : origenes) {
            if (Integer.parseInt(fila[1]) == numremarcador) {
                tabla = fila[0];
            }
        }
        String query = "";
        int campos = 0;
        switch (tabla) {
            case "circutorcvmC10":
                query = "SELECT CONVERT(A.TIMESTAMP, CHAR) TIMESTAMP, A.EQUIPO_ID, TRIM(A.ITEM49) ITEM49, TRIM(A.ITEM50) ITEM50, TRIM(A.ITEM95) ITEM95, TRIM(A.ITEM96) ITEM96, CASE WHEN B.NUMREMARCADOR IS NULL THEN 'NO' ELSE 'SI' END AS ESMANUAL, CASE WHEN B.NUMREMARCADOR IS NULL THEN 0 ELSE B.LECTURA END AS LECTURAMANUAL  FROM " + tabla + " A LEFT JOIN LECTURAMANUAL B ON A.EQUIPO_ID = B.NUMREMARCADOR AND A.FECHA = B.FECHA AND A.HORA = B.HORA WHERE A.EQUIPO_ID = " + numremarcador + " AND A.MES = " + mes + " AND A.ANIO = " + anio + " ORDER BY A.TIMESTAMP ASC";
                campos = 8;
                break;
            case "schneiderPM710":
                query = "SELECT CONVERT(A.TIMESTAMP, CHAR) TIMESTAMP, A.EQUIPO_ID, TRIM(A.ITEM7) ITEM7, TRIM(A.ITEM108) ITEM108, TRIM(A.ITEM1) ITEM1, TRIM(A.ITEM2) ITEM2, TRIM(A.ITEM109) ITEM109, CASE WHEN B.NUMREMARCADOR IS NULL THEN 'NO' ELSE 'SI' END AS ESMANUAL, CASE WHEN B.NUMREMARCADOR IS NULL THEN 0 ELSE B.LECTURA END AS LECTURAMANUAL  FROM " + tabla + " A LEFT JOIN LECTURAMANUAL B ON A.EQUIPO_ID = B.NUMREMARCADOR AND A.FECHA = B.FECHA AND A.HORA = B.HORA WHERE A.EQUIPO_ID = " + numremarcador + " AND A.MES = " + mes + " AND A.ANIO = " + anio + " ORDER BY A.TIMESTAMP ASC";
                campos = 9;
                break;
            case "schneiderPM5300":
                query = "SELECT CONVERT(A.TIMESTAMP, CHAR) TIMESTAMP, A.EQUIPO_ID, TRIM(A.ITEM1) ITEM1, TRIM(A.ITEM43) ITEM43, CASE WHEN B.NUMREMARCADOR IS NULL THEN 'NO' ELSE 'SI' END AS ESMANUAL, CASE WHEN B.NUMREMARCADOR IS NULL THEN 0 ELSE B.LECTURA END AS LECTURAMANUAL  FROM " + tabla + " A LEFT JOIN LECTURAMANUAL B ON A.EQUIPO_ID = B.NUMREMARCADOR AND A.FECHA = B.FECHA AND A.HORA = B.HORA WHERE A.EQUIPO_ID = " + numremarcador + " AND A.MES =" + mes + " AND A.ANIO = " + anio + " ORDER BY A.TIMESTAMP ASC";
                campos = 6;
                break;
        }
        LinkedList<String[]> filas = new LinkedList();
        int cont = 0;
        Conexion c = new Conexion();
        c.abrir();
        System.out.println(query);
        ResultSet rs = c.ejecutarQuery(query);

        try {
            while (rs.next()) {
                String[] fila = new String[campos + 1];
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    fila[i] = rs.getObject(i + 1).toString().trim();
                }
                fila[fila.length - 1] = "";
                filas.add(fila);
                cont++;
            }
        } catch (SQLException ex) {
            System.out.println("No se puede procesar las filas para el remarcador");
            System.out.println(ex);
        }
        String[][] salida = new String[cont][campos + 1];
        for (int x = 0; x < filas.size(); x++) {
            salida[x] = filas.get(x);
        }
        System.out.println("Tabla cruda procesada");
        c.cerrar();
        switch (tabla) {
            case "circutorcvmC10":
                return getTablaCircutor(salida);
            case "schneiderPM710":
                return getTablaSchneiderPM710(salida);
            case "schneiderPM5300":
                return getTablaSchneiderPM5300(salida);
        }
        return null;
    }

    /**
     * Obtiene el valor de la energía del remarcador Circutor. Aplica las reglas
     * de cálculo que se encuentran en la documentación.
     *
     * @param contador {@code int}. Contador de energía del remarcador. ITEM96
     * en tabla.
     * @param factor {@code int}. El multiplicador de vueltas del contador.
     * ITEM95 en tabla.
     * @return {@code int}. Resultado del contador real.
     */
    private static int getValorEnergiaCircutor(int contador, int factor) {
        if (contador >= 0) {
            return (factor * 65536) + contador;
        } else {
            return (factor * 65536) + (contador + 65536);
        }
    }

    /**
     * Obtiene el valor de la potencia activa
     *
     * @param contador {@code int}. Corresponde al contador de potencia. ITEM50
     * en tabla.
     * @param factor {@code int}. Corresponde al multiplicador de la potencia.
     * ITEM49 en tabla.
     * @return {@code double}. La potencia activa instantánea.
     */
    private static double getValorPotenciaCircutor(int contador, int factor) {
        if (contador >= 0) {
            return (((double) factor * 65536) + (double) contador) / 1000;
        } else {
            return (((double) factor * 65536) + ((double) contador + 65536)) / 1000;
        }
    }

    /**
     * Obtiene el valor de la energía del remarcador.
     *
     * @param contador {@code int}. Corresponde al contador del remarcador.
     * ITEM2 en tabla.
     * @param factor {@code int}. Corresponde al factor de vueltas del contador.
     * ITEM1 en tabla.
     * @param potencia {@code int}. Corresponde a la potencia aplicada a la
     * fórmula en base 10. ITEM109 en tabla.
     * @return {@code double}. Con el contador de la energía.
     */
    private static double getValorEnergiaSchneiderPM710(int contador, int factor, int potencia) {
        if (contador == 0) {
            return (double) (((factor * 65536) + contador) * Math.pow(10, potencia));
        } else if (contador < 0) {
            return (double) (((factor * 65536) + (contador + 65536)) * Math.pow(10, potencia));
        } else if (contador > 0) {
            return (double) ((factor * 65536) + (contador)) * Math.pow(10, potencia);
        }
        return 0.0d;
    }

    /**
     * Obtiene el resultado de la potencia del remarcador.
     *
     * @param contador {@code int}. Corresponde al contador de potencia. ITEM7
     * en tabla.
     * @param potencia {@code int}. Corresponde a la potencia de la base 10 para
     * el cálculo. ITEM108 en tabla.
     * @return {@code double} Con la potencia instantánea conectada al
     * remarcador.
     */
    private static double getValorPotenciaSchneiderPM710(int contador, int potencia) {
        return (double) ((double) contador * Math.pow(10, (double) potencia));
    }

    /**
     * Obtiene la energía del remarcador
     *
     * @param energia {@code double} la energía del remarcador.
     * @return {@code double} la energía del remarcador.
     */
    private static double getValorEnergiaSchneiderPM5300(double energia) {
        return energia;
    }

    /**
     * Obtiene la potencia del remarcador
     *
     * @param potencia {@code double} la potencia del remarcador.
     * @return {@code double} la potencia del remarcador.
     */
    private static double getValorPotenciaSchneiderPM5300(double potencia) {
        return potencia;
    }

    /**
     * Crea la tabla de salida de remarcador circutorcvmC10 con la recibida
     * desde los datos crudos. Genera continuidad en la remarcación detectando
     * las bajas en los valores que puede ser generada por reseteos del
     * remarcador. Mantiene una cuenta pareja ascendente, además de entregar
     * también la lectura real del remarcador.
     *
     * @param tabla {@code String[][]} con los datos de remarcación crudos sin
     * vacíos, pero puede contener regresos en la continuidad.
     * @return {@link etl.FilaNormal[]} con los dato ordenados y con
     * continuidad.
     */
    private static FilaNormal[] getTablaCircutor(String[][] tabla) {
        //[0]TIMESTAMP, [1]ID, [2]ITEM49, [3]ITEM50, [4]ITEM95, [5]ITEM96, [6]ESMANUAL, [7]VALORMANUAL
        FilaNormal[] lecturas = new FilaNormal[tabla.length];
        for (int i = 0; i < tabla.length; i++) {
            lecturas[i] = new FilaNormal();

            //Poner valores fijos.
            String fechahora = tabla[i][0].replace(".0", "");
            String fecha = tabla[i][0].replace(".0", "").split(" ")[0];
            String hora = tabla[i][0].replace(".0", "").split(" ")[1];
            int idremarcador = Integer.parseInt(tabla[i][1]);
            lecturas[i].idremarcador = idremarcador;
            lecturas[i].fechahora = fechahora;
            lecturas[i].fecha = fecha;
            lecturas[i].hora = hora;
            String esmanual = tabla[i][6];

            //Verificar si existe valor y guardarlo
            if (tabla[i][4].equals("") || tabla[i][5].equals("")) {
                lecturas[i].existe = false;
            } else {
                lecturas[i].existe = true;
                lecturas[i].lecturareal = getValorEnergiaCircutor(Integer.parseInt(tabla[i][5]), Integer.parseInt(tabla[i][4]));
            }

            if (tabla[i][2].equals("") || tabla[i][3].equals("")) {
                lecturas[i].potencia = 0;
            } else {
                lecturas[i].potencia = getValorPotenciaCircutor(Integer.parseInt(tabla[i][3]), Integer.parseInt(tabla[i][2]));
            }

            //Verificar si es manual y guardarlo
            if (esmanual.equals("SI")) {
                lecturas[i].esmanual = true;
                lecturas[i].lecturamanual = Integer.parseInt(tabla[i][7]);
            } else {
                lecturas[i].esmanual = false;
            }
        }

        System.out.println("Tabla circutorcvmC10 lista");
        return procesarLecturas(lecturas);
    }

    private static FilaNormal[] getTablaSchneiderPM710(String[][] tabla) {
        FilaNormal[] lecturas = new FilaNormal[tabla.length];
        for (int i = 0; i < tabla.length; i++) {
            lecturas[i] = new FilaNormal();

            //Poner valores fijos.
            String fechahora = tabla[i][0].replace(".0", "");
            String fecha = tabla[i][0].replace(".0", "").split(" ")[0];
            String hora = tabla[i][0].replace(".0", "").split(" ")[1];
            int idremarcador = Integer.parseInt(tabla[i][1]);
            lecturas[i].idremarcador = idremarcador;
            lecturas[i].fechahora = fechahora;
            lecturas[i].fecha = fecha;
            lecturas[i].hora = hora;
            String esmanual = tabla[i][7];

            //Verificar si existe valor y guardarlo
            if (tabla[i][4].equals("") || tabla[i][5].equals("") || tabla[i][6].equals("")) {
                lecturas[i].existe = false;
            } else {
                lecturas[i].existe = true;
                lecturas[i].lecturareal = getValorEnergiaSchneiderPM710(Integer.parseInt(tabla[i][5]), Integer.parseInt(tabla[i][4]), Integer.parseInt(tabla[i][6]));
            }

            if (tabla[i][2].equals("") || tabla[i][3].equals("")) {
                lecturas[i].potencia = 0;
            } else {
                lecturas[i].potencia = getValorPotenciaSchneiderPM710(Integer.parseInt(tabla[i][2]), Integer.parseInt(tabla[i][3]));
            }

            //Verificar si es manual y guardarlo
            if (esmanual.equals("SI")) {
                lecturas[i].esmanual = true;
                lecturas[i].lecturamanual = Integer.parseInt(tabla[i][8]);
            } else {
                lecturas[i].esmanual = false;
            }
        }

        System.out.println("Tabla SchneiderPM710 lista");
        return procesarLecturas(lecturas);
    }

    private static FilaNormal[] getTablaSchneiderPM5300(String[][] tabla) {
        FilaNormal[] lecturas = new FilaNormal[tabla.length];
        for (int i = 0; i < tabla.length; i++) {
            lecturas[i] = new FilaNormal();

            //Poner valores fijos.
            String fechahora = tabla[i][0].replace(".0", "");
            String fecha = tabla[i][0].replace(".0", "").split(" ")[0];
            String hora = tabla[i][0].replace(".0", "").split(" ")[1];
            int idremarcador = Integer.parseInt(tabla[i][1]);
            lecturas[i].idremarcador = idremarcador;
            lecturas[i].fechahora = fechahora;
            lecturas[i].fecha = fecha;
            lecturas[i].hora = hora;
            String esmanual = tabla[i][4];

            //Verificar si existe valor y guardarlo
            if (tabla[i][2].equals("5.8774717541114E-39")) {
                lecturas[i].existe = false;
            } else {
                lecturas[i].existe = true;
                lecturas[i].lecturareal = getValorEnergiaSchneiderPM5300(Double.parseDouble(tabla[i][2]));
            }

            if (tabla[i][3].equals("5.8774717541114E-39")) {
                lecturas[i].potencia = 0;
            } else {
                lecturas[i].potencia = getValorPotenciaSchneiderPM5300(Double.parseDouble(tabla[i][3]));
            }

            //Verificar si es manual y guardarlo
            if (esmanual.equals("SI")) {
                lecturas[i].esmanual = true;
                lecturas[i].lecturamanual = Integer.parseInt(tabla[i][5]);
            } else {
                lecturas[i].esmanual = false;
            }
        }

        System.out.println("Tabla SchneiderPM5300 lista");
        return procesarLecturas(lecturas);
    }
    
    private static FilaNormal[] procesarLecturas(FilaNormal[] lecturas){
        for (int i = 0; i < lecturas.length; i++) {
            if (i == 0) {

                lecturas[i].ultimomax = 0;
                if (!lecturas[i].existe) {
                    for (int x = (i + 1); x < lecturas.length; x++) {
                        if (lecturas[x].existe) {
                            lecturas[i].lecturareal = lecturas[x].lecturareal;
                            lecturas[i].pisada = true;
                            break;
                        }
                    }
                }
                if (!lecturas[i].existe && lecturas[i].esmanual) {
                    lecturas[i].lecturaproyectada = lecturas[i].lecturamanual;
                } else if (!lecturas[i].existe && !lecturas[i].esmanual && lecturas[i].pisada) {
                    lecturas[i].lecturaproyectada = lecturas[i].lecturareal;
                } else if (lecturas[i].existe || lecturas[i].pisada) {
                    lecturas[i].lecturaproyectada = lecturas[i].lecturareal;
                }
            } else if (i > 0) {
                if (!lecturas[i].existe) {
                    for (int x = (i - 1); x >= 0; x--) {
                        if (lecturas[x].existe || lecturas[x].pisada) {
                            lecturas[i].lecturareal = lecturas[x].lecturareal;
                            break;
                        }
                    }
                }
                lecturas[i].ultimomax = lecturas[i - 1].lecturareal;
            }
            if (lecturas[i].ultimomax <= lecturas[i].lecturareal) {
                lecturas[i].delta = lecturas[i].lecturareal - lecturas[i].ultimomax;
            } else {
                lecturas[i].delta = (lecturas[i].ultimomax - lecturas[i].lecturareal) - lecturas[i].ultimomax;
            }
            if (i > 0) {
                if(lecturas[i].esmanual && !lecturas[i - 1].esmanual){
                    lecturas[i].delta = lecturas[i].lecturamanual - lecturas[i].lecturareal;
                }
                lecturas[i].lecturaproyectada = lecturas[i - 1].lecturaproyectada + lecturas[i].delta;
            }
        }

        if (lecturas[lecturas.length - 1].esmanual) {
            if(lecturas[lecturas.length - 1].lecturamanual < lecturas[lecturas.length - 1].lecturaproyectada){
                lecturas[lecturas.length - 1].lecturaproyectada = lecturas[lecturas.length - 1].lecturaproyectada + (lecturas[lecturas.length - 1].lecturamanual - lecturas[lecturas.length - 1].lecturareal);
            }
        }
        return lecturas;
    }

}
