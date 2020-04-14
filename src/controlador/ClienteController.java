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

public class ClienteController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-clientes":
                out.print(getClientes());
                break;
            case "get-select-clientes":
                out.print(getSelectClientes());
                break;
            case "ins-cliente":
                out.print(insCliente(entrada));
                break;
            case "get-cliente-idcliente":
                out.print(getClienteIdCliente(entrada));
                break;
            case "upd-cliente":
                out.print(updCliente(entrada.getJSONObject("cliente")));
                break;
        }
    }

    private JSONObject getClientes() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_CLIENTES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDCLIENTE") + "' /><span>" + modelo.Util.formatRut(rs.getString("RUTCLIENTE") + "-" + rs.getString("DVCLIENTE")) + "</span></td>";
                filas += "<td>" + rs.getString("NOMCLIENTE") + "</td>";
                filas += "<td>" + rs.getString("RAZONCLIENTE") + "</td>";
                filas += "<td>" + rs.getString("DIRECCION") + "</td>";
                filas += "<td>" + rs.getString("PERSONA") + "</td>";
                filas += "<td>" + rs.getString("CARGO") + "</td>";
                filas += "<td>" + rs.getInt("FONO") + "</td>";
                filas += "<td>" + rs.getString("EMAIL") + "</td>";
                filas += "<td><button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ClienteController.getClientes().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject insCliente(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_CLIENTE("
                + "'" + entrada.getString("rutcliente") + "',"
                + "'" + entrada.getString("dvcliente") + "',"
                + "'" + entrada.getString("nomcliente") + "',"
                + "'" + entrada.getString("razoncliente") + "',"
                + "'" + entrada.getString("direccion") + "',"
                + "'" + entrada.getString("persona") + "',"
                + "'" + entrada.getString("cargo") + "',"
                + "" + entrada.getInt("fono") + ","
                + "'" + entrada.getString("email") + "')";

        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getClienteIdCliente(JSONObject entrada) {
        int idcliente = entrada.getInt("idcliente");
        JSONObject salida = new JSONObject();
        JSONObject cliente = new JSONObject();
        String query = "CALL SP_GET_CLIENTE_IDCLIENTE(" + idcliente + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                cliente.put("idcliente", rs.getInt("IDCLIENTE"));
                cliente.put("rutcliente", rs.getInt("RUTCLIENTE"));
                cliente.put("dvcliente", rs.getString("DVCLIENTE"));
                cliente.put("rutfullcliente", rs.getInt("RUTCLIENTE") + "-" + rs.getString("DVCLIENTE"));
                cliente.put("nomcliente", rs.getString("NOMCLIENTE"));
                cliente.put("razoncliente", rs.getString("RAZONCLIENTE"));
                cliente.put("direccion", rs.getString("DIRECCION"));
                cliente.put("persona", rs.getString("PERSONA"));
                cliente.put("cargo", rs.getString("CARGO"));
                cliente.put("fono", rs.getInt("FONO"));
                cliente.put("email", rs.getString("EMAIL"));
            }
            salida.put("cliente", cliente);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ClienteController.getClienteeIdCliente().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject updCliente(JSONObject cliente) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_UPD_CLIENTE("
                + cliente.getInt("idcliente") + ","
                + cliente.getInt("rutcliente") + ","
                + "'" + cliente.getString("dvcliente") + "',"
                + "'" + cliente.getString("nomcliente") + "',"
                + "'" + cliente.getString("razoncliente") + "',"
                + "'" + cliente.getString("direccion") + "',"
                + "'" + cliente.getString("persona") + "',"
                + "'" + cliente.getString("cargo") + "',"
                + "" + cliente.getInt("fono") + ","
                + "'" + cliente.getString("email") + "')";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getSelectClientes() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_SELECT_CLIENTES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDCLIENTE", "RAZONCLIENTE");
        c.cerrar();
        salida.put("estado", "ok");
        salida.put("options", options);
        return salida;
    }
}
