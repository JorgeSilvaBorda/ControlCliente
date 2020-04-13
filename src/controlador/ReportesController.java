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
        }
    }
    
    private JSONObject consumoClienteRemarcador(JSONObject entrada){
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
}
