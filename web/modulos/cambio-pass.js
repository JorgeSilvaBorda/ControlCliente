function validarCampos() {
    var claveAnterior = $('#claveAnterior').val();
    var claveNueva = $('#claveNueva').val();
    if (claveAnterior.length < 4 || claveNueva.lentgh < 4) {
        alert('Las claves ingresadas no pueden tener un largo menor a 4 caracteres');
        return false;
    }
    return true;
}

function limpiar() {
    $('#claveAnterior').val('');
    $('#claveNueva').val('');
}
        