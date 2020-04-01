package controlador;

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

public class TarifaController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-tarifas":
                out.print(getTarifas());
                break;
            case "ins-tarifa":
                out.print(insTarifa(entrada));
                break;
            case "get-tarifa-idtarifa":
                out.print(getTarifaIdTarifa(entrada));
                break;
            case "upd-tarifa":
                out.print(updTarifa(entrada.getJSONObject("tarifa")));
                break;
        }
    }

    private JSONObject getTarifas() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_TARIFAS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDTARIFA") + "' /><span>" + rs.getString("NOMTARIFA") + "</span></td>";
                filas += "<td><span>" + rs.getString("VALORTARIFA") + "</span></td>";
                filas += "<td><button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.TarifaController.getTarifas().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject insTarifa(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_TARIFA("
                + "'" + entrada.getString("nomtarifa") + "',"
                + "" + entrada.getInt("valortarifa") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getTarifaIdTarifa(JSONObject entrada) {
        int idtarifa = entrada.getInt("idtarifa");
        JSONObject salida = new JSONObject();
        JSONObject tarifa = new JSONObject();
        String query = "CALL SP_GET_TARIFA_IDTARIFA(" + idtarifa + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                tarifa.put("idtarifa", rs.getInt("IDTARIFA"));
                tarifa.put("nomtarifa", rs.getString("NOMTARIFA"));
                tarifa.put("valortarifa", rs.getInt("VALORTARIFA"));
            }
            salida.put("tarifa", tarifa);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.TarifaController.getTarifaIdTarifa().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject updTarifa(JSONObject tarifa) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_TARIFA("
                + tarifa.getInt("idtarifa") + ","
                + "'" + tarifa.getString("nomtarifa") + "',"
                + tarifa.getInt("valortarifa") + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }
}
