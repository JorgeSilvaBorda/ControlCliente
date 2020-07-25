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

public class ContactoController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-contactos":
                out.print(getContactos());
                break;
            case "get-contacto-idcontacto":
                out.print(getContactoIdContacto(entrada));
                break;
            case "ins-contacto":
                out.print(insContacto(entrada));
                break;
        }
    }

    private JSONObject getContactos() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_CONTACTOS()";
        String tabla = "";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                tabla += "<tr>";
                tabla += "<td><input type='hidden' value='" + rs.getInt("IDCONTACTO") + "' /><span>" + rs.getString("NOMCLIENTE") + "</span></td>";
                tabla += "<td>" + rs.getString("PERSONA") + "</td>";
                tabla += "<td>" + rs.getString("CARGO") + "</td>";
                tabla += "<td>" + rs.getInt("FONO") + "</td>";
                tabla += "<td>" + rs.getString("EMAIL") + "</td>";
                tabla += "<td style='width: 10%;'>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button>"
                        + "<button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-danger' onclick='eliminar(this)'>Eliminar</button>"
                        + "</td>";
                tabla += "</tr>";
            }
        } catch (Exception ex) {
            System.out.println("Problemas en controlador.ContactoController.getContactos().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject insContacto(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        int idcliente = entrada.getInt("idcliente");
        String persona = entrada.getString("persona");
        String cargo = entrada.getString("cargo");
        int fono = entrada.getInt("fono");
        String email = entrada.getString("email");
        String query = "CALL SP_INS_CONTACTO("
                + idcliente + ", "
                + "'" + persona + "', "
                + "'" + cargo + "', "
                + fono + ", "
                + "'" + email + "'"
                + ")";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

    private JSONObject getContactoIdContacto(JSONObject entrada) {
        int idcontacto = entrada.getInt("idcontacto");
        JSONObject salida = new JSONObject();
        JSONObject contacto = new JSONObject();
        String query = "CALL SP_GET_CONTACTO_IDCONTACTO(" + idcontacto + ")";
        Conexion c = new Conexion();
        System.out.println(query);
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        try {
            while (rs.next()) {
                contacto.put("idcontacto", rs.getInt("IDCONTACTO"));
                contacto.put("idcliente", rs.getInt("IDCLIENTE"));
                contacto.put("persona", rs.getString("PERSONA"));
                contacto.put("cargo", rs.getString("CARGO"));
                contacto.put("fono", rs.getInt("FONO"));
                contacto.put("email", rs.getString("EMAIL"));
            }
            salida.put("contacto", contacto);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ContactoController.getContactoIdContacto().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }
}
