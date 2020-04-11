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

public class InstalacionController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-instalaciones":
                out.print(getInstalaciones());
                break;
            case "get-select-instalacion":
                out.print(getSelectInstalacion());
                break;
            case "ins-instalacion":
                out.print(insInstalacion(entrada));
                break;
            case "get-instalacion-idinstalacion":
                out.print(getInstalacionIdInstalacion(entrada));
                break;
            case "upd-instalacion":
                out.print(updInstalacion(entrada.getJSONObject("instalacion")));
                break;
        }
    }

    private JSONObject getInstalaciones() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_INSTALACIONES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPLOGISTICO") + "' /><span>" + rs.getString("NOMPLOGISTICO") + "</span></td>";
                filas += "<td><span>" + rs.getString("DIRECCION") + "</span></td>";
                filas += "<td><button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.InstalacionController.getInstalaciones().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getSelectInstalacion() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_INSTALACIONES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDINSTALACION", "NOMINSTALACION");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject insInstalacion(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_INSTALACION('" + entrada.getString("nominstalacion") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getInstalacionIdInstalacion(JSONObject entrada) {
        int idinstalacion = entrada.getInt("idinstalacion");
        JSONObject salida = new JSONObject();
        JSONObject instalacion = new JSONObject();
        String query = "CALL SP_GET_INSTALACION_IDINSTALACION(" + idinstalacion + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                instalacion.put("idinstalacion", rs.getInt("IDINSTALACION"));
                instalacion.put("idplogistico", rs.getInt("IDPLOGISTICO"));
                instalacion.put("nominstalacion", rs.getString("NOMINSTALACION"));
                instalacion.put("direccion", rs.getString("DIRECCION"));
            }
            salida.put("instalacion", instalacion);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.InstalacionController.getInstalacionIdInstalacion().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject updInstalacion(JSONObject instalacion) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_INSTALACION("
                + instalacion.getInt("idinstalacion") + ","
                + instalacion.getInt("idplogistico") + ","
                + "'" + instalacion.getString("nominstalacion") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }
}
