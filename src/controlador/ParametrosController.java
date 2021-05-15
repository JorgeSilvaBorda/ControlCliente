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

public class ParametrosController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-select-comunas":
                out.print(getSelectComunas());
                break;
            case "get-finmes-anterior":
                out.print(getFinMesAnterior());
                break;
        }
    }

    private JSONObject getSelectComunas() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_COMUNAS()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDCOMUNA", "NOMCOMUNA");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject getFinMesAnterior() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_FINMES_ANTERIOR()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        try{
            while(rs.next()){
                salida.put("finmesanterior", rs.getString("FINMESANTERIOR"));
                salida.put("finmesanteriorformat", rs.getString("FINMESANTERIORFORMAT"));
            }
            salida.put("estado", "ok");
        }catch (JSONException | SQLException ex) {
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        
        c.cerrar();
        return salida;
    }
}
