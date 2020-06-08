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

    private JSONObject armarPreBoletaRemarcador(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int idremarcador = entrada.getInt("idremarcador");
        String fechaini = entrada.getString("fechaini");
        String fechafin = entrada.getString("fechafin");
        return salida;

    }

    private JSONObject getDetalleTarifaIdRedConsumoRemarcador(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String fechaconsulta = entrada.getString("fechafin");
        int idtarifa = entrada.getInt("idtarifa");
        int idred = entrada.getInt("idred");
        int consumo = entrada.getInt("consumo");
        int numremarcador = entrada.getInt("numremarcador");
        int total = 0;
        int exento = 0;
        //Query creada. Seguir... 
        //SP_GET_DETALLE_TARIFA_IDRED_CONSUMO_REMARCADOR
        String query = "CALL SP_GET_DETALLE_TARIFA_IDRED_CONSUMO_REMARCADOR("
                + "'" + fechaconsulta + "', "
                + idtarifa + ", "
                + idred + ", "
                + consumo + ", "
                + numremarcador
                + ")";
        System.out.println(query);
        String tabla = "<thead>"
                + "<tr>"
                + "<th colspan='2' style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; '>"
                + "Cargos"
                + "</th>"
                + "<th></th>"
                + "<th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; '>"
                + "Valores ($)"
                + "</th>"
                + "</tr>"
                + "</thead><tbody>";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try{
            while(rs.next()){
                tabla += "<tr>";
                tabla += "<td colspan='2' style='padding-right: 100px; font-weight: bold;'>";
                tabla += rs.getString("NOMCONCEPTO") + " (" + rs.getString("UMEDIDA") + ")";
                tabla += "</td>";
                tabla += "<td></td>";
                tabla += "<td>";
                tabla += rs.getInt("TOTAL");
                tabla += "</td>";
                tabla += "</tr>";
                if(rs.getInt("IDCONCEPTO") != 2){
                    total += rs.getInt("TOTAL");
                }else{
                    exento = rs.getInt("TOTAL");
                }
            }
            tabla += "<tr>";
            tabla += "<td colspan='3' style='text-align: right; padding-right: 25px; font-weight: bold;'>";
            tabla += "Total Monto Neto ";
            tabla += "</td>";
            tabla += "<td>";
            tabla += total;
            tabla += "</td>";
            tabla += "</tr>";
            
            tabla += "<tr>";
            tabla += "<td colspan='3' style='text-align: right; padding-right: 25px; font-weight: bold;'>";
            tabla += "Total I.V.A. ";
            tabla += "</td>";
            tabla += "<td>";
            tabla += Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]);
            tabla += "</td>";
            tabla += "</tr>";
            
            tabla += "<tr>";
            tabla += "<td colspan='3' style='text-align: right; padding-right: 25px; font-weight: bold;'>";
            tabla += "Monto Exento ";
            tabla += "</td>";
            tabla += "<td>";
            tabla += exento;
            tabla += "</td>";
            tabla += "</tr>";
            
            tabla += "<tr>";
            tabla += "<td colspan='3' style='text-align: right; padding-right: 25px; font-weight: bold;'>";
            tabla += "Monto Total ";
            tabla += "</td>";
            tabla += "<td>";
            tabla += total + Integer.parseInt(Double.toString(total * 0.19).split("\\.")[0]) + exento;
            tabla += "</td>";
            tabla += "</tr>";
            tabla += "</tbody>";
            tabla += "</table>";
        }catch (SQLException ex) {
            System.out.println("Problemas en controlador.BoletaController.getDetalleTarifaIdRedConsumoRemarcador().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        salida.put("estado", "ok");
        salida.put("tabla", tabla);
        return salida;
    }

}
