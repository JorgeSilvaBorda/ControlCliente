package controlador;

import clases.json.JSONArray;
import clases.json.JSONException;
import clases.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Conexion;

public class ReportesController extends HttpServlet {

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
        String query = "CALL SP_GET_ULTIMOS_100_REGISTROS_REMARCADOR(" + entrada.getInt("idremarcador") + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        JSONArray labels = new JSONArray();
        JSONArray datasets = new JSONArray();
        JSONArray datasetData = new JSONArray();

        try {
            while (rs.next()) {
                labels.put(rs.getString("TIMESTAMP"));
                datasetData.put(rs.getInt("EAC_KVAH"));
            }

            JSONObject dataset = new JSONObject();
            dataset.put("data", datasetData);
            dataset.put("label", "Remarcador Nº: " + entrada.getInt("idremarcador"));
            dataset.put("borderColor", "#3e95cd");
            dataset.put("pointRadius", 1);
            dataset.put("pointHoverRadius", 2);
            dataset.put("fill", "false");
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
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                labels.put(rs.getString("FECHA"));
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
        c = new Conexion();
        c.abrir();

        rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                JSONObject dataset = getDataSetPorRemarcador(rs.getInt("NUMREMARCADOR"));
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

    private JSONObject getDataSetPorRemarcador(int numremarcador) {
        JSONObject dataset = new JSONObject();

        dataset.put("label", "Nº: " + numremarcador);
        //dataset.put("borderColor", "#3e95cd");
        dataset.put("pointRadius", 2);
        dataset.put("pointHoverRadius", 3);
        dataset.put("fill", "false");

        JSONArray data = new JSONArray();
        String query = "CALL SP_GET_CONSUMO_MES_ACTUAL_REMARCADOR(" + numremarcador + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                data.put(rs.getInt("EAC_KVAH"));
            }
            dataset.put("data", data);
        } catch (JSONException | SQLException ex) {
            System.out.println("No se pudo obtener la data para el remarcador " + numremarcador);
            System.out.println(ex);
        }
        c.cerrar();
        return dataset;
    }
}
