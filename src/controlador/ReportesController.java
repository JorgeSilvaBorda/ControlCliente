package controlador;

import clases.json.JSONArray;
import clases.json.JSONException;
import clases.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            case "resumen-mes-cliente":
                out.print(resumenMesCliente(entrada));
                break;
        }
    }

    private JSONObject consumoClienteRemarcador(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        //Obtener Labels remarcadores
        String query = "CALL SP_GET_CONSUMO_13_MESES_REMARCADOR(" + entrada.getInt("numremarcador") + ", NOW())";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetData = new JSONArray();

        try {
            while (rs.next()) {
                labels.put(rs.getString("FECHA"));
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

    private JSONObject resumenMesCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        //Obtener Labels (Días del mes en curso) -------------------------------
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONObject data = new JSONObject();

        String query = "CALL SP_GET_DIAS_MES_CURSO()";
        System.out.println(query);
        LinkedList<String> fechas = new LinkedList();
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                labels.put(Util.invertirFecha(rs.getString("FECHA")));
                fechas.add(rs.getString("FECHA"));
            }
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ReportesController.resumenMesCliente().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();

        //Obtener Remarcadores asociados al cliente ----------------------------
        query = "CALL SP_GET_SELECT_REMARCADORES_CLIENTE(" + entrada.getInt("idcliente") + ")";
        System.out.println(query);
        c = new Conexion();
        c.abrir();

        rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                JSONObject dataset = getDataSetPorRemarcador(rs.getInt("NUMREMARCADOR"), fechas);
                datasets.put(dataset);
            }
            data.put("labels", labels);
            data.put("datasets", datasets);
            salida.put("data", data);
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
                    JSONObject dato = new JSONObject();
                    dato.put(fecha, rs.getInt("CONSUMO"));
                    //data.put(dato);
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
