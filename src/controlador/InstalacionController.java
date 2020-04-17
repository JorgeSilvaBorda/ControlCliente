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
            case "del-instalacion":
                out.print(delInstalacion(entrada));
                break;
            case "get-instalacion-idinstalacion":
                out.print(getInstalacionIdInstalacion(entrada));
                break;
            case "upd-instalacion":
                out.print(updInstalacion(entrada.getJSONObject("instalacion")));
                break;
            case "existe-instalacion":
                out.print(existeInstalacion(entrada));
                break;
            case "existe-instalacion-update":
                out.print(existeInstalacionUpdate(entrada));
                break;
            case "existe-direccion-instalacion":
                out.print(existeDireccionInstalacion(entrada));
                break;
            case "existe-direccion-instalacion-update":
                out.print(existeDireccionInstalacionUpdate(entrada));
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
                filas += "<td><span>" + rs.getString("DIRECCION") + "</span></td>";
                filas += "<td><span>" + rs.getString("NOMCOMUNA") + "</span></td>";
                filas += "<td style='width: 15%;'>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-danger' onclick='eliminar(this)'>Eliminar</button>"
                        + "</td>";
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
        String query = "CALL SP_INS_INSTALACION("
                + "'" + entrada.getString("nominstalacion") + "', "
                + "'" + entrada.getString("direccion") + "', "
                + "" + entrada.getString("idcomuna") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject delInstalacion(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_DEL_INSTALACION("
                + entrada.getInt("idinstalacion") + ")";
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
                instalacion.put("nominstalacion", rs.getString("NOMINSTALACION"));
                instalacion.put("direccion", rs.getString("DIRECCION"));
                instalacion.put("nomcomuna", rs.getString("NOMCOMUNA"));
                instalacion.put("idcomuna", rs.getInt("IDCOMUNA"));

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
                + "'" + instalacion.getString("nominstalacion") + "', "
                + "'" + instalacion.getString("direccion") + "', "
                + "" + instalacion.getInt("idcomuna") + ""
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject existeInstalacion(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_EXISTE_INSTALACION('" + entrada.getString("nominstalacion") + "')";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cantidad = 0;
        try {
            while (rs.next()) {
                cantidad = rs.getInt("CANTIDAD");
            }
            salida.put("cantidad", cantidad);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.InstalacionController.existeInstalacion().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject existeInstalacionUpdate(JSONObject entrada) {

        JSONObject salida = new JSONObject();
        String query = "CALL SP_EXISTE_INSTALACION_UPDATE("
                + entrada.getInt("idinstalacion") + ", "
                + "'" + entrada.getString("nominstalacion") + "', "
                + "'" + entrada.getString("newnominstalacion") + "'"
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cantidad = 0;
        try {
            while (rs.next()) {
                cantidad = rs.getInt("CANTIDAD");
            }
            salida.put("cantidad", cantidad);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.InstalacionController.existeInstalacion().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject existeDireccionInstalacion(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_EXISTE_DIRECCION_INSTALACION('" + entrada.getString("direccion") + "', " + entrada.getInt("idcomuna") + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cantidad = 0;
        try {
            while (rs.next()) {
                cantidad = rs.getInt("CANTIDAD");
            }
            salida.put("cantidad", cantidad);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.InstalacionController.existeDireccionInstalacion().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }
    
    private JSONObject existeDireccionInstalacionUpdate(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_EXISTE_DIRECCION_INSTALACION_UPDATE("
                + "" + entrada.getInt("idinstalacion") + ", "
                + "" + entrada.getInt("idcomuna") + ", "
                + "" + entrada.getInt("newidcomuna") + ", "
                + "'" + entrada.getString("direccion") + "', "
                + "'" + entrada.getString("newdireccion") + "'"
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cantidad = 0;
        try {
            while (rs.next()) {
                cantidad = rs.getInt("CANTIDAD");
            }
            salida.put("cantidad", cantidad);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.InstalacionController.existeDireccionInstalacionUpdate().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

}
