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

public class EmpalmeController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-empalmes":
                out.print(getEmpalmes());
                break;
            case "get-select-empalme":
                out.print(getSelectEmpalme());
                break;
            case "ins-empalme":
                out.print(insEmpalme(entrada));
                break;
            case "get-empalme-idempalme":
                out.print(getEmpalmeIdEmpalme(entrada));
                break;
            case "upd-empalme":
                out.print(updEmpalme(entrada.getJSONObject("empalme")));
                break;
        }
    }

    private JSONObject getEmpalmes() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_EMPALMES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDEMPALME") + "' /><span>" + rs.getString("NUMEMPALME") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td><button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.EmpalmeController.getEmpalmes().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getSelectEmpalme() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_EMPALMES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDEMPALME", "NUMEMPALME");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject insEmpalme(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_EMPALME("
                + "'" + entrada.getInt("idinstalacion") + "',"
                + "'" + entrada.getString("numempalme") + "'"
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getEmpalmeIdEmpalme(JSONObject entrada) {
        int idempalme = entrada.getInt("idempalme");
        JSONObject salida = new JSONObject();
        JSONObject empalme = new JSONObject();
        String query = "CALL SP_GET_EMPALME_IDEMPALME(" + idempalme + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                empalme.put("idempalme", rs.getInt("IDEMPALME"));
                empalme.put("idinstalacion", rs.getInt("IDINSTALACION"));
                empalme.put("numempalme", rs.getString("NUMEMPALME"));
                empalme.put("nominstalacion", rs.getString("NOMINSTALACION"));
            }
            salida.put("empalme", empalme);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.EmpalmeController.getEmpalmeIdEmpalme().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject updEmpalme(JSONObject empalme) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_EMPALME("
                + empalme.getInt("idempalme") + ","
                + empalme.getInt("idinstalacion") + ","
                + "'" + empalme.getString("numempalme") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }
}
