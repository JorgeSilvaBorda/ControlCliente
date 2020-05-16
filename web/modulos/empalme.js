var ID_EMPALME_EDICION = null;
var EMPALME_EDIT = null;
function existeEmpalmeInstalacion(callback1) {
    var numempalme = $('#num-empalme').val();
    var idinstalacion = $('#select-instalacion').val();
    var idparque = $('#select-bodega').val();
    var datos = {
        tipo: 'existe-empalme-instalacion',
        idinstalacion: idinstalacion,
        idparque: idparque,
        numempalme: numempalme
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
                console.log(obj);
                if (obj.cantidad > 0) {
                    alert("El número de empalme que desea ingresar ya existe en la instalación asociado a la bodega selecionada.");
                } else {
                    callback1(true);
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

function existeEmpalmeInstalacionUpdate(callback1) {
    var idinstalacion = $('#select-instalacion').val();
    var newnumempalme = $('#num-empalme').val();
    var idparque = $('#select-bodega').val();
    var datos = {
        tipo: 'existe-empalme-instalacion-update',
        idempalme: ID_EMPALME_EDICION,
        newidinstalacion: idinstalacion,
        newidparque: idparque,
        newnumempalme: newnumempalme
    };
    console.log(datos);
    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                console.log(obj);
                if (obj.cantidad > 0) {
                    alert("El número de empalme que desea ingresar, ya se encuentra en la instalación seleccionada asociada a la bodega indicada.");
                } else {
                    callback1(true);
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

function validarCampos(esvalido) {
    if (!esvalido) {
        return false;
    }
    var idinstalacion = $('#select-instalacion').val();
    var idparque = $('#select-bodega').val();
    var idred = $('#select-red').val();
    var numempalme = $('#num-empalme').val();

    if (idinstalacion === '0') {
        alert("Debe seleccionar una instalacion del listado.");
        return false;
    }
    if (idparque === '0') {
        alert("Debe seleccionar una bodega del listado.");
        return false;
    }
    if (idred === '0') {
        alert("Debe seleccionar una red del listado.");
        return false;
    }
    if (numempalme.length < 3) { //Mínimo 3 caracteres para el número del empalme
        alert("Debe indicar un número de empalme válido (Mínimo 3 caracteres).");
        return false;
    }
    insEmpalme(getEmpalmes);
}

function validarCamposUpdate(esvalido) {
    if (!esvalido) {
        return false;
    }
    var idinstalacion = $('#select-instalacion').val();
    var idparque = $('#select-bodega').val();
    var idred = $('#select-red').val();
    var numempalme = $('#num-empalme').val();

    if (idinstalacion === '0') {
        alert("Debe seleccionar una instalacion del listado.");
        return false;
    }
    if (idparque === '0') {
        alert("Debe seleccionar una bodega del listado.");
        return false;
    }
    if (idred === '0') {
        alert("Debe seleccionar una red del listado.");
        return false;
    }
    if (numempalme.length < 3) { //Mínimo 3 caracteres para el número del empalme
        alert("Debe indicar un número de empalme válido (Mínimo 3 caracteres).");
        return false;
    }
    saveEmpalme(getEmpalmes);
}

function getEmpalmes() {
    var datos = {
        tipo: 'get-empalmes'
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
                $('.dataTable').DataTable().destroy();
                $('#tabla-empalmes tbody').html(obj.tabla);
                $('#tabla-empalmes').DataTable(OPCIONES_DATATABLES);
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

function getSelectBodegasInstalacion() {
    var idinstalacion = $('#select-instalacion').val();
    var datos = {
        tipo: 'get-select-bodegas-instalacion',
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
                $('#select-bodega').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectRed() {
    var datos = {
        tipo: 'get-select-red'
    };
    $.ajax({
        url: 'RedController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-red').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectBodegasInstalacionEdicion(idparque) {
    var idinstalacion = $('#select-instalacion').val();
    var datos = {
        tipo: 'get-select-bodegas-instalacion',
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
                $('#select-bodega').html(obj.options);
                $('#select-bodega').val(idparque);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insEmpalme(callback) {
    var idinstalacion = $('#select-instalacion').val();
    var idparque = $('#select-bodega').val();
    var idred = $('#select-red').val();
    var numempalme = $('#num-empalme').val();

    var datos = {
        tipo: 'ins-empalme',
        idinstalacion: idinstalacion,
        idparque: idparque,
        idred: idred,
        numempalme: numempalme
    };

    $.ajax({
        url: 'EmpalmeController',
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

function activarEdicion(boton) {
    var fila = $(boton).parent().parent();
    var idempalme = $(fila).children(0).children(0).val();
    ID_EMPALME_EDICION = idempalme;
    var datos = {
        tipo: 'get-empalme-idempalme',
        idempalme: idempalme
    };

    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarEmpalme(obj.empalme); 
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarEmpalme(empalme) {
    ID_EMPALME_EDICION = empalme.idempalme;
    $('#select-instalacion').val(empalme.idinstalacion);
    $('#select-red').val(empalme.idred);
    //$('#select-instalacion').change();
    getSelectBodegasInstalacionEdicion(empalme.idparque);
    $('#num-empalme').val(empalme.numempalme);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
    EMPALME_EDIT = empalme;
}

function saveEmpalme(callback) {
    var idempalme = ID_EMPALME_EDICION;
    var idinstalacion = $('#select-instalacion').val();
    var idparque = $('#select-bodega').val();
    var idred = $('#select-red').val();
    var numempalme = $('#num-empalme').val();

    var datos = {
        tipo: 'upd-empalme',
        empalme: {
            idempalme: idempalme,
            idinstalacion: idinstalacion,
            idparque: idparque,
            idred: idred,
            numempalme: numempalme
        }
    };

    $.ajax({
        url: 'EmpalmeController',
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

function eliminar(boton) {
    var fila = $(boton).parent().parent();
    var idempalme = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'del-empalme',
        idempalme: idempalme
    };
    if (confirm("Está seguro que desea eliminar el Empalme seleccionado?")) {
        $.ajax({
            url: 'EmpalmeController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    getEmpalmes();
                    limpiar();
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
    ID_EMPALME_EDICION = null;
    EMPALME_EDIT = null;
    $('#num-empalme').val('');
    $('#select-instalacion').val('0');
    $('#select-red').val('0');
    $('#select-bodega').html('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}