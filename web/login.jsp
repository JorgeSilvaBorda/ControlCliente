<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script type="text/javascript">
    function login() {
        
        window.location.href = "main.jsp";
        return false;
        
        var datos = {
            tipo: 'login-usuario',
            rutusuario: $('#rut').val(),
            password: $('#password').val()
        };
        $.ajax({
            url: 'UsuarioController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function(response){
                window.location.href = "main.jsp";
            },
            error: function(a, b, c){
                console.log(a);
                console.log(b);
                console.log(c);
            }
        });
    }
</script>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <br />
            <br />
            <br />
        </div>
    </div>
    <div class="row">
        <div class="col-sm-5">
        </div>
        <div class="col-sm-2 mx-auto">
            <form role="form" class="sombra-caja-1">
                <div class="form-group-sm">
                    <label for="rut" class="small">
                        Rut Usuario
                    </label>
                    <input type="text" class="form-control form-control-sm" id="rut" />
                </div>
                <div class="form-group-sm">
                    <label for="password" class="small">
                        Password
                    </label>
                    <input type="password" class="form-control form-control-sm" id="password" />
                </div>
                <div class="form-group form-group-sm">
                    <br />
                    <button type="button" onclick="login();" class="btn btn-primary btn-sm">
                        Ingresar
                    </button>
                </div>
            </form>
        </div>
        <div class="col-sm-5">
        </div>
    </div>
</div>
