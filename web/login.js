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
    //setTest();
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

function setTest(){
    $('#rut').val('163556626');
    $('#rut').keyup();
    $('#rut').blur();
    $('#password').val('password');
    $('#password').keyup();
    $('#password').blur();
    $('#btn-login').click();
}