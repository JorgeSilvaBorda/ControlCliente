var ID_INSTALACION_EDICION = null;
var INSTALACION_EDIT = null;
function existeInstalacion(callback1) {
    var nominstalacion = $('#nom-instalacion').val();
    var datos = {
        tipo: 'existe-instalacion',
        nominstalacion: nominstalacion
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
                if (obj.cantidad > 0) {
                    alert("El nombre de la instalación que desea ingresar, ya existe.");
                } else {
                    callback1(validarCampos);
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

function existeDireccionInstalacion(callback) {
    var direccion = $('#direccion').val();
    var idcomuna = $('#select-comuna').val();
    var datos = {
        tipo: 'existe-direccion-instalacion',
        direccion: direccion,
        idcomuna: idcomuna
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
                if (obj.cantidad > 0) {
                    alert("La dirección de la instalación que desea ingresar, ya existe.");
                    callback(false);
                } else {
                    callback(true);
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
    var nominstalacion = $('#nom-instalacion').val();
    var idcomuna = $('#select-comuna').val();
    var direccion = $('#direccion').val();

    if (nominstalacion.length < 3) { //Mínimo 3 caracteres para el nombre
        alert("Debe indicar un nombre de instalación válido (Mínimo 3 caracteres).");
        return false;
    }
    if (direccion.length < 3) {
        alert("Debe indicar una dirección válida (Mínimo 3 caracteres).");
        return false;
    }
    if (idcomuna === '0') {
        alert("Debe seleccionar una comuna del listado.");
        return false;
    }
    insInstalacion(getInstalaciones);
}

function existeInstalacionUpdate(callback1) {
    var nominstalacion = $('#nom-instalacion').val();
    var idcomuna = $('#select-comuna').val();
    var direccion = $('#direccion').val();
    INSTALACION_EDIT.newnominstalacion = nominstalacion;
    INSTALACION_EDIT.newidcomuna = idcomuna;
    INSTALACION_EDIT.newdireccion = direccion;
    var datos = {
        tipo: 'existe-instalacion-update',
        idinstalacion: ID_INSTALACION_EDICION,
        nominstalacion: INSTALACION_EDIT.nominstalacion,
        newnominstalacion: nominstalacion
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
                if (obj.cantidad > 0) {
                    alert("El nombre de la instalación que desea ingresar, ya se encuentra en uso por otra.");
                } else {
                    callback1(validarCamposUpdate);
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

function existeDireccionInstalacionUpdate(callback) {
    var direccion = $('#direccion').val();
    var idcomuna = $('#select-comuna').val();
    INSTALACION_EDIT.newidcomuna = idcomuna;
    INSTALACION_EDIT.newdireccion = direccion;
    var datos = {
        tipo: 'existe-direccion-instalacion-update',
        idinstalacion: ID_INSTALACION_EDICION,
        direccion: INSTALACION_EDIT.direccion,
        idcomuna: INSTALACION_EDIT.idcomuna,
        newdireccion: direccion,
        newidcomuna: idcomuna
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
                if (obj.cantidad > 0) {
                    alert("La dirección de la instalación que desea ingresar, ya se encuentra en uso por otra.");
                    callback(false);
                } else {
                    callback(true);
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

function validarCamposUpdate(esvalido) {
    if (!esvalido) {
        return false;
    }
    var nominstalacion = $('#nom-instalacion').val();
    var idcomuna = $('#select-comuna').val();
    var direccion = $('#direccion').val();

    if (nominstalacion.length < 3) { //Mínimo 3 caracteres para el nombre
        alert("Debe indicar un nombre de instalación válido (Mínimo 3 caracteres).");
        return false;
    }
    if (direccion.length < 3) {
        alert("Debe indicar una dirección válida (Mínimo 3 caracteres).");
        return false;
    }
    if (idcomuna === '0') {
        alert("Debe seleccionar una comuna del listado.");
        return false;
    }
    saveInstalacion(getInstalaciones);
}

function getInstalaciones() {
    var datos = {
        tipo: 'get-instalaciones'
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
                $('.dataTable').DataTable().destroy();
                $('#tabla-instalaciones tbody').html(obj.tabla);
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Instalaciones',
                        exportOptions: {
                            columns: [0, 1, 2]
                        }
                    }

                ];
                $('#tabla-instalaciones').DataTable(OPCIONES);
                $('.buttons-html5').addClass("btn-sm");
                $('.buttons-html5').addClass("btn-success");
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectComunas() {
    var datos = {
        tipo: 'get-select-comunas'
    };

    $.ajax({
        url: 'ParametrosController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-comuna').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insInstalacion(callback) {

    console.log("entra a insertar");
    var nominstalacion = $('#nom-instalacion').val();
    var direccion = $('#direccion').val();
    var idcomuna = $('#select-comuna').val();

    var datos = {
        tipo: 'ins-instalacion',
        nominstalacion: nominstalacion,
        direccion: direccion,
        idcomuna: idcomuna
    };

    $.ajax({
        url: 'InstalacionController',
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
    var idinstalacion = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-instalacion-idinstalacion',
        idinstalacion: idinstalacion
    };

    $.ajax({
        url: 'InstalacionController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarInstalacion(obj.instalacion);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarInstalacion(instalacion) {
    ID_INSTALACION_EDICION = instalacion.idinstalacion;
    $('#nom-instalacion').val(instalacion.nominstalacion);
    $('#direccion').val(instalacion.direccion);
    $('#select-comuna').val(instalacion.idcomuna);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
    INSTALACION_EDIT = instalacion;
}

function saveInstalacion(callback) {

    var idinstalacion = ID_INSTALACION_EDICION;
    var nominstalacion = $('#nom-instalacion').val();
    var direccion = $('#direccion').val();
    var idcomuna = $('#select-comuna').val();

    var datos = {
        tipo: 'upd-instalacion',
        instalacion: {
            idinstalacion: idinstalacion,
            nominstalacion: nominstalacion,
            direccion: direccion,
            idcomuna: idcomuna
        }
    };

    $.ajax({
        url: 'InstalacionController',
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
    var idinstalacion = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'del-instalacion',
        idinstalacion: idinstalacion
    };

    if (confirm("Está seguro que desea eliminar la instalación seleccionada?")) {
        $.ajax({
            url: 'InstalacionController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    getInstalaciones();
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
    ID_INSTALACION_EDICION = null;
    INSTALACION_EDIT = null;
    $('#nom-instalacion').val('');
    $('#direccion').val('');
    $('#select-comuna').val('0');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}