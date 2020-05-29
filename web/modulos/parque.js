var ID_PARQUE_EDICION = null;
var PARQUE_EDICION = null;
function getParques() {
    var datos = {
        tipo: 'get-parques'
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
                $('.dataTable').DataTable().destroy();
                $('#tabla-parques tbody').html(obj.tabla);
                $('#tabla-parques').DataTable(OPCIONES_DATATABLES);
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

function validarCamposInsert() {
    var idinstalacion = $('#select-instalacion').val();
    var nomparque = $('#nom-parque').val();

    if (idinstalacion === '' || idinstalacion === '0' || idinstalacion === 0 || idinstalacion === null || idinstalacion === undefined) {
        alert("Debe seleccionar una instalación del listado.");
        return false;
    }

    if (nomparque.length < 2) {
        alert("Debe indicar un nombre de Bodega válido (mínimo 2 caracteres).");
        return false;
    }
    return true;
}

function insParque() {
    var idinstalacion = $('#select-instalacion').val();
    var nomparque = $('#nom-parque').val();

    var datos = {
        tipo: 'ins-parque',
        idinstalacion: idinstalacion,
        nomparque: nomparque
    };

    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                limpiar();
                getParques();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idparque = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-parque-idparque',
        idparque: idparque
    };

    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarParque(obj.parque);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarParque(parque) {
    ID_PARQUE_EDICION = parque.idparque;
    $('#select-instalacion').val(parque.idinstalacion);
    $('#nom-parque').val(parque.nomparque);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
    PARQUE_EDICION = parque;
}

function saveParque() {
    var idparque = ID_PARQUE_EDICION;
    var idinstalacion = $('#select-instalacion').val();
    var nomparque = $('#nom-parque').val();

    var datos = {
        tipo: 'upd-parque',
        parque: {
            idparque: idparque,
            idinstalacion: idinstalacion,
            nomparque: nomparque
        }
    };

    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                limpiar();
                getParques();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function eliminar(boton) {
    var fila = $(boton).parent().parent();
    var idparque = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'del-parque',
        idparque: idparque
    };

    if (confirm("Está seguro que desea eliminar la bodega seleccionada?")) {
        $.ajax({
            url: 'ParqueController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    getParques();
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

function existeParqueInstalacion() {
    if (validarCamposInsert()) {
        var idinstalacion = $('#select-instalacion').val();
        var nomparque = $('#nom-parque').val();

        var datos = {
            tipo: 'existe-parque-instalacion',
            idinstalacion: idinstalacion,
            nomparque: nomparque
        };

        $.ajax({
            url: 'ParqueController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    if (parseInt(obj.cantidad) === 0) {
                        insParque();
                    } else {
                        alert("La Bodega que intenta ingresar ya se encuentra en la instalación seleccionada.");
                        return false;
                    }
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

function existeParqueInstalacionUpdate() {
    if (validarCamposInsert()) {
        var idinstalacion = $('#select-instalacion').val();
        var nomparque = $('#nom-parque').val();

        var datos = {
            tipo: 'existe-parque-instalacion-update',
            idparque: PARQUE_EDICION.idparque,
            idinstalacion: PARQUE_EDICION.idinstalacion,
            newidinstalacion: idinstalacion,
            nomparque: PARQUE_EDICION.nomparque,
            newnomparque: nomparque
        };

        $.ajax({
            url: 'ParqueController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    if (parseInt(obj.cantidad) === 0) {
                        saveParque();
                    } else {
                        alert("La Bodega que intenta ingresar ya se encuentra en la instalación seleccionada.");
                        return false;
                    }
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

function limpiar() {
    ID_PARQUE_EDICION = null;
    PARQUE_EDICION = null;
    $('#nom-parque').val('');
    $('#select-instalacion').val('0');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}