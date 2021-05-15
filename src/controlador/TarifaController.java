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
import modelo.Util;

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
            case "get-select-anio-tarifa":
                out.print(getSelectAnioTarifa());
                break;
            case "get-select-tarifas-idcomuna":
                out.print(getSelectTarifasIdComuna(entrada));
                break;
            case "get-select-tarifas-idinstalacion":
                out.print(getSelectTarifasIdInstalacion(entrada));
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
            case "get-hist-tarifa-anio":
                out.print(getHistTarifaAnio(entrada));
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

    private JSONObject getSelectAnioTarifa() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_ANIO_TARIFA()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione Año", "ANIO", "ANIO");
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

    private JSONObject getSelectTarifasIdInstalacion(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_SELECT_TARIFAS_IDINSTALACION(" + entrada.getInt("idinstalacion") + ")";
        Conexion c = new Conexion();
        System.out.println(query);
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

    private JSONObject getHistTarifaAnio(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int anio = entrada.getInt("anio");
        int filastotales = 0;
        String query = "CALL SP_GET_HIST_TARIFA_ANIO("
                + anio
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String tabla = "";
        tabla += "<table id='tabla-hist-tarifa' class='table table-sm table-condensed table-bordered table-striped table-hover' style='font-size: 10px;'>";
        tabla += "<thead style='text-align: center; vertical-align: middle;'>";
        tabla += "<tr>";
        tabla += "<th style='vertical-align: middle;'>Nombre</th>";
        tabla += "<th style='vertical-align: middle;'>Comuna</th>";
        tabla += "<th style='vertical-align: middle;'>Cargo<br />Fijo</th>";
        tabla += "<th style='vertical-align: middle;'>Cargo por<br />Servicio Público</th>";
        tabla += "<th style='vertical-align: middle;'>Transporte<br />Electricidad</th>";
        tabla += "<th style='vertical-align: middle;'>Cargo<br />Energía</th>";

        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Leída H. Punta (BTAA)</th>";
        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Leída H. Punta (BTAS)</th>";
        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Leída H. Punta (BTSA)</th>";
        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Leída H. Punta (BTSS)</th>";

        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Potencia (BTAA)</th>";
        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Potencia (BTAS)</th>";
        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Potencia (BTSA)</th>";
        tabla += "<th style='vertical-align: middle;'>Dem. Máx.<br />Potencia (BTSS)</th>";

        tabla += "<th style='vertical-align: middle;'>Acción</th>";
        tabla += "<th style='vertical-align: middle; min-width: 65px;'>Fecha</th>";
        tabla += "<th style='vertical-align: middle;'>Hora</th>";
        tabla += "</tr>";
        tabla += "</thead>";
        tabla += "<tbody>";
        try {
            while (rs.next()) {
                tabla += "<tr>";
                tabla += "<td>" + rs.getString("NOMTARIFA") + "</td>";
                tabla += "<td>" + rs.getString("NOMCOMUNA") + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CARGOFIJO")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CARGOSERVICIOPUBLICO")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("TRANSPORTEELECTRICIDAD")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CARGOENERGIA")) + "</td>";

                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPLHPBTAA")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPLHPBTAS")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPLHPBTSA")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPLHPBTSS")) + "</td>";

                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPSBTAA")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPSBTAS")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPSBTSA")) + "</td>";
                tabla += "<td style='text-align: right;'>" + Util.formatMiles(rs.getBigDecimal("CDMPSBTSS")) + "</td>";

                tabla += "<td style='text-align: left;'>" + rs.getString("ACCION") + "</td>";
                tabla += "<td style='text-align: center;'>" + Util.invertirFecha(rs.getString("FECHA")) + "</td>";
                tabla += "<td style='text-align: center;'>" + rs.getString("HORA") + "</td>";
                tabla += "</tr>";

                filastotales++;
            }
            tabla += "</tbody>";
            tabla += "</table>";
            if (filastotales > 0) {
                tabla = "<button type='button' onclick='exportExcel(\"tabla-hist-tarifa\");' class='btn btn-success btn-sm' >Excel</button><br /><br />" + tabla;
            }
            salida.put("tabla", tabla);
            salida.put("estado", "ok");
        } catch (Exception ex) {
            salida.put("estado", "error");
            System.out.println("No se puede obtener el histórico de tarifas.");
            System.out.println(ex);
        }
        return salida;
    }
}
