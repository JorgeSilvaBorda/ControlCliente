package etl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import modelo.Conexion;

/**
 * Clase creada para obtener dataset completo de los remarcadores y procesarlos fuera de SQL
 * @author Jorge Silva Borda
 */
public class ETL {
    
    /**
     * Obtiene el listado de ID de remarcadores y su tabla de origen
     * @return {@code String[][]}. En donde:
     * [0] = ORIGEN.
     * [1] = ID REMARCADOR.
     */
    private static String[][] getOrigenesRemarcador() {
        String query = "CALL SP_GET_ORIGEN_REMARCADORES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

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
     * Construye la consulta a la base de datos para obtener los campos de la tabla que corresponda al tipo de remarcador.
     * Esta función incorpora el relleno de las lecturas vacías en la tabla origen del remarcador seleccionado.
     * Devuelve un arreglo con todas las filas del rango de fechas ya procesado y con continuidad ajustada.
     * @param idRemarcador {@code int}. Corresponde al ID REMARCADOR.
     * @param fechaDesde {@code String}. Corresponde a la fecha de inicio de la búsqueda.
     * @param fechaHasta {@code String}. Corresponde a la fecha de térmimo de la búsqueda.
     * @return {@code Array} {@link etl.FilaNormal} con los campos deseados y transformados.
     */
    public static FilaNormal[] getDatasetRemarcador(int idRemarcador, String fechaDesde, String fechaHasta) {
        String[][] origenes = getOrigenesRemarcador();
        String tabla = "";
        for (String[] fila : origenes) {
            if (Integer.parseInt(fila[1]) == idRemarcador) {
                tabla = fila[0];
            }
        }
        String query = "";
        int campos = 0;
        switch (tabla) {
            case "circutorcvmC10":
                query = "SELECT CONVERT(TIMESTAMP, CHAR) TIMESTAMP, EQUIPO_ID, TRIM(ITEM49) ITEM49, TRIM(ITEM50) ITEM50, TRIM(ITEM95) ITEM95, TRIM(ITEM96) ITEM96 FROM " + tabla + " WHERE EQUIPO_ID = " + idRemarcador + " AND FECHA >= '" + fechaDesde + "' AND FECHA <= '" + fechaHasta + "' ORDER BY TIMESTAMP ASC";
                campos = 6;
                break;
            case "schneiderPM710":
                query = "SELECT CONVERT(TIMESTAMP, CHAR) TIMESTAMP, EQUIPO_ID, TRIM(ITEM7) ITEM7, TRIM(ITEM108) ITEM108, TRIM(ITEM1) ITEM1, TRIM(ITEM2) ITEM2, TRIM(ITEM109) ITEM109 FROM " + tabla + " WHERE EQUIPO_ID = " + idRemarcador + " AND FECHA >= '" + fechaDesde + "' AND FECHA <= '" + fechaHasta + "' ORDER BY TIMESTAMP ASC";
                campos = 7;
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
                String[] fila = new String[campos];
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    fila[i] = rs.getObject(i + 1).toString().trim();
                }
                filas.add(fila);
                cont++;
            }
        } catch (SQLException ex) {
            System.out.println("No se puede procesar las filas para el remarcador");
            System.out.println(ex);
        }
        String[][] salida = new String[cont][campos];
        for (int x = 0; x < filas.size(); x++) {
            salida[x] = filas.get(x);
        }
        System.out.println("Tabla cruda procesada");
        c.cerrar();
        switch(tabla){
            case "circutorcvmC10":
                return getTablaCircutor(salida);
            case "schneiderPM710":
                return getTablaSchneiderPM710(salida);
        }
        return null;
    }
    
    /**
     * Construye la consulta a la base de datos para obtener los campos de la tabla que corresponda al tipo de remarcador.
     * Esta función incorpora el relleno de las lecturas vacías en la tabla origen del remarcador seleccionado.
     * Devuelve un arreglo con todas las filas del mes-anio ya procesado y con continuidad ajustada.
     * @param idRemarcador {@code int}. Corresponde al ID REMARCADOR.
     * @param mes {@code int}. Corresponde al mes de la consulta.
     * @param anio {@code int}. Corresponde al año de la consulta.
     * @return 
     */
    public static FilaNormal[] getDatasetRemarcador(int idRemarcador, int mes, int anio) {
        String[][] origenes = getOrigenesRemarcador();
        String tabla = "";
        for (String[] fila : origenes) {
            if (Integer.parseInt(fila[1]) == idRemarcador) {
                tabla = fila[0];
            }
        }
        String query = "";
        int campos = 0;
        switch (tabla) {
            case "circutorcvmC10":
                query = "SELECT CONVERT(TIMESTAMP, CHAR) TIMESTAMP, EQUIPO_ID, TRIM(ITEM49) ITEM49, TRIM(ITEM50) ITEM50, TRIM(ITEM95) ITEM95, TRIM(ITEM96) ITEM96 FROM " + tabla + " WHERE EQUIPO_ID = " + idRemarcador + " AND MES >= " + mes + " AND ANIO <= " + anio + " ORDER BY TIMESTAMP ASC";
                campos = 6;
                break;
            case "schneiderPM710":
                query = "SELECT CONVERT(TIMESTAMP, CHAR) TIMESTAMP, EQUIPO_ID, TRIM(ITEM7) ITEM7, TRIM(ITEM108) ITEM108, TRIM(ITEM1) ITEM1, TRIM(ITEM2) ITEM2, TRIM(ITEM109) ITEM109 FROM " + tabla + " WHERE EQUIPO_ID = " + idRemarcador + " AND MES >= " + mes + " AND ANIO <= " + anio + " ORDER BY TIMESTAMP ASC";
                campos = 7;
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
                String[] fila = new String[campos];
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    fila[i] = rs.getObject(i + 1).toString().trim();
                }
                filas.add(fila);
                cont++;
            }
        } catch (SQLException ex) {
            System.out.println("No se puede procesar las filas para el remarcador");
            System.out.println(ex);
        }
        String[][] salida = new String[cont][campos];
        for (int x = 0; x < filas.size(); x++) {
            salida[x] = filas.get(x);
        }
        System.out.println("Tabla cruda procesada");
        c.cerrar();
        switch(tabla){
            case "circutorcvmC10":
                return getTablaCircutor(salida);
            case "schneiderPM710":
                return getTablaSchneiderPM710(salida);
        }
        return null;
    }

    /**
     * Obtiene el valor de la energía del remarcador Circutor.
     * Aplica las reglas de cálculo que se encuentran en la documentación.
     * @param contador {@code int}. Contador de energía del remarcador. ITEM96 en tabla.
     * @param factor {@code int}. El multiplicador de vueltas del contador. ITEM95 en tabla.
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
     * @param contador {@code int}. Corresponde al contador de potencia. ITEM50 en tabla.
     * @param factor {@code int}. Corresponde al multiplicador de la potencia. ITEM49 en tabla.
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
     * @param contador {@code int}. Corresponde al contador del remarcador. ITEM2 en tabla.
     * @param factor {@code int}. Corresponde al factor de vueltas del contador. ITEM1 en tabla.
     * @param potencia {@code int}. Corresponde a la potencia aplicada a la fórmula en base 10. ITEM109 en tabla.
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
     * @param contador {@code int}. Corresponde al contador de potencia. ITEM7 en tabla.
     * @param potencia {@code int}. Corresponde a la potencia de la base 10 para el cálculo. ITEM108 en tabla.
     * @return {@code double} Con la potencia instantánea conectada al remarcador.
     */
    private static double getValorPotenciaSchneiderPM710(int contador, int potencia) {
        return (double) ((double) contador * Math.pow(10, (double) potencia));
    }

    //Solo referencial. Funciona con Circutor
    private static FilaNormal[] emparejarTabla(String[][] tabla) {
        /*
        try {
            final Path path = Paths.get("/home/jorge/dev/pisar-43.sql");
            Files.write(path, Arrays.asList("SET AUTOCOMMIT = FALSE;"), StandardCharsets.UTF_8,Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            Files.write(path, Arrays.asList("LOCK TABLES `CONSUMO` WRITE;"), StandardCharsets.UTF_8,Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (final IOException ioe) {
            System.out.println(ioe);
        }
         */
        FilaNormal[] tablaNormal = new FilaNormal[tabla.length];
        for (int i = 0; i < tabla.length; i++) {

            if (tabla[i][2].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][2].equals("")) {
                        tabla[i][2] = tabla[x][2];
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][2].equals("")) {
                            tabla[i][2] = tabla[z][2];
                            encontrado = true;
                        }
                    }
                }

            }
            if (tabla[i][3].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][3].equals("")) {
                        tabla[i][3] = tabla[x][3];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][3].equals("")) {
                            tabla[i][3] = tabla[z][3];
                            encontrado = true;
                        }
                    }
                }
            }

            if (tabla[i][4].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][4].equals("")) {
                        tabla[i][4] = tabla[x][4];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][4].equals("")) {
                            tabla[i][4] = tabla[z][4];
                            encontrado = true;
                        }
                    }
                }
            }
            if (tabla[i][5].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][5].equals("")) {
                        tabla[i][5] = tabla[x][5];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][5].equals("")) {
                            tabla[i][5] = tabla[z][5];
                            encontrado = true;
                        }
                    }
                }
            }
        }
        int acum = 0;
        int lectura = 0;
        for (int i = 0; i < tabla.length; i++) {
            int contador = 0;
            int ultimomax = 0;
            int consumo;
            contador = getValorEnergiaCircutor(Integer.parseInt(tabla[i][5]), Integer.parseInt(tabla[i][4]));
            if (i == 0) {
                ultimomax = 0;
            } else {
                ultimomax = getValorEnergiaCircutor(Integer.parseInt(tabla[i - 1][5]), Integer.parseInt(tabla[i - 1][4]));
            }

            if (ultimomax <= contador) {
                consumo = contador - ultimomax;
            } else {
                consumo = (ultimomax - contador) - ultimomax;
            }
            double potencia = getValorPotenciaCircutor(Integer.parseInt(tabla[i][3]), Integer.parseInt(tabla[i][2]));
            acum = acum + consumo;
            lectura = lectura + consumo;
            //[timestamp][id][factor-potencia][contador-potencia][potencia][factor-energia][contador-energia][energia][ultimomax][consumo(delta)][lectura continua]
            System.out.println(tabla[i][0].replace(".0", "") + ";" + tabla[i][1] + ";" + tabla[i][2] + ";" + tabla[i][3] + ";" + potencia + ";" + tabla[i][4] + ";" + tabla[i][5] + ";" + contador + ";" + ultimomax + ";" + consumo + ";" + lectura);
            //System.out.println(tabla[i][0] + ";" + tabla[i][1] + ";" + tabla[i][2] + ";" + tabla[i][3] + ";" + tabla[i][4] + ";" + tabla[i][5] + ";" + contador + ";" + ultimomax + ";" + consumo + ";" + lectura);
            //System.out.println("INSERT INTO CONSUMO(TIMESTAMP, REMARCADOR_ID, EnergiaActivaConsumida_KWH, PotenciaActivaTotal_KW, FECHA, ANIO, MES, DIA, HORA, HH, MM, SS, DATONULO, CONSUMO, ULTIMOMAX) VALUES('" + tabla[i][0].replace(".0", "") + "', " + tabla[i][1] + ", " + lectura + ", " + potencia + ", date('" + tabla[i][0].replace(".0", "") + "'), year('" + tabla[i][0].replace(".0", "") + "'), month('" + tabla[i][0].replace(".0", "") + "'), day('" + tabla[i][0].replace(".0", "") + "'), TIME('" + tabla[i][0].replace(".0", "") + "'), HOUR('" + tabla[i][0].replace(".0", "") + "'), MINUTE('" + tabla[i][0].replace(".0", "") + "'), SECOND('" + tabla[i][0].replace(".0", "") + "'), 0, " + contador + ", " + ultimomax + ")");
            //String fila = "INSERT INTO CONSUMO(TIMESTAMP, REMARCADOR_ID, EnergiaActivaConsumida_KWH, PotenciaActivaTotal_KW, FECHA, ANIO, MES, DIA, HORA, HH, MM, SS, DATONULO, CONSUMO, ULTIMOMAX) VALUES('" + tabla[i][0].replace(".0", "") + "', " + tabla[i][1] + ", " + lectura + ", " + potencia + ", date('" + tabla[i][0].replace(".0", "") + "'), year('" + tabla[i][0].replace(".0", "") + "'), month('" + tabla[i][0].replace(".0", "") + "'), day('" + tabla[i][0].replace(".0", "") + "'), TIME('" + tabla[i][0].replace(".0", "") + "'), HOUR('" + tabla[i][0].replace(".0", "") + "'), MINUTE('" + tabla[i][0].replace(".0", "") + "'), SECOND('" + tabla[i][0].replace(".0", "") + "'), 0, " + contador + ", " + ultimomax + ");";
            //System.out.println(tabla[i].fecha + ";" + tabla[i].idremarcador + ";" + (tabla[i].factorPotencia.equals("") ? '0' : tabla[i].factorPotencia) + ";" + tabla[i].contadorPotencia + ";" + tabla[i].factorEnergia + ";" + tabla[i].contadorEnergia);
            /*
            try {
                final Path path = Paths.get("/home/jorge/dev/pisar-43.sql");
                Files.write(path, Arrays.asList(fila), StandardCharsets.UTF_8,
                        Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            } catch (final IOException ioe) {
                System.out.println(ioe);
            }
             */
        }
        /*
        try {
            final Path path = Paths.get("/home/jorge/dev/pisar-43.sql");
            Files.write(path, Arrays.asList("COMMIT;"), StandardCharsets.UTF_8,Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            Files.write(path, Arrays.asList("SET AUTOCOMMIT = TRUE;"), StandardCharsets.UTF_8,Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            Files.write(path, Arrays.asList("UNLOCK TABLES;"), StandardCharsets.UTF_8,Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (final IOException ioe) {
            System.out.println(ioe);
        }
         */
        System.out.println("Acum: " + acum);
        return tablaNormal;
    }

    /**
     * Crea la tabla de salida de remarcador circutorcvmC10 con la recibida desde los datos crudos.
     * Genera continuidad en la remarcación detectando las bajas en los valores que puede ser generada por reseteos del remarcador.
     * Mantiene una cuenta pareja ascendente, además de entregar también la lectura real del remarcador.
     * @param tabla {@code String[][]} con los datos de remarcación crudos sin vacíos, pero puede contener regresos en la continuidad.
     * @return {@link etl.FilaNormal[]} con los dato ordenados y con continuidad.
     */
    private static FilaNormal[] getTablaCircutor(String[][] tabla) {
        FilaNormal[] tablaNormal = new FilaNormal[tabla.length];
        for (int i = 0; i < tabla.length; i++) {

            if (tabla[i][2].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][2].equals("")) {
                        tabla[i][2] = tabla[x][2];
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][2].equals("")) {
                            tabla[i][2] = tabla[z][2];
                            encontrado = true;
                        }
                    }
                }

            }
            if (tabla[i][3].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][3].equals("")) {
                        tabla[i][3] = tabla[x][3];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][3].equals("")) {
                            tabla[i][3] = tabla[z][3];
                            encontrado = true;
                        }
                    }
                }
            }

            if (tabla[i][4].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][4].equals("")) {
                        tabla[i][4] = tabla[x][4];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][4].equals("")) {
                            tabla[i][4] = tabla[z][4];
                            encontrado = true;
                        }
                    }
                }
            }
            if (tabla[i][5].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][5].equals("")) {
                        tabla[i][5] = tabla[x][5];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][5].equals("")) {
                            tabla[i][5] = tabla[z][5];
                            encontrado = true;
                        }
                    }
                }
            }
        }
        int acum = 0;
        int lectura = 0;
        for (int i = 0; i < tabla.length; i++) {
            int contador = 0;
            int ultimomax = 0;
            int consumo;
            contador = getValorEnergiaCircutor(Integer.parseInt(tabla[i][5]), Integer.parseInt(tabla[i][4]));
            if (i == 0) {
                ultimomax = 0;
            } else {
                ultimomax = getValorEnergiaCircutor(Integer.parseInt(tabla[i - 1][5]), Integer.parseInt(tabla[i - 1][4]));
            }

            if (ultimomax <= contador) {
                consumo = contador - ultimomax;
            } else {
                consumo = (ultimomax - contador) - ultimomax;
            }
            double potencia = getValorPotenciaCircutor(Integer.parseInt(tabla[i][3]), Integer.parseInt(tabla[i][2]));
            acum = acum + consumo;
            lectura = lectura + consumo;
            String fechahora = tabla[i][0].replace(".0", "");
            String fecha = tabla[i][0].replace(".0", "").split(" ")[0];
            String hora = tabla[i][0].replace(".0", "").split(" ")[1];
            int idremarcador = Integer.parseInt(tabla[i][1]);

            tablaNormal[i] = new FilaNormal(fechahora, fecha, hora, idremarcador, lectura, potencia, contador, consumo, ultimomax);
        }
        System.out.println("Tabla circutorcvmC10 lista");
        return tablaNormal;
    }
    
    /**
     * Crea la tabla de salida de remarcador SchneiderPM710 con la recibida desde los datos crudos.
     * Genera continuidad en la remarcación detectando las bajas en los valores que puede ser generada por reseteos del remarcador.
     * Mantiene una cuenta pareja ascendente, además de entregar también la lectura real del remarcador.
     * @param tabla {@code String[][]} con los datos de remarcación crudos sin vacíos, pero puede contener regresos en la continuidad.
     * @return {@link etl.FilaNormal[]} con los dato ordenados y con continuidad.
     */
    private static FilaNormal[] getTablaSchneiderPM710(String[][] tabla) {
        FilaNormal[] tablaNormal = new FilaNormal[tabla.length];
        for (int i = 0; i < tabla.length; i++) {

            if (tabla[i][2].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][2].equals("")) {
                        tabla[i][2] = tabla[x][2];
                        encontrado = true;
                    }
                }

                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][2].equals("")) {
                            tabla[i][2] = tabla[z][2];
                            encontrado = true;
                        }
                    }
                }

            }
            if (tabla[i][3].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][3].equals("")) {
                        tabla[i][3] = tabla[x][3];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][3].equals("")) {
                            tabla[i][3] = tabla[z][3];
                            encontrado = true;
                        }
                    }
                }
            }

            if (tabla[i][4].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][4].equals("")) {
                        tabla[i][4] = tabla[x][4];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][4].equals("")) {
                            tabla[i][4] = tabla[z][4];
                            encontrado = true;
                        }
                    }
                }
            }
            if (tabla[i][5].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][5].equals("")) {
                        tabla[i][5] = tabla[x][5];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][5].equals("")) {
                            tabla[i][5] = tabla[z][5];
                            encontrado = true;
                        }
                    }
                }
            }
            if (tabla[i][6].equals("")) {
                boolean encontrado = false;
                for (int x = (i + 1); (x < tabla.length) && !encontrado; x++) {
                    if (!tabla[x][6].equals("")) {
                        tabla[i][6] = tabla[x][6];
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    for (int z = (i - 1); (z >= 0) && !encontrado; z--) {
                        if (!tabla[z][6].equals("")) {
                            tabla[i][6] = tabla[z][6];
                            encontrado = true;
                        }
                    }
                }
            }
        }
        double acum = 0.0d;
        double lectura = 0.0d;
        for (int i = 0; i < tabla.length; i++) {
            double contador = 0.0d;
            double ultimomax = 0.0d;
            double consumo;
            contador = getValorEnergiaSchneiderPM710(Integer.parseInt(tabla[i][5]), Integer.parseInt(tabla[i][4]), Integer.parseInt(tabla[i][6]));
            if (i == 0) {
                ultimomax = 0;
            } else {
                ultimomax = getValorEnergiaSchneiderPM710(Integer.parseInt(tabla[i - 1][5]), Integer.parseInt(tabla[i - 1][4]), Integer.parseInt(tabla[i - 1][6]));
            }

            if (ultimomax <= contador) {
                consumo = contador - ultimomax;
            } else {
                consumo = (ultimomax - contador) - ultimomax;
            }
            double potencia = getValorPotenciaSchneiderPM710(Integer.parseInt(tabla[i][2]), Integer.parseInt(tabla[i][3]));
            acum = acum + consumo;
            lectura = lectura + consumo;
            String fechahora = tabla[i][0].replace(".0", "");
            String fecha = tabla[i][0].replace(".0", "").split(" ")[0];
            String hora = tabla[i][0].replace(".0", "").split(" ")[1];
            int idremarcador = Integer.parseInt(tabla[i][1]);

            tablaNormal[i] = new FilaNormal(fechahora, fecha, hora, idremarcador, lectura, potencia, contador, consumo, ultimomax);
        }
        System.out.println("Tabla SchneiderPM710 lista");
        return tablaNormal;
    }

    /**
     * Imprime un array del tipo String[][].
     * @param filas {@code String[][]} con los datos a mostrar en pantalla.
     */
    private static void imprimirArreglo(String[][] filas) {

        for (String[] fila : filas) {
            String filaString = "";
            for (String campo : fila) {
                filaString += campo + ";";
            }
            System.out.println(filaString);
        }
    }

}