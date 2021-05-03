package controlador;

import clases.json.JSONArray;
import clases.json.JSONException;
import clases.json.JSONObject;
import etl.FilaNormal;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Conexion;
import modelo.Lectura;
import modelo.Util;

public class ReportesController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "consumo-cliente-remarcador":
                out.print(consumoClienteRemarcador(entrada));
                break;
            case "consumo-cliente-remarcador-aniomes":
                out.print(consumoClienteRemarcadorAnioMes(entrada));
                break;
            case "resumen-mes-cliente":
                out.print(resumenMesCliente(entrada));
                break;
            case "resumen-mes-remarcadores-empalme":
                out.print(resumenMesRemarcadoresEmpalme(entrada));
                break;
        }
    }

    private JSONObject consumoClienteRemarcador(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String mesini = "", mesfin = "";
        String query = "CALL SP_GET_FECHAS_ATRAS_DESDE_HOY(13);";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetData = new JSONArray();
        LinkedList<Object[]> fechas = new LinkedList();
        try {
            while (rs.next()) {
                Object[] datos = new Object[4];
                datos[0] = (String) rs.getString("MESANIO");
                datos[1] = (String) rs.getString("FECHAINI");
                datos[2] = (String) rs.getString("FECHAFIN");
                datos[3] = (int) 0;
                fechas.add(datos);
            }

        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ReportesController.consumoClienteRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();

        mesini = fechas.get(0)[1].toString();
        mesfin = fechas.get(fechas.size() - 1)[2].toString();
        FilaNormal[] filas = etl.ETL.getDatasetRemarcador(entrada.getInt("numremarcador"), mesini, mesfin);

        String mesanioActual = "";
        String iniMes = "";
        String finMes = "";
        double lecturaIniMes = 0.0d;
        double lecturaFinMes = 0.0d;
        LinkedList<Object[]> fechasLecturas = new LinkedList();

        for (int i = 0; i < filas.length; i++) {
            if (!filas[i].fecha.substring(0, 7).equals(mesanioActual) && i == 0) {
                lecturaIniMes = filas[i].lecturaproyectada;
                iniMes = filas[i].fecha;
                mesanioActual = filas[i].fecha.substring(0, 7);
            }
            if ((!filas[i].fecha.substring(0, 7).equals(mesanioActual)) && i > 0 && i < filas.length - 1) {
                finMes = filas[i - 1].fecha;
                lecturaFinMes = filas[i - 1].lecturaproyectada;
                Object[] fecha = new Object[4];
                fecha[0] = mesanioActual;
                fecha[1] = iniMes;
                fecha[2] = finMes;
                fecha[3] = (lecturaFinMes - lecturaIniMes);
                fechasLecturas.add(fecha);
                mesanioActual = filas[i].fecha.substring(0, 7);

                iniMes = filas[i].fecha;
                lecturaIniMes = filas[i].lecturaproyectada;
            }
            if (i == filas.length - 1) {
                finMes = filas[i].fecha;
                lecturaFinMes = filas[i].lecturaproyectada;
                Object[] fecha = new Object[4];
                fecha[0] = mesanioActual;
                fecha[1] = iniMes;
                fecha[2] = finMes;
                fecha[3] = (lecturaFinMes - lecturaIniMes);
                fechasLecturas.add(fecha);
            }
        }
        for (int i = 0; i < fechas.size(); i++) {
            for (int x = 0; x < fechasLecturas.size(); x++) {
                if (fechas.get(i)[0].equals(fechasLecturas.get(x)[0])) {
                    fechas.get(i)[3] = fechasLecturas.get(x)[3];
                }
            }
        }

        for (Object[] fecs : fechas) {
            System.out.println(fecs[1] + " - " + fecs[2] + ": " + fecs[3]);
            labels.put(Util.invertirFecha(fecs[2].toString()));
            datasetData.put(Double.parseDouble(fecs[3].toString()));
        }
        JSONObject dataset = new JSONObject();
        dataset.put("data", datasetData);
        dataset.put("label", "Remarcador ID: " + entrada.getInt("numremarcador"));
        dataset.put("borderWidth", "2");
        datasets.put(dataset);
        JSONObject data = new JSONObject();
        data.put("labels", labels);
        data.put("datasets", datasets);
        salida.put("data", data);
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject consumoClienteRemarcadorAnioMes(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int anio = Integer.parseInt(entrada.getString("aniomes").split("-")[0]);
        int mes = Integer.parseInt(entrada.getString("aniomes").split("-")[1]);

        String mesini = "", mesfin = "";
        String query = "CALL SP_GET_FECHAS_ATRAS_DESDE_ANIO_MES("
                + anio + ","
                + mes + ", "
                + "13"
                + ");";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetData = new JSONArray();
        LinkedList<Object[]> fechas = new LinkedList();
        try {
            while (rs.next()) {
                Object[] datos = new Object[4];
                datos[0] = (String) rs.getString("MESANIO");
                datos[1] = (String) rs.getString("FECHAINI");
                datos[2] = (String) rs.getString("FECHAFIN");
                datos[3] = (int) 0;
                fechas.add(datos);
            }

        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ReportesController.consumoClienteRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();

        mesini = fechas.get(0)[1].toString();
        mesfin = fechas.get(fechas.size() - 1)[2].toString();
        FilaNormal[] filas = etl.ETL.getDatasetRemarcador(entrada.getInt("numremarcador"), mesini, mesfin);
        String mesanioActual = "";
        String iniMes = "";
        String finMes = "";
        double lecturaIniMes = 0.0d;
        double lecturaFinMes = 0.0d;
        LinkedList<Object[]> fechasLecturas = new LinkedList();

        for (int i = 0; i < filas.length; i++) {
            if (!filas[i].fecha.substring(0, 7).equals(mesanioActual) && i == 0) {
                lecturaIniMes = filas[i].lecturaproyectada;
                iniMes = filas[i].fecha;
                mesanioActual = filas[i].fecha.substring(0, 7);
            }
            if ((!filas[i].fecha.substring(0, 7).equals(mesanioActual)) && i > 0 && i < filas.length - 1) {
                finMes = filas[i - 1].fecha;
                lecturaFinMes = filas[i - 1].lecturaproyectada;
                Object[] fecha = new Object[4];
                fecha[0] = mesanioActual;
                fecha[1] = iniMes;
                fecha[2] = finMes;
                fecha[3] = (lecturaFinMes - lecturaIniMes);
                fechasLecturas.add(fecha);
                mesanioActual = filas[i].fecha.substring(0, 7);

                iniMes = filas[i].fecha;
                lecturaIniMes = filas[i].lecturaproyectada;
            }
            if (i == filas.length - 1) {
                finMes = filas[i].fecha;
                lecturaFinMes = filas[i].lecturaproyectada;
                Object[] fecha = new Object[4];
                fecha[0] = mesanioActual;
                fecha[1] = iniMes;
                fecha[2] = finMes;
                fecha[3] = (lecturaFinMes - lecturaIniMes);
                fechasLecturas.add(fecha);
            }
        }
        for (int i = 0; i < fechas.size(); i++) {
            for (int x = 0; x < fechasLecturas.size(); x++) {
                if (fechas.get(i)[0].equals(fechasLecturas.get(x)[0])) {
                    fechas.get(i)[3] = fechasLecturas.get(x)[3];
                }
            }
        }

        for (Object[] fecs : fechas) {
            System.out.println(fecs[1] + " - " + fecs[2] + ": " + fecs[3]);
            labels.put(Util.invertirFecha(fecs[2].toString()));
            datasetData.put(Double.parseDouble(fecs[3].toString()));
        }

        JSONObject dataset = new JSONObject();
        dataset.put("data", datasetData);
        dataset.put("label", "Remarcador ID: " + entrada.getInt("numremarcador"));
        dataset.put("borderWidth", "2");
        datasets.put(dataset);
        JSONObject data = new JSONObject();
        data.put("labels", labels);
        data.put("datasets", datasets);
        salida.put("data", data);
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject resumenMesCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        //Obtener Labels (Días del mes en curso) -------------------------------
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetsdemandas = new JSONArray();
        JSONObject data = new JSONObject();
        JSONObject datademandas = new JSONObject();
        String query = "";
        Conexion c = new Conexion();
        LinkedList<Integer> ides = new LinkedList();

        //Obtener Remarcadores asociados al cliente ----------------------------
        query = "CALL SP_GET_REMARCADORES_IDCLIENTE_IDINSTALACION("
                + "" + entrada.getInt("idcliente") + ", "
                + "" + (entrada.getInt("idinstalacion") == 0 ? "NULL" : entrada.getInt("idinstalacion")) + ""
                + ")";
        System.out.println(query);
        c = new Conexion();
        c.abrir();

        ResultSet rs = c.ejecutarQuery(query);
        JSONArray resumenesConsumo = new JSONArray();
        JSONArray resumenesPotencia = new JSONArray();
        try {
            //Por cada remarcador ir a buscar el dataset
            while (rs.next()) {
                //JSONObject dataset = getDatasetRemarcadorMes(rs.getInt("NUMREMARCADOR"), entrada.getInt("mes"), entrada.getInt("anio"));
                JSONObject dataset = modelo.Lectura.getDatasetMesRemarcadorNumremarcador(rs.getInt("NUMREMARCADOR"), entrada.getInt("anio"), entrada.getInt("mes"));
                resumenesConsumo.put(dataset.getJSONObject("resumen"));
                JSONObject datasetdemandas = modelo.Lectura.getDatasetPotenciasMesRemarcadorNumremarcador(rs.getInt("NUMREMARCADOR"), entrada.getInt("anio"), entrada.getInt("mes"));
                resumenesPotencia.put(datasetdemandas.getJSONObject("resumen"));
                labels = dataset.getJSONArray("labels");
                datasets.put(dataset);
                datasetsdemandas.put(datasetdemandas.getJSONObject("datasetdemanda"));
                datasetsdemandas.put(datasetdemandas.getJSONObject("datasethpunta"));
                ides.add(rs.getInt("NUMREMARCADOR"));
            }

            data.put("labels", labels);
            data.put("datasets", datasets);
            datademandas.put("labels", labels);
            datademandas.put("datasets", datasetsdemandas);

            //String tablaresumen = tablaResumenMesRemarcadores(ides, entrada.getInt("mes"), entrada.getInt("anio"));
            String tablaresumen = tablaResumenMesRemarcadores(resumenesConsumo, resumenesPotencia, entrada.getInt("mes"), entrada.getInt("anio"));

            data.put("tablaresumen", tablaresumen);

            salida.put("data", data);
            salida.put("datademandas", datademandas);
            //salida.put("datademandas", datasetsdemandas);
            //System.out.println(salida);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ReportesController.resumenMesCliente().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject resumenMesRemarcadoresEmpalme(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONObject data = new JSONObject();
        JSONArray remarcadores = entrada.getJSONArray("remarcadores");
        Iterator i = remarcadores.iterator();

        while (i.hasNext()) {
            //String query = "CALL SP_GET_LECTURAS_MES_CIRCUTOR";
            JSONObject remarcador = (JSONObject) i.next();
            int numremarcador = remarcador.getInt("numremarcador");
            //JSONObject d = getDatasetRemarcadorMes(numremarcador, entrada.getInt("mes"), entrada.getInt("anio"));
            JSONObject dataset = modelo.Lectura.getDatasetMesRemarcadorNumremarcador(numremarcador, entrada.getInt("anio"), entrada.getInt("mes"));
            //JSONObject dataset = d.getJSONObject("dataset");
            labels = dataset.getJSONArray("labels");
            //dataset.put("label", "Remarcador ID: " + numremarcador);
            //dataset.put("borderWidth", "2");
            datasets.put(dataset);
            System.out.println(dataset);
        }
        data.put("labels", labels);
        data.put("datasets", datasets);
        System.out.println(data);
        salida.put("data", data);
        salida.put("estado", "ok");
        return salida;
    }
    
    private String tablaResumenMesRemarcadores(JSONArray resumenesConsumo, JSONArray resumenesPotencia, int mes, int anio) {
        String tablasalida = ""
                + "<table class='table table-sm small table-condensed table-bordered' style='font-size: 10px;'>"
                + "<thead>"
                + "<tr>"
                + "<th style='text-align:center;' >Remarcador</th>"
                + "<th style='text-align:center;'>Lectura Inicial</th>"
                + "<th style='text-align:center;'>Lectura Final</th>"
                + "<th style='text-align:center;'>Consumo (kWh)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />Leída (KW)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />H. Punta (KW)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />6 meses (KW)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />12 meses (KW)</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";
        boolean haymanual = false;
        boolean haymanualini = false;
        boolean almenosUnManual = false;
        
        Iterator i = resumenesConsumo.iterator();
        int arrcont = 0;
        while(i.hasNext()) {
            JSONObject resumen = (JSONObject)i.next();
            boolean inimanual = resumen.getBoolean("lecturainimanual");
            boolean finmanual = resumen.getBoolean("lecturafinmanual");
            if(inimanual || finmanual){
                almenosUnManual = true;
            }
            Integer idrem = resumen.getInt("numremarcador");
            Double lecturaini = resumen.getDouble("lecturaini");
            Double lecturafin = resumen.getDouble("lecturafin");
            Double consumo = resumen.getDouble("consumototalmes");
            
            Double demmax = resumenesPotencia.getJSONObject(arrcont).getDouble("maxdemandaleida");
            Double demmaxhp = resumenesPotencia.getJSONObject(arrcont).getDouble("maxdemandahpunta");
            
            Conexion conn = new Conexion();
            String querydem = "CALL SP_GET_MAXDEM_6_12_MESES(" + idrem + ", " + mes + ", " + anio + ")";
            System.out.println("Query demandas 6 y 12 meses: " + querydem);
            conn.abrir();
            double maxdem6 = 0.0d;
            double maxdem12 = 0.0d;
            ResultSet resset = conn.ejecutarQuery(querydem);
            try {
                while (resset.next()) {
                    maxdem6 = resset.getDouble("MAXDEM6");
                    maxdem12 = resset.getDouble("MAXDEM12");
                }
            } catch (SQLException ex) {
                System.out.println("No se pueden obtener las demandas máximas de los últimos 6 y 12 meses.");
                System.out.println(ex);
                ex.printStackTrace();
            }
            conn.cerrar();

            DecimalFormat formato = new DecimalFormat("#.##");
            tablasalida += "<tr>";
            tablasalida += "<td style='text-align:center;'>" + idrem + "</td>";
            tablasalida += "<td style='text-align:right;'>" + Util.formatMiles(lecturaini) + " " + (inimanual ? "*" : "") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + Util.formatMiles(lecturafin) + " " + (finmanual ? "*" : "") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + Util.formatMiles(consumo) + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(demmax).replace(".", ",") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(demmaxhp).replace(".", ",") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(maxdem6).replace(".", ",") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(maxdem12).replace(".", ",") + "</td>";
            tablasalida += "</tr>";
            inimanual = false;
            finmanual = false;
            arrcont ++;
        }
        tablasalida += "</tbody>";
        tablasalida += "</table>";
        if (almenosUnManual) {
            tablasalida += "* La lectura fue ingresada de forma manual.";
        }

        return tablasalida;
    }

    private String tablaResumenMesRemarcadoresOld(LinkedList<Integer> ides, int mes, int anio) {
        String tablasalida = ""
                + "<table class='table table-sm small table-condensed table-bordered' style='font-size: 10px;'>"
                + "<thead>"
                + "<tr>"
                + "<th style='text-align:center;' >Remarcador</th>"
                + "<th style='text-align:center;'>Lectura Inicial</th>"
                + "<th style='text-align:center;'>Lectura Final</th>"
                + "<th style='text-align:center;'>Consumo (kWh)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />Leída (KW)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />H. Punta (KW)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />6 meses (KW)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />12 meses (KW)</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";
        boolean haymanual = false;
        boolean haymanualini = false;
        boolean almenosUnManual = false;
        for (int i = 0; i < ides.size(); i++) {
            FilaNormal[] tabla = etl.ETL.getDatasetRemarcador(ides.get(i), mes, anio);
            int idrem = ides.get(i);

            int lecturaini = (int) tabla[0].lecturareal;
            if (tabla[0].esmanual) {
                lecturaini = (int) tabla[0].lecturamanual;
                haymanualini = true;
                almenosUnManual = true;
            }
            int lecturafin = (int) tabla[tabla.length - 1].lecturareal;
            if (tabla[tabla.length - 1].esmanual) {
                lecturafin = tabla[tabla.length - 1].lecturamanual;
                haymanual = true;
                almenosUnManual = true;
            }
            int lecturaproyectadaini = (int) tabla[0].lecturaproyectada;
            int lecturaproyectadafin = (int) tabla[tabla.length - 1].lecturaproyectada;
            int consumo = lecturaproyectadafin - lecturaproyectadaini;
            double demmax = 0;
            double demmaxhp = 0;
            for (FilaNormal f : tabla) {
                if (f.potencia > demmax) {
                    demmax = f.potencia;
                }
                if (Integer.parseInt(f.hora.substring(0, 2)) >= 18 && Integer.parseInt(f.hora.substring(0, 2)) <= 23) {
                    if (f.potencia > demmaxhp) {
                        demmaxhp = f.potencia;
                    }
                }
            }

            Conexion conn = new Conexion();
            String querydem = "CALL SP_GET_MAXDEM_6_12_MESES(" + idrem + ", " + mes + ", " + anio + ")";
            System.out.println("Query demandas 6 y 12 meses: " + querydem);
            conn.abrir();
            double maxdem6 = 0.0d;
            double maxdem12 = 0.0d;
            ResultSet resset = conn.ejecutarQuery(querydem);
            try {
                while (resset.next()) {
                    maxdem6 = resset.getDouble("MAXDEM6");
                    maxdem12 = resset.getDouble("MAXDEM12");
                }
            } catch (SQLException ex) {
                System.out.println("No se pueden obtener las demandas máximas de los últimos 6 y 12 meses.");
                System.out.println(ex);
                ex.printStackTrace();
            }
            conn.cerrar();

            DecimalFormat formato = new DecimalFormat("#.##");
            tablasalida += "<tr>";
            tablasalida += "<td style='text-align:center;'>" + idrem + "</td>";
            tablasalida += "<td style='text-align:right;'>" + Util.formatMiles(lecturaini) + " " + (haymanualini ? "*" : "") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + Util.formatMiles(lecturafin) + " " + (haymanual ? "*" : "") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + Util.formatMiles(consumo) + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(demmax).replace(".", ",") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(demmaxhp).replace(".", ",") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(maxdem6).replace(".", ",") + "</td>";
            tablasalida += "<td style='text-align:right;'>" + formato.format(maxdem12).replace(".", ",") + "</td>";
            tablasalida += "</tr>";
            haymanual = false;
            haymanualini = false;
        }
        tablasalida += "</tbody>";
        tablasalida += "</table>";
        if (almenosUnManual) {
            tablasalida += "* La lectura fue ingresada de forma manual.";
        }

        return tablasalida;
    }

}
