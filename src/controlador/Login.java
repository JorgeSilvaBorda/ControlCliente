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
import modelo.Util;

public class Login extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        JSONObject respuesta = loginUsuario(request.getParameter("rut"), request.getParameter("password"), request);
        if (respuesta.getInt("filas") == 1) {
            response.sendRedirect("main.jsp");
        } else if (respuesta.getInt("filas") < 1) {
            response.sendRedirect("login.jsp?status=badpass&rut=" + request.getParameter("rut"));
        }
    }

    private JSONObject loginUsuarioTest(String rutlogin, String passlogin, HttpServletRequest request) {
        JSONObject salida = new JSONObject();
        if (Util.hashMD5(passlogin).equals(Util.hashMD5("password"))) {
            JSONObject usuario = new JSONObject();
            usuario.put("idusuario", 1);
            usuario.put("idtipousuario", 1);
            usuario.put("nomtipousuario", "Administrador");
            usuario.put("rut", 16355662);
            usuario.put("dv", "6");
            usuario.put("nombres", "Jorge");
            usuario.put("appaterno", "Silva");
            usuario.put("apmaterno", "Borda");
            salida.put("usuario", usuario);
            salida.put("filas", 1);
            HttpSession session = request.getSession();
            session.setAttribute("idusuario", usuario.getInt("idusuario"));
            session.setAttribute("idtipousuario", usuario.getInt("idtipousuario"));
            session.setAttribute("nomtipousuario", usuario.getString("nomtipousuario"));
            session.setAttribute("rut", usuario.getInt("rut"));
            session.setAttribute("dv", usuario.getString("dv"));
            session.setAttribute("nombres", usuario.getString("nombres"));
            session.setAttribute("appaterno", usuario.getString("appaterno"));
            session.setAttribute("apmaterno", usuario.getString("apmaterno"));
            session.setAttribute("usuario.json", usuario.toString());
        } else {
            salida.put("filas", 0);
        }
        return salida;
    }

    private JSONObject loginUsuario(String rutlogin, String passlogin, HttpServletRequest request) {

        JSONObject salida = new JSONObject();

        String rutsolo = rutlogin.split("-")[0];
        rutsolo = rutsolo.replaceAll("\\.", "");
        int rut = Integer.parseInt(rutsolo);
        String password = passlogin;

        String query = "CALL SP_VALIDA_USUARIO(" + rut + ", '" + modelo.Util.hashMD5(password) + "')";
        System.out.println(query);
        Conexion c = new Conexion();
        c.abrir();
        ResultSet rs = c.ejecutarQuery(query);
        int cont = 0;
        try {
            while (rs.next()) {
                JSONObject usuario = new JSONObject();
                usuario.put("idusuario", rs.getInt("IDUSUARIO"));
                usuario.put("idtipousuario", rs.getInt("IDTIPOUSUARIO"));
                usuario.put("nomtipousuario", rs.getString("NOMTIPOUSUARIO"));
                usuario.put("rut", rs.getInt("RUT"));
                usuario.put("dv", rs.getString("DV"));
                usuario.put("nombres", rs.getString("NOMBRES"));
                usuario.put("appaterno", rs.getString("APPATERNO"));
                usuario.put("apmaterno", rs.getString("APMATERNO"));
                cont++;
                salida.put("usuario", usuario);
            }
            salida.put("filas", cont);
            if (cont < 1) {
                salida.put("estado", "no-valido");
                salida.put("mensaje", "No se encuentra el registro de usuario/clave ingresados.");
                request.getSession().invalidate();
            } else if (cont > 1) {
                salida.put("estado", "no-valido");
                salida.put("mensaje", "No se encuentra el registro de usuario/clave ingresados.");
                System.out.println("[WRN] Se encuentra más de una combinación Usuario(rut)/password!");
                System.out.println("    Rut: " + rut);
                request.getSession().invalidate();
            } else {
                salida.put("estado", "ok");
                HttpSession session = request.getSession();
                JSONObject usuario = salida.getJSONObject("usuario");
                session.setAttribute("idusuario", usuario.getInt("idusuario"));
                session.setAttribute("idtipousuario", usuario.getInt("idtipousuario"));
                session.setAttribute("nomtipousuario", usuario.getString("nomtipousuario"));
                session.setAttribute("rut", usuario.getInt("rut"));
                session.setAttribute("dv", usuario.getString("dv"));
                session.setAttribute("nombres", usuario.getString("nombres"));
                session.setAttribute("appaterno", usuario.getString("appaterno"));
                session.setAttribute("apmaterno", usuario.getString("apmaterno"));
                session.setAttribute("usuario.json", usuario.toString());
            }
        } catch (JSONException | SQLException ex) {
            System.out.println("Problemas en  controlador.UsuarioControler.loginUsuario()");
            System.out.println(ex);
            salida.put("estado", "error");
            salida.put("mensaje", ex);
        }
        c.cerrar();
        return salida;
    }
}
