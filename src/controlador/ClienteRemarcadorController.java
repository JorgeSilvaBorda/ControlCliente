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

public class ClienteRemarcadorController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-remarcadores-asignados-idcliente":
                out.print(getRemarcadoresAsignadosIdCliente(entrada));
                break;
            case "asignar-remarcador-cliente":
                out.print(asignarRemarcadorCliente(entrada));
                break;
            case "quitar-remarcador-cliente":
                out.print(quitarRemarcadorCliente(entrada));
                break;
        }
    }

    private JSONObject getRemarcadoresAsignadosIdCliente(JSONObject entrada) {
        int idcliente = entrada.getInt("idcliente");
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_REMARCADORES_ASIGNADOS_IDCLIENTE(" + idcliente + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDREMARCADOR") + "' /><span>" + rs.getString("NUMREMARCADOR") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDEMPALME") + "' /><span>" + rs.getString("NUMEMPALME") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDINSTALACION") + "' /><span>" + rs.getString("NOMINSTALACION") + "</span></td>";
                filas += "<td><span>" + rs.getString("FECHAASIGNACION") + "</span></td>";
                filas += "<td><a href='#' style='color:#D25E45; font-size:12px;' class='oi oi-x' onclick='quitar(" + rs.getInt("IDREMARCADOR") + ", " + rs.getInt("IDCLIENTE") + ")'></a></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ClienteRemarcadorController.getRemarcadoresAsignadosIdCliente().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject asignarRemarcadorCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_ASIGNAR_REMARCADOR_CLIENTE(" + entrada.getInt("idremarcador") + ", " + entrada.getInt("idcliente") + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject quitarRemarcadorCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_QUITAR_REMARCADOR_CLIENTE(" + entrada.getInt("idremarcador") + ", " + entrada.getInt("idcliente") + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }
}
