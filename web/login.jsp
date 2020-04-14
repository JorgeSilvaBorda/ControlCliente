<%
    String rutProblemas = "";
    if (request.getParameterMap().containsKey("status")) {
        if (request.getParameter("status").equals("badpass")) {
            rutProblemas = request.getParameter("rut");
        }
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Login</title>
        <link href="css/bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="signin.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet">
        <link href="iconic/font/css/open-iconic-bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <script src="js/jquery-3.4.1.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/jquery.rut.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/funciones.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="login.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
    </head>

    <body class="text-center">
        <div class="container-fluid">
            <form name="form" id="form" action="Login" method="post" class="form-signin" >
                <h1 class="mb-4 oi oi-lock-locked text-center d-flex justify-content-center"></h1>
                <h1 class="h3 mb-3 font-weight-normal">Por favor ingrese</h1>
                <label for="rut" class="sr-only">Usuario</label>
                <input type="text" class="form-control" id="rut" name="rut" placeholder="Ej: 12345678-9" maxlength="12" autofocus required value="<% out.print(rutProblemas); %>">
                <label for="password" class="sr-only">Password</label>
                <input type="password" class="form-control" id="password" name="password"  maxlength="20" required placeholder="Password">
                <button class="btn btn-lg btn-primary btn-block" id="btn-login" onclick="return validarCampos();" type="submit">Ingresar</button>
                <%
                    if (request.getParameterMap().containsKey("status")) {
                        if (request.getParameter("status").equals("badpass")) {
                            out.print("<br /><div id='div-alert' class='alert alert-danger' style='display:none;' role='alert'>Usuario o contraseña equivocados.</div>");
                        }
                    }
                %>
                <p class="mt-5 mb-3 text-muted">Bodenor 2020</p>
            </form>
        </div>
    </body>
</html>
