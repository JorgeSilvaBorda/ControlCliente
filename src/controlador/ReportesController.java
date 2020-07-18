package controlador;

import clases.json.JSONArray;
import clases.json.JSONException;
import clases.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Conexion;
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

        String query = "CALL SP_GET_CONSUMO_13_MESES_REMARCADOR(" + entrada.getInt("numremarcador") + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetData = new JSONArray();

        try {
            while (rs.next()) {
                labels.put(Util.invertirFecha(rs.getString("FECHA")));
                datasetData.put(rs.getInt("CONSUMO"));
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
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ReportesController.consumoClienteRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject consumoClienteRemarcadorAnioMes(JSONObject entrada) {
        JSONObject salida = new JSONObject();

        String query = "CALL SP_GET_CONSUMO_13_MESES_REMARCADOR_DESDE("
                + +entrada.getInt("numremarcador") + ", "
                + "'" + entrada.getString("aniomes") + "'"
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetData = new JSONArray();

        try {
            while (rs.next()) {
                labels.put(Util.invertirFecha(rs.getString("FECHA")));
                datasetData.put(rs.getInt("CONSUMO"));
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
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ReportesController.consumoClienteRemarcadorAnioMes().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject resumenMesCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        //Obtener Labels (Días del mes en curso) -------------------------------
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONObject data = new JSONObject();
        String query = "";
        Conexion c = new Conexion();
        LinkedList<Integer> ides = new LinkedList();
        //Obtener Remarcadores asociados al cliente ----------------------------
        //query = "CALL SP_GET_SELECT_REMARCADORES_CLIENTE(" + entrada.getInt("idcliente") + ")";
        query = "CALL SP_GET_REMARCADORES_IDCLIENTE_IDINSTALACION("
                + "" + entrada.getInt("idcliente") + ", "
                + "" +(entrada.getInt("idinstalacion") == 0 ? "NULL" : entrada.getInt("idinstalacion"))+ ""
        + ")";
        System.out.println(query);
        c = new Conexion();
        c.abrir();

        ResultSet rs = c.ejecutarQuery(query);
        try {
            //Por cada remarcador ir a buscar el dataset
            while (rs.next()) {
                JSONObject dataset = getDatasetRemarcadorMes(rs.getInt("NUMREMARCADOR"), entrada.getInt("mes"), entrada.getInt("anio"));
                labels = dataset.getJSONArray("labels");
                datasets.put(dataset.getJSONObject("dataset"));
                ides.add(rs.getInt("NUMREMARCADOR"));
            }
            String tablaresumen = tablaResumenMesRemarcadores(ides, entrada.getInt("mes"), entrada.getInt("anio"));
            data.put("labels", labels);
            data.put("tablaresumen", tablaresumen);
            data.put("datasets", datasets);
            salida.put("data", data);
            System.out.println(salida);
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

    private String tablaResumenMesRemarcadores(LinkedList<Integer> ides, int mes, int anio) {
        String where = "(";
        for (int i = 0; i < ides.size(); i++) {
            if (i == ides.size() - 1) {
                where += ides.get(i) + ")";
            } else {
                where += ides.get(i) + ", ";
            }
        }
        String query = "SELECT "
                + "REMARCADOR_ID, "
                + "LECTURA_INICIAL, "
                + "LECTURA_FINAL, "
                + "(LECTURA_FINAL - LECTURA_INICIAL) CONSUMO, "
                + "CAST(ROUND(MAX_DEMANDA_LEIDA, 2) AS CHAR) MAX_DEMANDA_LEIDA, "
                + "CAST(ROUND(MAX_DEMANDA_HORA_PUNTA, 2) AS CHAR) MAX_DEMANDA_HORA_PUNTA "
                + "FROM "
                + "LECTURAS "
                + "WHERE "
                + "YEAR(FECHA_LECTURA_FINAL) = " + anio + " "
                + "AND MONTH(FECHA_LECTURA_FINAL) = " + mes + " "
                + "AND REMARCADOR_ID IN" + where;
        System.out.println(query);
        Conexion c = new Conexion();
        String tabla = ""
                + "<table class='table table-sm small table-condensed table-bordered' style='font-size: 10px;'>"
                + "<thead>"
                + "<tr>"
                + "<th style='text-align:center;' >Remarcador</th>"
                + "<th style='text-align:center;'>Lectura Inicial</th>"
                + "<th style='text-align:center;'>Lectura Final</th>"
                + "<th style='text-align:center;'>Consumo (kWh)</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />Leída</th>"
                + "<th style='text-align:center;'>Demanda Máx.<br />H. Punta</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                tabla += "<tr>";
                tabla += "<td style='text-align:center;'>" + rs.getInt("REMARCADOR_ID") + "</td>";
                tabla += "<td style='text-align:right;'>" + Util.formatMiles(rs.getInt("LECTURA_INICIAL")) + "</td>";
                tabla += "<td style='text-align:right;'>" + Util.formatMiles(rs.getInt("LECTURA_FINAL")) + "</td>";
                tabla += "<td style='text-align:right;'>" + Util.formatMiles(rs.getInt("CONSUMO")) + "</td>";
                tabla += "<td style='text-align:right;'>" + rs.getString("MAX_DEMANDA_LEIDA").replace(".", ",") + "</td>";
                tabla += "<td style='text-align:right;'>" + rs.getString("MAX_DEMANDA_HORA_PUNTA").replace(".", ",") + "</td>";
                tabla += "</tr>";
            }
            tabla += "</tbody>";
            tabla += "</table>";
        } catch (Exception ex) {
            System.out.println("No se puede obtener la tabla resumen de consumo de los remarcadores del cliente.");
            System.out.println(ex);
            tabla = "";
        }
        c.cerrar();
        return tabla;
    }

    private JSONObject resumenMesRemarcadoresEmpalme(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONObject data = new JSONObject();
        JSONArray remarcadores = entrada.getJSONArray("remarcadores");
        Iterator i = remarcadores.iterator();

        while (i.hasNext()) {
            JSONObject remarcador = (JSONObject) i.next();
            int numremarcador = remarcador.getInt("numremarcador");
            JSONObject d = getDatasetRemarcadorMes(numremarcador, entrada.getInt("mes"), entrada.getInt("anio"));
            JSONObject dataset = d.getJSONObject("dataset");
            labels = d.getJSONArray("labels");
            dataset.put("label", "Remarcador ID: " + numremarcador);
            dataset.put("borderWidth", "2");
            datasets.put(dataset);
            System.out.println(dataset);
        }
        data.put("labels", labels);
        data.put("datasets", datasets);
        //System.out.println(data);
        salida.put("data", data);
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getDatasetRemarcadorMes(int numremarcador, int mes, int anio) {
        JSONObject dataset = new JSONObject();
        JSONObject salida = new JSONObject();
        JSONArray data = new JSONArray();
        JSONArray labels = new JSONArray();
        String query = "CALL SP_GET_DATASET_REMARCADOR_MES_ANIO(" + numremarcador + ", " + mes + ", " + anio + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        System.out.println(query);
        try {
            while (rs.next()) {
                data.put(rs.getInt("CONSUMO"));
                labels.put(rs.getDate("FECHA"));
            }
            dataset.put("data", data);
            dataset.put("label", "Remarcador ID: " + numremarcador);
            salida.put("dataset", dataset);
            salida.put("labels", labels);
        } catch (JSONException | SQLException ex) {
            System.out.println("No se pudo obtener la data para el remarcador " + numremarcador);
            System.out.println(ex);
        }
        c.cerrar();
        return salida;
    }

    @Deprecated
    private JSONObject getDataSetPorRemarcador(int numremarcador, LinkedList<String> fechas) {
        JSONObject dataset = new JSONObject();

        dataset.put("label", "Remarcador ID: " + numremarcador);
        dataset.put("borderWidth", "2");

        JSONArray data = new JSONArray();

        for (String fecha : fechas) {
            String query = "SELECT FN_GET_CONSUMO_DIA_REMARCADOR(" + numremarcador + ", '" + fecha + "') AS CONSUMO";
            Conexion c = new Conexion();
            c.abrir();
            ResultSet rs = c.ejecutarQuery(query);
            try {
                while (rs.next()) {
                    data.put(rs.getInt("CONSUMO"));
                }
                dataset.put("data", data);
            } catch (JSONException | SQLException ex) {
                System.out.println("No se pudo obtener la data para el remarcador " + numremarcador);
                System.out.println(ex);
            }
            c.cerrar();
        }

        return dataset;
    }
}
