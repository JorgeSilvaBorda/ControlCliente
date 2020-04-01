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
import javax.servlet.http.HttpSession;
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
                case "ins-cliente":
                out.print(insCliente(entrada));
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
                filas += "<td>" + rs.getString("MODULOS") + "</td>";
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
                + "'" + entrada.getString("modulos") + "',"
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
}
