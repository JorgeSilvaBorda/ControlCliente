<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="js/jquery-3.4.1.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.js" type="text/javascript"></script>
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <link href="css/estilos.css" rel="stylesheet" type="text/css"/>
        
        
        <title>Control Cliente</title>
    </head>
    <body>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#contenido').load("login.jsp");
            });
        </script>
        <div id="contenido" class="container-fluid"></div>
    </body>
</html>
