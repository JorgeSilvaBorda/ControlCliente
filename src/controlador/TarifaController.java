package controlador;

import clases.json.JSONArray;
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
            case "get-select-tarifas":
                out.print(getSelectTarifas());
                break;
            case "get-select-tarifas-idcomuna":
                out.print(getSelectTarifasIdComuna(entrada));
                break;
            case "get-detalle-tarifa":
                out.print(getDetalleTarifa(entrada));
                break;
            case "ins-tarifa":
                out.print(insTarifa(entrada));
                break;
            case "upd-tarifa":
                out.print(updTarifa(entrada));
                break;
        }
    }

    private JSONObject getSelectTarifas() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_TARIFAS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDTARIFA", "NOMBRESELECT");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject getSelectTarifasIdComuna(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_SELECT_TARIFAS_IDCOMUNA(" + entrada.getInt("idcomuna") + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDTARIFA", "NOMBRESELECT");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject getDetalleTarifa(JSONObject entrada) {
        int idtarifa = entrada.getInt("idtarifa");
        JSONObject salida = new JSONObject();
        JSONObject tarifa = new JSONObject();
        JSONArray conceptos = new JSONArray();
        tarifa.put("idtarifa", idtarifa);
        String query = "CALL SP_GET_DETALLE_TARIFA("
                + idtarifa + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                tarifa.put("nomtarifa", rs.getString("NOMTARIFA"));
                tarifa.put("nomcomuna", rs.getString("NOMCOMUNA"));
                tarifa.put("idcomuna", rs.getInt("IDCOMUNA"));

                JSONObject concepto = new JSONObject();
                concepto.put("idconcepto", rs.getInt("IDCONCEPTO"));
                concepto.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                concepto.put("umedida", rs.getString("UMEDIDA"));
                concepto.put("idred", rs.getInt("IDRED"));
                concepto.put("nomred", rs.getString("NOMRED"));
                concepto.put("nomred", rs.getString("NOMRED"));
                concepto.put("valorneto", rs.getBigDecimal("VALORNETO"));
                conceptos.put(concepto);
            }
            tarifa.put("conceptos", conceptos);
            salida.put("estado", "ok");
            salida.put("tarifa", tarifa);
        } catch (Exception ex) {
            System.out.println("Problemas en controlador.TarifaController.getDetalleTarifa().");
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

        JSONObject tarifa = entrada.getJSONObject("tarifa");
        if (existeTarifa(tarifa) > 0) {
            salida.put("estado", "existe");
            return salida;
        }
        String query = "CALL SP_INS_TARIFA("
                + "'" + tarifa.getString("nomtarifa") + "', "
                + tarifa.getInt("idcomuna") + ", "
                + tarifa.getBigDecimal("cargofijo") + ", "
                + tarifa.getBigDecimal("cargoserviciopublico") + ", "
                + tarifa.getBigDecimal("transporteelectricidad") + ", "
                + tarifa.getBigDecimal("cargoenergia") + ", "
                + tarifa.getBigDecimal("cdmplhpbtaa") + ", "
                + tarifa.getBigDecimal("cdmplhpbtas") + ", "
                + tarifa.getBigDecimal("cdmplhpbtsa") + ", "
                + tarifa.getBigDecimal("cdmplhpbtss") + ", "
                + tarifa.getBigDecimal("cdmpsbtaa") + ", "
                + tarifa.getBigDecimal("cdmpsbtas") + ", "
                + tarifa.getBigDecimal("cdmpsbtsa") + ", "
                + tarifa.getBigDecimal("cdmpsbtss") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);

        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject updTarifa(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        JSONObject newtarifa = entrada.getJSONObject("newtarifa");
        JSONObject tarifa = entrada.getJSONObject("tarifa");

        if (existeTarifaUpdate(tarifa, newtarifa) > 0) {
            salida.put("estado", "existe");
            return salida;
        }

        String query = "CALL SP_UPD_TARIFA("
                + tarifa.getInt("idtarifa") + ", "
                + "'" + newtarifa.getString("nomtarifa") + "', "
                + newtarifa.getInt("idcomuna") + ", "
                + newtarifa.getBigDecimal("cargofijo") + ", "
                + newtarifa.getBigDecimal("cargoserviciopublico") + ", "
                + newtarifa.getBigDecimal("transporteelectricidad") + ", "
                + newtarifa.getBigDecimal("cargoenergia") + ", "
                + newtarifa.getBigDecimal("cdmplhpbtaa") + ", "
                + newtarifa.getBigDecimal("cdmplhpbtas") + ", "
                + newtarifa.getBigDecimal("cdmplhpbtsa") + ", "
                + newtarifa.getBigDecimal("cdmplhpbtss") + ", "
                + newtarifa.getBigDecimal("cdmpsbtaa") + ", "
                + newtarifa.getBigDecimal("cdmpsbtas") + ", "
                + newtarifa.getBigDecimal("cdmpsbtsa") + ", "
                + newtarifa.getBigDecimal("cdmpsbtss") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);

        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private int existeTarifa(JSONObject tarifa) {
        int cantidad = 0;
        String query = "CALL SP_EXISTE_TARIFA("
                + "'" + tarifa.getString("nomtarifa") + "', "
                + tarifa.getInt("idcomuna") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                cantidad = rs.getInt("CANTIDAD");
            }

        } catch (SQLException ex) {
            System.out.println("Problemas en controlador.TarifaController.existeTarifa().");
            System.out.println(ex);
            ex.printStackTrace();
            return -1;
        }
        return cantidad;
    }

    private int existeTarifaUpdate(JSONObject tarifa, JSONObject newtarifa) {
        int cantidad = 0;
        String query = "CALL SP_EXISTE_TARIFA_UPDATE("
                + tarifa.getInt("idtarifa") + ", "
                + "'" + newtarifa.getString("nomtarifa") + "', "
                + newtarifa.getInt("idcomuna") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                cantidad = rs.getInt("CANTIDAD");
            }

        } catch (SQLException ex) {
            System.out.println("Problemas en controlador.TarifaController.existeTarifa().");
            System.out.println(ex);
            ex.printStackTrace();
            return -1;
        }
        return cantidad;
    }
}
