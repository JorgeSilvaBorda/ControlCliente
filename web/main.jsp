<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String menu1 = "";
    String menu2 = "";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="css/estilos.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="js/datatables/datatables.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="js/datatables/jquery-ui.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="js/pivot.min.css" rel="stylesheet" type="text/css"/>
        <link href="iconic/font/css/open-iconic-bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <script src="main.js" type="text/javascript"></script>

        <script src="js/jquery-3.4.1.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/datatables/jquery-ui.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/popper.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/bootstrap.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>

        <script src="js/globales.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/jquery.rut.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/datatables/datatables.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/datatables/FixedHeader-3.1.4/js/fixedHeader.bootstrap4.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/datatables/Buttons-1.5.6/js/buttons.print.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/funciones.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        <script src="js/datatables/Buttons-1.5.6/js/buttons.html5.js" type="text/javascript"></script>
        <script src="js/datatables/JSZip-2.5.0/jszip.js" type="text/javascript"></script>
        <script src="js/datatables/pdfmake-0.1.36/pdfmake.js" type="text/javascript"></script>
        <script src="js/datatables/pdfmake-0.1.36/vfs_fonts.js" type="text/javascript"></script>
        <script src="js/pivot.min.js" type="text/javascript"></script>
        <script src="js/Chart.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>

        <script src="js/html2canvas.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        <script src="js/pdfmake.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        <script src="js/vfs_fonts.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        
        <script src="js/html2pdf.bundle.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <nav class="navbar navbar-expand-lg navbar-light bg-light navbar-fixed fixed-top">
                        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                            <span class="navbar-toggler-icon"></span>
                        </button> 
                        <a class="navbar-brand" href="#"><img src="img/logo.jpg" height="30em;" alt="Bodenor FlexCenter"></a>
                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                            <ul class="navbar-nav">
                                <%
                                    String idtipousuario = session.getAttribute("idtipousuario").toString();
                                    if (idtipousuario != null) {
                                        if (Integer.parseInt(idtipousuario) == 2) {
                                            out.print("<li class='nav-item dropdown'>\n"
                                                    + "<a class='nav-link dropdown-toggle' href='#' id='navbarDropdownMenuLink' data-toggle='dropdown'>Reportes</a>\n"
                                                    + "<div class='dropdown-menu' aria-labelledby='navbarDropdownMenuLink'>\n"
                                                    + "<a onclick='cargarModulo(\"boleta-empalme\");' class='dropdown-item' href='#'>Emitir Detalle Complemento Servicios de Administración</a> \n"
                                                    + "<a onclick='cargarModulo(\"resumen-pagos\");' class='dropdown-item' href='#'>Resumen Detalles Complementos Servicios de Administración por Instalación</a> \n"
                                                    + "<a onclick='cargarModulo(\"consumo-cliente-remarcador\");' class='dropdown-item' href='#'>Consumo de Cliente</a> \n"
                                                    + "<a onclick='cargarModulo(\"resumen-mes-cliente\");' class='dropdown-item' href='#'>Resumen Mes Cliente</a> \n"
                                                    + "<a onclick='cargarModulo(\"hist-tarifa\");' class='dropdown-item' href='#'>Histórico de Tarifa</a> \n"
                                                    + "</div>"
                                                    + "</li>");
                                        } else if (Integer.parseInt(idtipousuario) == 1) {
                                            out.print("<li class='nav-item dropdown'>\n"
                                                    + "<a class='nav-link dropdown-toggle' href='#' id='navbarDropdownMenuLink' data-toggle='dropdown'>Reportes</a>\n"
                                                    + "<div class='dropdown-menu' aria-labelledby='navbarDropdownMenuLink'>\n"
                                                    + "<a onclick='cargarModulo(\"boleta-empalme\");' class='dropdown-item' href='#'>Emitir Detalle Complemento Servicios de Administración</a> \n"
                                                    + "<a onclick='cargarModulo(\"resumen-pagos\");' class='dropdown-item' href='#'>Resumen Detalles Complementos Servicios de Administración por Instalación</a> \n"
                                                    + "<a onclick='cargarModulo(\"consumo-cliente-remarcador\");' class='dropdown-item' href='#'>Consumo de Cliente</a> \n"
                                                    + "<a onclick='cargarModulo(\"resumen-mes-cliente\");' class='dropdown-item' href='#'>Resumen Mes Cliente</a> \n"
                                                    + "<a onclick='cargarModulo(\"resumen-mes-remarcadores\");' class='dropdown-item' href='#'>Resumen Mes Remarcadores</a> \n"
                                                    + "<a onclick='cargarModulo(\"hist-tarifa\");' class='dropdown-item' href='#'>Histórico de Tarifa</a> \n"
                                                    + "</div>"
                                                    + "</li>");
                                            out.print("<li class='nav-item dropdown'>"
                                                    + "<a class='nav-link dropdown-toggle' href='#' id='navbarDropdownMenuLink' data-toggle='dropdown'>Operaciones</a>\n"
                                                    + "<div class='dropdown-menu' aria-labelledby='navbarDropdownMenuLink'>\n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"cliente-remarcador\");' href='#'>Asignar remarcador a cliente</a> \n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"registros-mes-remarcador\");' href='#'>Descarga Registros Remarcador</a> \n"
                                                    + "</div>"
                                                    + "</li>"
                                                    + "<li class='nav-item dropdown'>"
                                                    + "<a class='nav-link dropdown-toggle' href='#' id='navbarDropdownMenuLink' data-toggle='dropdown' >Mantenedores</a>\n"
                                                    + "<div class='dropdown-menu' aria-labelledby='navbarDropdownMenuLink'>\n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"usuario\");' href='#'>Usuario</a> \n"
                                                    + "<div class='dropdown-divider'></div> \n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"instalacion\");' href='#'>Instalación</a> \n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"parque\");' href='#'>Bodega</a>\n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"empalme\");' href='#'>Empalme</a>\n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"remarcador\");' href='#'>Remarcador</a>\n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"cliente\");' href='#'>Cliente</a>\n"
                                                    + "<a class='dropdown-item' onclick='cargarModulo(\"tarifa\");' href='#'>Tarifa</a>\n"
                                                    + "</div>"
                                                    + "</li>");
                                        }
                                    }
                                %>
                            </ul>
                            <ul class="navbar-nav ml-md-auto">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown">
                                        <% out.print(session.getAttribute("nombres") + " " + session.getAttribute("appaterno"));%>
                                    </a>
                                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                                        <a class="dropdown-item" onclick="salir();" href="#">Salir</a> 
                                        <div class="dropdown-divider"></div> 
                                        <a onclick="cargarModulo('cambio-pass');" class="dropdown-item" href="#">Cambiar Contraseña</a> 
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </div>
            </div>
            <br />
            <br />
            <br />
            <div class="row" id="contenido-principal">

            </div>
        </div>
    </body>
</html>
