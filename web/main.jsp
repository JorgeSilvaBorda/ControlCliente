<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="js/jquery-3.4.1.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/bootstrap.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="main.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/jquery.rut.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="js/funciones.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <link href="css/bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="css/estilos.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <nav class="navbar navbar-expand-lg navbar-light bg-light navbar-dark bg-dark navbar-fixed fixed-top">

                        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                            <span class="navbar-toggler-icon"></span>
                        </button> <a class="navbar-brand" href="#">Bodenor</a>
                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                            <ul class="navbar-nav">
                                 <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="" id="navbarDropdownMenuLink" data-toggle="dropdown">Reportes</a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                        <a class="dropdown-item" href="#">Emisión Boleta</a> 
                                        <a class="dropdown-item" href="#">Consumo Cliente por remarcador</a> 
                                        <a class="dropdown-item" href="#">Consumo Total Cliente</a> 
                                    </div>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="" id="navbarDropdownMenuLink" data-toggle="dropdown">Operaciones</a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                        <a class="dropdown-item" href="#">Asignar remarcador a cliente</a> 
                                    </div>
                                </li>
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="" id="navbarDropdownMenuLink" data-toggle="dropdown">Mantenedores</a>
                                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                        <a class="dropdown-item" onclick="cargarModulo('usuario');" href="#">Usuario</a> 
                                        <div class="dropdown-divider"></div> 
                                        <a class="dropdown-item" onclick="cargarModulo('fase');" href="#">Fase</a> 
                                        <a class="dropdown-item" onclick="cargarModulo('parque');" href="#">Parque</a>
                                        <a class="dropdown-item" onclick="cargarModulo('empalme');" href="#">Empalme</a>
                                        <a class="dropdown-item" onclick="cargarModulo('remarcador');" href="#">Remarcador</a>
                                        <a class="dropdown-item" onclick="cargarModulo('cliente');" href="#">Cliente</a>
                                        <a class="dropdown-item" onclick="cargarModulo('tarifa');" href="#">Tarifa</a>
                                    </div>
                                </li>
                            </ul>
                            <ul class="navbar-nav ml-md-auto">
                                <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown">Usuario</a>
                                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
                                        <a class="dropdown-item" href="#">Cerrar Sesión</a> 
                                        <div class="dropdown-divider"></div> 
                                        <a class="dropdown-item" href="#">Cambiar Contraseña</a> 
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
