<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="initial-scale=1">
        <script src="modulos/cambio-pass.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
    </head>
    <body>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#rut').val('<% out.print(session.getAttribute("rut").toString() + session.getAttribute("dv").toString());%>');
                $('#rut').val($.formatRut($('#rut').val()));
            });

            function enviar() {
                if (validarCampos()) {
                    var datos = {
                        tipo: 'cambio-pass',
                        rutfull: '<% out.print(session.getAttribute("rut").toString() + session.getAttribute("dv").toString());%>',
                        claveAnterior: $('#claveAnterior').val(),
                        claveNueva: $('#claveNueva').val()
                    };
                    $.ajax({
                        type: 'post',
                        url: 'UsuarioController',
                        cache: false,
                        data: {
                            datos: JSON.stringify(datos)
                        },
                        success: function (res) {
                            var obj = JSON.parse(res);
                            if (obj.estado === 'ok') {
                                alert('Contraseña cambiada exitosamente.');
                                limpiar();
                                
                                window.location.href = './main.jsp';
                            } else if (obj.estado === 'error') {
                                alert(obj.mensaje);
                            }
                        },
                        error: function (a, b, c) {
                            console.log(a);
                            console.log(b);
                            console.log(c);
                        }
                    });
                }
            }
        </script>
        <div class="container-fluid">
            <br />
            <br />
            <br />
            <div class="row">
                <div class="col-sm-12">
                    <h2>Cambio de contraseña</h2>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-5"></div>
                <div class="col-sm-2">
                    <form>
                        <div class="form-group small">
                            <label for="rut">Rut</label>
                            <input type="text" id="rut" class="form-control form-control-sm" disabled="disabled" />
                        </div>
                        <div class="form-group small">
                            <label for="rut">Clave anterior</label>
                            <input type="password" id="claveAnterior" class="form-control form-control-sm" />
                        </div>
                        <div class="form-group small">
                            <label for="rut">Clave nueva</label>
                            <input type="password" id="claveNueva" class="form-control form-control-sm" />
                        </div>
                        <div class="form-group small">
                            <button type="button" class="btn btn-sm btn-success" onclick="enviar();" >Enviar</button>
                        </div>
                    </form>
                </div>
                <div class="col-sm-5"></div>
            </div>
        </div>
    </body>
</html>
