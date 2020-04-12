$(document).on('keyup', function (e) {
    if (e.keyCode === 13) { //Enter o click
        $('#btn-login').click();
    }
});
$(document).ready(function () {
    $('#rut').rut({
        formatOn: 'keyup',
        validateOn: 'blur'
    }).on('rutInvalido', function () {
        $('#rut').addClass("is-invalid");
        $('#rut').removeClass("is-valid");
        return false;
    }).on('rutValido', function () {
        $('#rut').addClass("is-valid");
        $('#rut').removeClass("is-invalid");
        return false;
    });
    $('#div-alert').fadeIn(500);
});


function validarCampos() {
    if ($('#rut').hasClass("is-invalid")) {
        alert('El rut ingresado no es válido.');
        return false;
    }
    if ($('#rut').val().length < 11) {
        alert('El rut ingresado no es válido.');
        return false;
    }
    if ($('#password').val().length < 6) {
        alert('La contraseña no puede contener menos de 6 caracteres.');
        return false;
    }
    return true;
}

function login() {
    if (validarCampos()) {

        var rut = $('#rut').val().replaceAll("\\.", "").split("-")[0];
        var dv = $('#rut').val().split("-")[1];
        var password = $('#password').val();
        
        var datos = {
            tipo: 'login',
            usuario: {
                rut: rut,
                dv: dv,
                password: password
            }
        };
        /**
        $.ajax({
            type: 'post',
            url: 'UsuarioController',
            data:{
                datos: JSON.stringify(datos)
            },
            success: function(resp){
                console.log(resp);
            },
            error: function(a, b, c){
                console.log(a);
                console.log(b);
                console.log(c);
            }
        });
        */
       return true;
    }
    return false;
}