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
            case "ins-tarifa-concepto":
                out.print(insTarifaConcepto(entrada));
                break;
            case "get-tarifa-idtarifa":
                out.print(getTarifaIdTarifa(entrada));
                break;
            case "get-concepto-idconcepto":
                out.print(getConceptoIdConcepto(entrada));
                break;
            case "upd-tarifa":
                out.print(updTarifa(entrada.getJSONObject("tarifa")));
                break;
            case "upd-concepto":
                out.print(updConcepto(entrada.getJSONObject("concepto")));
                break;
            case "get-select-tarifas":
                out.print(getSelectTarifas());
                break;
            case "get-tarifas-conceptos":
                out.print(getTarifasConceptos());
                break;
            case "del-concepto":
                out.print(delConcepto(entrada));
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

    private JSONObject getTarifasConceptos() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_TARIFAS_CONCEPTOS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDCONCEPTO") + "' /><span>" + rs.getString("NOMCONCEPTO") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDTARIFA") + "' /><span>" + rs.getString("NOMTARIFA") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDCOMUNA") + "' /><span>" + rs.getString("NOMCOMUNA") + "</span></td>";
                filas += "<td><span>" + rs.getString("UNIDADMEDIDA") + "</span></td>";
                filas += "<td><span>" + rs.getString("VALORNETO") + "</span></td>";
                filas += "<td style='width:12%'>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicionConcepto(this)'>Editar</button>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-danger' onclick='eliminarConcepto(this)'>Eliminar</button>"
                        + "</td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.TarifaController.getTarifasConceptos().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getSelectTarifas() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_TARIFAS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDTARIFA", "NOMTARIFA");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject insTarifa(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_TARIFA("
                + "'" + entrada.getString("nomtarifa") + "'"
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject insTarifaConcepto(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_TARIFA_CONCEPTO("
                + entrada.getInt("idtarifa") + ", "
                + entrada.getInt("idcomuna") + ", "
                + "'" + entrada.getString("nomconcepto") + "', "
                + "'" + entrada.getString("umedida") + "', "
                + entrada.getBigDecimal("valorneto")
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

    private JSONObject getConceptoIdConcepto(JSONObject entrada) {
        int idconcepto = entrada.getInt("idconcepto");
        JSONObject salida = new JSONObject();
        JSONObject concepto = new JSONObject();
        String query = "CALL SP_GET_CONCEPTO_IDCONCEPTO(" + idconcepto + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                concepto.put("idconcepto", rs.getInt("IDCONCEPTO"));
                concepto.put("idtarifa", rs.getInt("IDTARIFA"));
                concepto.put("idcomuna", rs.getInt("IDCOMUNA"));
                concepto.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                concepto.put("umedida", rs.getString("UNIDADMEDIDA"));
                concepto.put("valorneto", rs.getBigDecimal("VALORNETO"));
            }
            salida.put("concepto", concepto);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.TarifaController.getConceptoIdConcepto().");
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
                + "'" + tarifa.getString("nomtarifa") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject updConcepto(JSONObject concepto) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_CONCEPTO("
                + concepto.getInt("idconcepto") + ","
                + concepto.getInt("idtarifa") + ","
                + concepto.getInt("idcomuna") + ","
                + "'" + concepto.getString("nomconcepto") + "',"
                + "'" + concepto.getString("umedida") + "',"
                + "'" + concepto.getBigDecimal("valorneto") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }
    
    private JSONObject delConcepto(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_DEL_CONCEPTO(" + entrada.getInt("idconcepto") + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }
}
