ID_REMARCADOR_EDICION = null;

function getRemarcadores() {
    var datos = {
        tipo: 'get-remarcadores'
    };
    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-remarcadores tbody').html(obj.tabla);
                $('#tabla-remarcadores').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectInstalacion() {
    var datos = {
        tipo: 'get-select-instalacion'
    };
    $.ajax({
        url: 'InstalacionController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-instalacion').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectEmpalmeIdParque(idparque) {
    var datos = {
        tipo: 'get-select-empalme-idparque',
        idparque: idparque
    };
    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-empalme').html('');
                $('#select-empalme').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectEmpalmeIdParqueAutoSelect(idparque, valautoselect) {
    var datos = {
        tipo: 'get-select-empalme-idparque',
        idparque: idparque
    };
    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-empalme').html('');
                $('#select-empalme').html(obj.options);
                $('#select-empalme').val(valautoselect);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectBodegaIdInstalacion(idinstalacion) {
    var datos = {
        tipo: 'get-select-parque-idinstalacion',
        idinstalacion: idinstalacion
    };
    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-parque').html('');
                $('#select-parque').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectBodegaIdInstalacionAutoSelect(idinstalacion, valautoselect) {
    var datos = {
        tipo: 'get-select-parque-idinstalacion',
        idinstalacion: idinstalacion
    };
    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-parque').html('');
                $('#select-parque').html(obj.options);
                $('#select-parque').val(valautoselect);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function validarCampos() {
    var idempalme = $('#select-empalme').val();
    var idparque = $('#select-parque').val();
    var numremarcador = $('#num-remarcador').val();
    var numserie = $('#num-serie').val();
    var modulos = $('#modulos').val();
    if (idempalme === '' || idempalme === '0') {
        alert("Debe seleccionar un empalme.");
        return false;
    }
    if (idparque === '' || idparque === '0') {
        alert("Debe seleccionar una bodega.");
        return false;
    }
    if (numremarcador.length < 1) {
        alert("Debe indicar un número de remarcador válido (Largo mínimo 1).");
        return false;
    }
    if (numserie.length < 2) {
        alert("Debe indicar un número de serie válido (Largo mínimo 2).");
        return false;
    }
    return true;
}

function insRemarcador(callback) {
    var idempalme = $('#select-empalme').val();
    var idparque = $('#select-parque').val();
    var numremarcador = $('#num-remarcador').val();
    var numserie = $('#num-serie').val();
    var modulos = $('#modulos').val();
    var marca = $('#marca').val();
    var modelo = $('#modelo').val();

    var datos = {
        tipo: 'ins-remarcador',
        idempalme: idempalme,
        idparque: idparque,
        numremarcador: numremarcador,
        numserie: numserie,
        modulos: modulos,
        marca: marca,
        modelo: modelo
    };

    if (validarCampos()) {
        $.ajax({
            url: 'RemarcadorController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiarAfterInsert();
                    callback();
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

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idremarcador = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-remarcador-idremarcador',
        idremarcador: idremarcador
    };

    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            console.log(obj);
            if (obj.estado === 'ok') {
                armarRemarcador(obj.remarcador);
                $('#select-instalacion').val(obj.remarcador.idinstalacion);
                getSelectBodegaIdInstalacionAutoSelect(obj.remarcador.idinstalacion, obj.remarcador.idparque);
                getSelectEmpalmeIdParqueAutoSelect(obj.remarcador.idparque, obj.remarcador.idempalme);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function saveRemarcador(callback) {
    var idremarcador = ID_REMARCADOR_EDICION;
    var idempalme = $('#select-empalme').val();
    var idparque = $('#select-parque').val();
    var numremarcador = $('#num-remarcador').val();
    var numserie = $('#num-serie').val();
    var modulos = $('#modulos').val();
    var marca = $('#marca').val();
    var modelo = $('#modelo').val();

    var datos = {
        tipo: 'upd-remarcador',
        remarcador: {
            idremarcador: idremarcador,
            idempalme: idempalme,
            idparque: idparque,
            numremarcador: numremarcador,
            numserie: numserie,
            modulos: modulos,
            marca: marca,
            modelo: modelo
        }
    };
    if (validarCampos()) {
        $.ajax({
            url: 'RemarcadorController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiar();
                    callback();
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

function armarRemarcador(remarcador) {
    ID_REMARCADOR_EDICION = remarcador.idremarcador;
    $('#num-remarcador').val(remarcador.numremarcador);
    $('#marca').val(remarcador.marca);
    $('#modelo').val(remarcador.modelo);
    $('#num-serie').val(remarcador.numserie);
    $('#select-empalme').val(remarcador.idempalme);
    $('#modulos').val(remarcador.modulos);
    $('#select-empalme').change();
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function limpiar() {
    ID_REMARCADOR_EDICION = null;
    $('#select-instalacion').val('0');
    $('#num-remarcador').val('');
    $('#num-serie').val('');
    $('#marca').val('');
    $('#modelo').val('');
    $('#select-empalme').html('');
    $('#modulos').val('');
    $('#select-parque').html('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}

function limpiarAfterInsert() {
    ID_REMARCADOR_EDICION = null;
    $('#num-remarcador').val('');
    $('#marca').val('');
    $('#modelo').val('');
    $('#num-serie').val('');
    $('#modulos').val('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}

function eliminar(boton) {
    var fila = $(boton).parent().parent();
    var idremarcador = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'del-remarcador',
        idremarcador: idremarcador
    };

    if (confirm("Está seguro que desea eliminar el remarcador seleccionado?")) {
        $.ajax({
            url: 'RemarcadorController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    getRemarcadores();
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