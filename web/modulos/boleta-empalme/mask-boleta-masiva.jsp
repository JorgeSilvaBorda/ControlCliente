<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../../css/bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="../../css/estilos.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="../../js/datatables/datatables.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="../../js/datatables/jquery-ui.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>
        <link href="../../js/pivot.min.css" rel="stylesheet" type="text/css"/>
        <link href="../../iconic/font/css/open-iconic-bootstrap.css?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" rel="stylesheet" type="text/css"/>

        <script src="../../js/jquery-3.4.1.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/datatables/jquery-ui.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/popper.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/bootstrap.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>

        <script src="../../js/globales.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/jquery.rut.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/datatables/datatables.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/datatables/FixedHeader-3.1.4/js/fixedHeader.bootstrap4.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/datatables/Buttons-1.5.6/js/buttons.print.min.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
        <script src="../../js/funciones.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        <script src="../../js/datatables/Buttons-1.5.6/js/buttons.html5.js" type="text/javascript"></script>
        <script src="../../js/datatables/JSZip-2.5.0/jszip.js" type="text/javascript"></script>
        <script src="../../js/datatables/pdfmake-0.1.36/pdfmake.js" type="text/javascript"></script>
        <script src="../../js/datatables/pdfmake-0.1.36/vfs_fonts.js" type="text/javascript"></script>
        <script src="../../js/pivot.min.js" type="text/javascript"></script>
        <script src="../../js/Chart.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>

        <script src="../../js/html2canvas.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        <script src="../../js/pdfmake.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
        <script src="../../js/vfs_fonts.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>

        <script src="../../js/html2pdf.bundle.js" type="text/javascript"></script>

        <script src="../../modulos/boleta-empalme/mask-boleta-masiva.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
    </head>
    <script type="text/javascript">
        var jsonboletas = <%out.print(request.getParameter("jsonboletas"));%>;
        $(document).ready(function () {
            buscarBoletasMasivo(jsonboletas);
        });
    </script>
    <body>
        <div class="container-fluid" id="cuerpo">
            
        </div>
    </body>
</html>
