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

public class ParqueController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        JSONObject entrada = new JSONObject(request.getParameter("datos"));
        switch (entrada.getString("tipo")) {
            case "get-parques":
                out.print(getParques());
                break;
            case "get-select-parque-empalme":
                int idempalme = entrada.getInt("idempalme");
                out.print(getSelectParqueEmpalme(idempalme));
                break;
            case "ins-parque":
                out.print(insParque(entrada));
                break;
        }

    }

    private JSONObject getParques() {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_PARQUES()";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        String filas = "";
        try {
            while (rs.next()) {
                filas += "<tr>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDPARQUE") + "' /><span>" + rs.getString("NOMPARQUE") + "</span></td>";
                filas += "<td><input type='hidden' value='" + rs.getInt("IDFASE") + "' /><span>" + rs.getString("NOMFASE") + "</span></td>";
                filas += "<td><button style='font-size:10px; padding: 0.1 rem 0.1 rem;' type='button' class='btn btn-sm btn-warning' onclick='activarEdicion(this)'>Editar</button></td>";
                filas += "</tr>";
            }
            salida.put("tabla", filas);
            salida.put("estado", "ok");
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en controlador.ParqueController.getParques().");
            System.out.println(ex);
            ex.printStackTrace();
            salida.put("estado", "error");
            salida.put("error", ex);
        }
        c.cerrar();
        return salida;
    }

    private JSONObject getSelectParqueEmpalme(int idempalme) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_GET_SELECT_PARQUE_EMPALME(" + idempalme + ")";
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);

        String options = modelo.Util.armarSelect(rs, "0", "Seleccione", "IDPARQUE", "NOMPARQUE");
        salida.put("options", options);
        salida.put("estado", "ok");
        c.cerrar();
        return salida;
    }

    private JSONObject insParque(JSONObject entrada) {
        JSONObject salida = new JSONObject();
        String query = "CALL SP_INS_PARQUE("
                + "'" + entrada.getInt("idfase") + "',"
                + "'" + entrada.getString("nomparque") + "'"
                + ")";
        Conexion c = new Conexion();
        c.abrir();
        c.ejecutar(query);
        c.cerrar();
        salida.put("estado", "ok");
        return salida;
    }

}
