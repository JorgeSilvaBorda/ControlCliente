function validarCampos() {
    var claveAnterior = $('#claveAnterior').val();
    var claveNueva = $('#claveNueva').val();
    if (claveAnterior.length < 6 || claveNueva.lentgh < 6) {
        alert('Las claves ingresadas no pueden tener un largo menor a 6 caracteres');
        return false;
    }
    return true;
}

function limpiar() {
    $('#claveAnterior').val('');
    $('#claveNueva').val('');
}
        