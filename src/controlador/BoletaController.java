package controlador;

import clases.json.JSONArray;
import clases.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.BoletaCliente;
import modelo.Conexion;
import modelo.Remarcador;
import modelo.Util;

public class BoletaController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "armar-pre-boleta-cliente":
                out.print(armarPreBoletaCliente(entrada));
                break;
            case "get-detalle-tarifa-id-red-consumo-remarcador":
                out.print(getDetalleTarifaIdRedConsumoRemarcador(entrada));
                break;
            case "genera-boleta":
                out.print(generaBoleta(entrada));
                break;
            case "get-last-boleta-idboleta":
                out.print(getLastBoleta(entrada));
                break;
        }
    }

    private JSONObject armarPreBoletaCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int idcliente = entrada.getInt("idcliente");
        String fechaini = entrada.getString("fechaini");
        String fechafin = entrada.getString("fechafin");

        BoletaCliente boleta = new BoletaCliente(idcliente, fechaini, fechafin);
        JSONObject bjson = new JSONObject();
        bjson.put("idcliente", entrada.getInt("idcliente"));
        bjson.put("fechaini", entrada.getString("fechaini"));
        bjson.put("fechafin", entrada.getString("fechafin"));
        bjson.put("nomcliente", boleta.getNomcliente());
        bjson.put("nomcliente", boleta.getRazoncliente());
        bjson.put("rutcliente", boleta.getRutcliente());
        bjson.put("dvcliente", boleta.getDvcliente());
        bjson.put("rutfullcliente", Util.formatRut(boleta.getRutcliente() + boleta.getDvcliente()));
        bjson.put("direccion", boleta.getDireccion());
        bjson.put("persona", boleta.getPersona());
        bjson.put("cargo", boleta.getCargo());
        bjson.put("fono", boleta.getFono());
        bjson.put("email", boleta.getEmail());

        JSONArray remarcadores = new JSONArray();

        for (Remarcador r : boleta.getRemarcadores()) {
            JSONObject remarcador = new JSONObject();
            remarcador.put("idremarcador", r.getIdremarcador());
            remarcador.put("idparque", r.getIdparque());
            remarcador.put("idempalme", r.getIdempalme());
            remarcador.put("nomparque", r.getNomparque());
            remarcador.put("numempalme", r.getNumempalme());
            remarcador.put("nominstalacion", r.getNominstalacion());
            remarcador.put("modulos", r.getModulos());
            remarcador.put("idinstalacion", r.getIdinstalacion());
            remarcador.put("diferencia", r.diffperiodo);
            remarcador.put("lecturaactual", r.lecturaactual);
            remarcador.put("lecturaanterior", r.lecturaanterior);
            remarcadores.put(remarcador);
        }
        bjson.put("remarcadores", remarcadores);
        bjson.put("tablaremarcadores", armarTablaRemarcadores(boleta));
        salida.put("estado", "ok");
        salida.put("preboleta", bjson);
        return salida;
    }

    private String armarTablaRemarcadores(BoletaCliente b) {
        if (b.getRemarcadores().size() < 1) {
            return "";
        }
        String tabla = "<h4>Resumen de Consumo por Remarcador</h4>";
        tabla += "<table class='table table-sm table-striped table-hover small'>"
                + "<thead>"
                + "<tr>"
                + "<th>Nº Remarcador</th>"
                + "<th>Dirección</th>"
                + "<th>Lectura Anterior</th>"
                + "<th>Lectura Actual</th>"
                + "<th>Consumo (kWh)</th>"
                + "</thead>"
                + "<tbody>";
        for (Remarcador r : b.getRemarcadores()) {
            tabla += "<tr>";
            tabla += "<td>" + r.getNumremarcador() + "</td>";
            tabla += "<td>" + r.getDireccion() + " " + r.getModulos() + "</td>";
            tabla += "<td>" + r.lecturaanterior + "</td>";
            tabla += "<td>" + r.lecturaactual + "</td>";
            tabla += "<td>" + r.diffperiodo + "</td>";
            tabla += "</tr>";
        }
        tabla += "</tbody></table>";
        return tabla;
    }

    private JSONObject getDetalleTarifaIdRedConsumoRemarcador(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        JSONObject boleta = new JSONObject();
        JSONArray consumos = new JSONArray();
        int idtarifa = entrada.getInt("idtarifa");
        int idred = entrada.getInt("idred");
        int consumo = entrada.getInt("consumo");
        int numremarcador = entrada.getInt("numremarcador");
        int total = 0;
        int exento = 0;
        String query = "CALL SP_GET_DETALLE_TARIFA_IDRED_CONSUMO_REMARCADOR("
                + idtarifa + ", "
                + idred + ", "
                + consumo + ", "
                + numremarcador
                + ")";
        System.out.println(query);
        String tabla = "<thead>"
                + "<tr>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "Cargos"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "Unidad"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "Cantidad"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "$ Unitario"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; '>"
                + "Valores ($)"
                + "</th>"
                + "</tr>"
                + "</thead><tbody>";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filahpunta = "";
        String filapotencia = "";
        try {
            while (rs.next()) {
                JSONObject jsonconsumo = new JSONObject();

                if (rs.getInt("IDCONCEPTO") != 7 && rs.getInt("IDCONCEPTO") != 11) {
                    tabla += "<tr>";
                    tabla += "<td style='font-weight: bold;'>";
                    tabla += rs.getString("NOMCONCEPTO");
                    tabla += "</td>";

                    tabla += "<td style='font-weight: bold;'>";
                    tabla += rs.getString("UMEDIDA");
                    tabla += "</td>";

                    tabla += "<td style='font-weight: bold; text-align: right;'>";
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    tabla += Util.formatMiles(Double.toString(cant));
                    tabla += "</td>";

                    tabla += "<td style='font-weight: bold; text-align: right;'>";
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    tabla += Util.formatMiles(Double.toString(net));
                    tabla += "</td>";

                    tabla += "<td style='text-align: right; '>";
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());
                    tabla += Util.formatMiles(Double.toString(Math.floor(tot)));
                    tabla += "</td>";
                    tabla += "</tr>";

                    if (rs.getInt("IDCONCEPTO") != 2) {
                        total += rs.getInt("TOTAL");
                    } else {
                        exento = rs.getInt("TOTAL");
                        //total += rs.getInt("TOTAL");
                    }

                    jsonconsumo.put("idconcepto", rs.getInt("IDCONCEPTO"));
                    jsonconsumo.put("idred", rs.getInt("IDRED"));
                    jsonconsumo.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                    jsonconsumo.put("nomred", rs.getString("NOMRED"));
                    jsonconsumo.put("umedida", rs.getString("UMEDIDA"));
                    jsonconsumo.put("cantidad", cant);
                    jsonconsumo.put("valorneto", net);
                    jsonconsumo.put("total", tot);

                } else if (rs.getInt("IDCONCEPTO") == 7) {
                    filahpunta += "<tr>";
                    filahpunta += "<td style='font-weight: bold;'>";
                    filahpunta += rs.getString("NOMCONCEPTO");
                    filahpunta += "</td>";

                    filahpunta += "<td style='font-weight: bold;'>";
                    filahpunta += rs.getString("UMEDIDA");
                    filahpunta += "</td>";

                    filahpunta += "<td style='font-weight: bold; text-align: right;'>";
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    filahpunta += Util.formatMiles(Double.toString(cant));
                    filahpunta += "</td>";

                    filahpunta += "<td style='font-weight: bold; text-align: right;'>";
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    filahpunta += Util.formatMiles(Double.toString(net));
                    filahpunta += "</td>";

                    filahpunta += "<td style='text-align: right; '>";
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());
                    filahpunta += Util.formatMiles(Double.toString(Math.floor(tot)));
                    filahpunta += "</td>";
                    filahpunta += "</tr>";

                    jsonconsumo.put("idconcepto", rs.getInt("IDCONCEPTO"));
                    jsonconsumo.put("idred", rs.getInt("IDRED"));
                    jsonconsumo.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                    jsonconsumo.put("nomred", rs.getString("NOMRED"));
                    jsonconsumo.put("umedida", rs.getString("UMEDIDA"));
                    jsonconsumo.put("cantidad", cant);
                    jsonconsumo.put("valorneto", net);
                    jsonconsumo.put("total", tot);
                    total += rs.getInt("TOTAL");
                } else if (rs.getInt("IDCONCEPTO") == 11) {
                    filapotencia += "<tr>";
                    filapotencia += "<td style='font-weight: bold;'>";
                    filapotencia += rs.getString("NOMCONCEPTO");
                    filapotencia += "</td>";

                    filapotencia += "<td style='font-weight: bold;'>";
                    filapotencia += rs.getString("UMEDIDA");
                    filapotencia += "</td>";

                    filapotencia += "<td style='font-weight: bold; text-align: right;'>";
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    filapotencia += Util.formatMiles(Double.toString(cant));
                    filapotencia += "</td>";

                    filapotencia += "<td style='font-weight: bold; text-align: right;'>";
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    filapotencia += Util.formatMiles(Double.toString(net));
                    filapotencia += "</td>";

                    filapotencia += "<td style='text-align: right; '>";
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());
                    filapotencia += Util.formatMiles(Double.toString(Math.floor(tot)));
                    filapotencia += "</td>";
                    filapotencia += "</tr>";

                    jsonconsumo.put("idconcepto", rs.getInt("IDCONCEPTO"));
                    jsonconsumo.put("idred", rs.getInt("IDRED"));
                    jsonconsumo.put("nomconcepto", rs.getString("NOMCONCEPTO"));
                    jsonconsumo.put("nomred", rs.getString("NOMRED"));
                    jsonconsumo.put("umedida", rs.getString("UMEDIDA"));
                    jsonconsumo.put("cantidad", cant);
                    jsonconsumo.put("valorneto", net);
                    jsonconsumo.put("total", tot);
                    total += rs.getInt("TOTAL");
                }
                consumos.put(jsonconsumo);
            }
            tabla += filapotencia;
            tabla += filahpunta;

            boleta.put("consumos", consumos);

            //Separador --------------------------------------------------------------------------------
            tabla += "<tr><td colspan='5' style='border-bottom: 1px solid white;'></td></tr>";

            tabla += "<tr>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Total Monto Neto ";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(total);
            tabla += "</td>";
            tabla += "</tr>";
            boleta.put("totalneto", total);

            tabla += "<tr style='text-align: right;'>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Total I.V.A.(19%)";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]));
            tabla += "</td>";
            tabla += "</tr>";
            boleta.put("iva", Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]));

            tabla += "<tr>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Monto Exento ";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(exento);
            tabla += "</td>";
            tabla += "</tr>";
            boleta.put("exento", exento);

            tabla += "<tr>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Monto Total ";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
            tabla += "</td>";
            tabla += "</tr>";
            boleta.put("total", total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);

            tabla += "<tr style='border-top: 2px solid white;'>";
            tabla += "<td colspan='4' style='border-top: 2px solid white;background-color: white; text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Total a Pagar ";
            tabla += "</td>";
            tabla += "<td style='border-top: 2px solid white; text-align: right;'>";
            tabla += Util.formatMiles(total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
            tabla += "</td>";
            tabla += "</tr>";
            boleta.put("totalapagar", total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
            tabla += "</tbody>";
        } catch (SQLException ex) {
            System.out.println("Problemas en controlador.BoletaController.getDetalleTarifaIdRedConsumoRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();

        boleta.put("fechaemision", "");
        boleta.put("nextfecha", "");
        boleta.put("fechadesde", "");
        boleta.put("fechahasta", "");
        boleta.put("idtarifa", 0);
        boleta.put("nomtarifa", "");
        boleta.put("maxdemandaleida", 0.0);
        boleta.put("maxdemandahorapuntaleida", 0.0);
        boleta.put("maxdemandafacturada", 0.0);
        boleta.put("maxdemandahorapuntafacturada", 0.0);
        boleta.put("consumo", consumo);
        boleta.put("lecturaactual", 0);
        boleta.put("lecturaanterior", 0);
        salida.put("boleta", boleta);
        salida.put("estado", "ok");
        salida.put("tabla", tabla);
        return salida;
    }

    private JSONObject generaBoleta(JSONObject entrada) {
        JSONObject remcli = entrada.getJSONObject("remcli");
        JSONObject boleta = entrada.getJSONObject("boleta");
        JSONObject salida = new JSONObject();
        String querycabecera = "CALL SP_INS_CABECERA_BOLETA("
                + boleta.getInt("idtarifa") + ", "
                + "'" + boleta.getString("nomtarifa") + "', "
                + remcli.getInt("idcliente") + ", "
                + "'" + remcli.getString("nomcliente") + "', "
                + "'" + remcli.getString("razoncliente") + "', "
                + remcli.getInt("rutcliente") + ", "
                + "'" + remcli.getString("dvcliente") + "', "
                + "'" + remcli.getString("direccion") + "', "
                + "'" + remcli.getString("persona") + "', "
                + "'" + remcli.getString("email") + "', "
                + "'" + remcli.getInt("fono") + "', "
                + "NOW(),"
                + "'" + boleta.getString("nextfecha") + "', "
                + remcli.getInt("idinstalacion") + ", "
                + "'" + remcli.getString("nominstalacion") + "', "
                + remcli.getInt("idparque") + ", "
                + "'" + remcli.getString("nomparque") + "', "
                + remcli.getInt("idempalme") + ", "
                + "'" + remcli.getString("numempalme") + "', "
                + remcli.getInt("idcomuna") + ", "
                + "'" + remcli.getString("nomcomuna") + "', "
                + remcli.getInt("idred") + ", "
                + "'" + remcli.getString("nomred") + "', "
                + remcli.getInt("idremarcador") + ", "
                + remcli.getInt("numremarcador") + ", "
                + "'" + remcli.getString("modulos") + "', "
                + "'" + remcli.getString("numserie") + "', "
                + boleta.getInt("lecturaanterior") + ", "
                + boleta.getInt("lecturaactual") + ", "
                + "'" + boleta.getString("fechadesde") + "', "
                + "'" + boleta.getString("fechahasta") + "', "
                + boleta.getInt("consumo") + ", "
                + boleta.getDouble("maxdemandaleida") + ", "
                + boleta.getDouble("maxdemandafacturada") + ", "
                + boleta.getDouble("maxdemandahorapuntaleida") + ", "
                + boleta.getDouble("maxdemandahorapuntafacturada") + ", "
                + boleta.getInt("totalneto") + ", "
                + boleta.getInt("iva") + ", "
                + boleta.getInt("exento") + ", "
                + boleta.getInt("total") + ""
                + ")";
        System.out.println("query:");
        System.out.println(querycabecera);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(querycabecera);
        String numboleta = "";
        int idboleta = 0;
        try {
            while (rs.next()) {
                idboleta = rs.getInt("IDBOLETA");
                numboleta = rs.getString("NUMBOLETA");
            }
            salida.put("estado", "ok");
            salida.put("idboleta", idboleta);
            salida.put("numboleta", numboleta);
        } catch (Exception ex) {
            System.out.println("Problemas en controlador.BoletaController.generaBoleta().");
            System.out.println(ex);
            salida.put("estado", "ok");
            salida.put("error", ex);
        }
        c.cerrar();

        JSONArray consumos = boleta.getJSONArray("consumos");
        Iterator i = consumos.iterator();
        while (i.hasNext()) {

            JSONObject consumo = new JSONObject();
            consumo = (JSONObject) i.next();
            System.out.println(consumo);
            String querydetalle = "CALL SP_INS_DETALLE_BOLETA("
                    + idboleta + ", "
                    + consumo.getInt("idconcepto") + ", "
                    + "'" + consumo.getString("nomconcepto") + "', "
                    + "'" + consumo.getString("umedida") + "', "
                    + consumo.getInt("idred") + ", "
                    + (consumo.getInt("idred") == 0 ? "NULL, " : "'" + consumo.getString("nomred") + "', ")
                    + consumo.getInt("valorneto") + ", "
                    + consumo.getDouble("cantidad") + ", "
                    + consumo.getInt("total") + ""
                    + ")";
            c = new Conexion();
            c.abrir();
            c.ejecutar(querydetalle);
            c.cerrar();
        }
        return salida;
    }

    private JSONObject getLastBoleta(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        JSONObject boleta = new JSONObject();
        JSONArray cargos = new JSONArray();
        JSONObject cargo;
        int idboleta = entrada.getInt("idboleta");
        String query = "SELECT * FROM HIST_BOLETA WHERE IDBOLETA = " + idboleta;
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        try {
            while (rs.next()) {
                int columnas = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= columnas; i++) {
                    boleta.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
            }
            salida.put("boleta", boleta);
        } catch (Exception ex) {
            System.out.println("No se puede obtener la cabecera de la última boleta.");
            System.out.println(ex);
            salida.put("estado", "error");
        }
        c.cerrar();
        // Obtener el detalle de la boleta ----------------------------------------------------------------------------
        query = "SELECT * FROM HIST_DETALLE_BOLETA WHERE IDBOLETA = " + idboleta;
        c = new Conexion();
        c.abrir();
        rs = c.ejecutarQuery(query);
        String filahpunta = "";
        String filapotencia = "";
        int total = 0;
        int exento = 0;
        String tabla = "<thead>"
                + "<tr>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "Cargos"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "Unidad"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "Cantidad"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;'>"
                + "$ Unitario"
                + "</th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; '>"
                + "Valores ($)"
                + "</th>"
                + "</tr>"
                + "</thead><tbody>";
        try {
            while (rs.next()) {
                if (rs.getInt("IDCONCEPTO") != 7 && rs.getInt("IDCONCEPTO") != 11) {
                    tabla += "<tr>";
                    tabla += "<td style='font-weight: bold;'>";
                    tabla += rs.getString("NOMCONCEPTO");
                    tabla += "</td>";

                    tabla += "<td style='font-weight: bold;'>";
                    tabla += rs.getString("UMEDIDA");
                    tabla += "</td>";

                    tabla += "<td style='font-weight: bold; text-align: right;'>";
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    tabla += Util.formatMiles(Double.toString(cant));
                    tabla += "</td>";

                    tabla += "<td style='font-weight: bold; text-align: right;'>";
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    tabla += Util.formatMiles(Double.toString(net));
                    tabla += "</td>";

                    tabla += "<td style='text-align: right; '>";
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());
                    tabla += Util.formatMiles(Double.toString(Math.floor(tot)));
                    tabla += "</td>";
                    tabla += "</tr>";

                    if (rs.getInt("IDCONCEPTO") != 2) {
                        total += rs.getInt("TOTAL");
                    } else {
                        exento = rs.getInt("TOTAL");
                        //total += rs.getInt("TOTAL");
                    }
                } else if (rs.getInt("IDCONCEPTO") == 7) {
                    filahpunta += "<tr>";
                    filahpunta += "<td style='font-weight: bold;'>";
                    filahpunta += rs.getString("NOMCONCEPTO");
                    filahpunta += "</td>";

                    filahpunta += "<td style='font-weight: bold;'>";
                    filahpunta += rs.getString("UMEDIDA");
                    filahpunta += "</td>";

                    filahpunta += "<td style='font-weight: bold; text-align: right;'>";
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    filahpunta += Util.formatMiles(Double.toString(cant));
                    filahpunta += "</td>";

                    filahpunta += "<td style='font-weight: bold; text-align: right;'>";
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    filahpunta += Util.formatMiles(Double.toString(net));
                    filahpunta += "</td>";

                    filahpunta += "<td style='text-align: right; '>";
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());
                    filahpunta += Util.formatMiles(Double.toString(Math.floor(tot)));
                    filahpunta += "</td>";
                    filahpunta += "</tr>";
                    total += rs.getInt("TOTAL");
                } else if (rs.getInt("IDCONCEPTO") == 11) {
                    filapotencia += "<tr>";
                    filapotencia += "<td style='font-weight: bold;'>";
                    filapotencia += rs.getString("NOMCONCEPTO");
                    filapotencia += "</td>";

                    filapotencia += "<td style='font-weight: bold;'>";
                    filapotencia += rs.getString("UMEDIDA");
                    filapotencia += "</td>";

                    filapotencia += "<td style='font-weight: bold; text-align: right;'>";
                    Double cant = Double.parseDouble(rs.getBigDecimal("CANTIDAD").toString());
                    filapotencia += Util.formatMiles(Double.toString(cant));
                    filapotencia += "</td>";

                    filapotencia += "<td style='font-weight: bold; text-align: right;'>";
                    Double net = Double.parseDouble(rs.getBigDecimal("VALORNETO").toString());
                    filapotencia += Util.formatMiles(Double.toString(net));
                    filapotencia += "</td>";

                    filapotencia += "<td style='text-align: right; '>";
                    Double tot = Double.parseDouble(rs.getBigDecimal("TOTAL").toString());
                    filapotencia += Util.formatMiles(Double.toString(Math.floor(tot)));
                    filapotencia += "</td>";
                    filapotencia += "</tr>";
                    total += rs.getInt("TOTAL");
                }
            }
            tabla += filapotencia;
            tabla += filahpunta;

            //Separador --------------------------------------------------------------------------------
            tabla += "<tr><td colspan='5' style='border-bottom: 1px solid white;'></td></tr>";

            tabla += "<tr>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Total Monto Neto ";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(total);
            tabla += "</td>";
            tabla += "</tr>";

            tabla += "<tr style='text-align: right;'>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Total I.V.A.(19%)";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]));
            tabla += "</td>";
            tabla += "</tr>";

            tabla += "<tr>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Monto Exento ";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(exento);
            tabla += "</td>";
            tabla += "</tr>";

            tabla += "<tr>";
            tabla += "<td colspan='4' style='text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Monto Total ";
            tabla += "</td>";
            tabla += "<td style='text-align: right;'>";
            tabla += Util.formatMiles(total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
            tabla += "</td>";
            tabla += "</tr>";

            tabla += "<tr style='border-top: 2px solid white;'>";
            tabla += "<td colspan='4' style='border-top: 2px solid white;background-color: white; text-align: right; padding-right: 2px; font-weight: bold;'>";
            tabla += "Total a Pagar ";
            tabla += "</td>";
            tabla += "<td style='border-top: 2px solid white; text-align: right;'>";
            tabla += Util.formatMiles(total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento);
            tabla += "</td>";
            tabla += "</tr>";
            tabla += "</tbody>";
            salida.put("tabla", tabla);
        } catch (SQLException ex) {
            System.out.println("Problemas en controlador.BoletaController.getDetalleTarifaIdRedConsumoRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        
        salida.put("estado", "ok");
        return salida;
    }
}
