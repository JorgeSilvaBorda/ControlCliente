package controlador;

import clases.json.JSONArray;
import clases.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Boleta;
import modelo.Remarcador;
import modelo.Util;

public class BoletaController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch(entrada.getString("tipo")){
            case "armar-pre-boleta":
                out.print(armarPreBoleta(entrada));
                break;
        }
    }
    
    private JSONObject armarPreBoleta(JSONObject entrada){
        JSONObject salida = new JSONObject();
        int idcliente = entrada.getInt("idcliente");
        String fechaini = entrada.getString("fechaini");
        String fechafin = entrada.getString("fechafin");
        
        Boleta boleta = new Boleta(idcliente, fechaini, fechafin);
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
        
        for(Remarcador r : boleta.getRemarcadores()){
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
    
    private String armarTablaRemarcadores(Boleta b){
        if(b.getRemarcadores().size() < 1){
            return "";
        }
        String  tabla = "<h4>Detalle de Consumo por Remarcador</h4>";
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
        for(Remarcador r : b.getRemarcadores()){
            tabla +="<tr>";
            tabla +="<td>" + r.getNumremarcador() + "</td>";            
            tabla +="<td>" + r.getNomplogistico() + ". " + r.getDireccion() + " " + r.getModulos() + "</td>";            
            tabla +="<td>" + r.lecturaanterior + "</td>";            
            tabla +="<td>" + r.lecturaactual + "</td>";            
            tabla +="<td>" + r.diffperiodo + "</td>";            
            tabla +="</tr>";
        }
        tabla += "</tbody></table>";
        return tabla;
    }
}
