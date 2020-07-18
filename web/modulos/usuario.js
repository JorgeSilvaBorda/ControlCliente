var ID_USUARIO_EDICION = null;

function getUsuarios() {
    var datos = {
        tipo: 'get-usuarios'
    };
    $.ajax({
        url: 'UsuarioController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-usuarios tbody').html(obj.tabla);
                $('#tabla-usuarios').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insUsuario(callback) {
    var idtipousuario = $('#select-tipo-usuario').val();
    var rutusuario = $('#rut-usuario').val().replaceAll("\\.", "").split("-")[0];
    var dvusuario = $('#rut-usuario').val().split("-")[1];
    var nombres = $('#nombres').val();
    var appaterno = $('#ap-paterno').val();
    var apmaterno = $('#ap-materno').val();

    var datos = {
        tipo: 'ins-usuario',
        idtipousuario: idtipousuario,
        rutusuario: rutusuario,
        dvusuario: dvusuario,
        nombres: nombres,
        appaterno: appaterno,
        apmaterno: apmaterno
    };

    $.ajax({
        url: 'UsuarioController',
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
    var idusuario = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-usuario-idusuario',
        idusuario: idusuario
    };

    $.ajax({
        url: 'UsuarioController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarUsuario(obj.usuario);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarUsuario(usuario) {
    ID_USUARIO_EDICION = usuario.idusuario;
    $('#select-tipo-usuario').val(usuario.idtipousuario);
    $('#rut-usuario').val(usuario.rut + usuario.dv);
    $('#rut-usuario').keyup();
    $('#nombres').val(usuario.nombres);
    $('#ap-paterno').val(usuario.appaterno);
    $('#ap-materno').val(usuario.apmaterno);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveUsuario(callback) {
    var idtipousuario = $('#select-tipo-usuario').val();
    var rutusuario = $('#rut-usuario').val().replaceAll("\\.", "").split("-")[0];
    var dvusuario = $('#rut-usuario').val().split("-")[1];
    var nombres = $('#nombres').val();
    var appaterno = $('#ap-paterno').val();
    var apmaterno = $('#ap-materno').val();

    var datos = {
        tipo: 'upd-usuario',
        usuario: {
            idusuario: ID_USUARIO_EDICION,
            idtipousuario: idtipousuario,
            rutusuario: rutusuario,
            dvusuario: dvusuario,
            nombres: nombres,
            appaterno: appaterno,
            apmaterno: apmaterno
        }
    };

    $.ajax({
        url: 'UsuarioController',
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
    var idusuario = $(fila).children(0).children(0).val();

    if (confirm("Está seguro que desea eliminar este Usuario?")) {
        var datos = {
            tipo: 'del-usuario',
            idusuario: idusuario
        };

        $.ajax({
            url: 'UsuarioController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    getUsuarios();
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
    ID_USUARIO_EDICION = null;
    $('#select-tipo-usuario').val('0');
    $('#rut-usuario').val('');
    $('#nombres').val('');
    $('#ap-paterno').val('');
    $('#ap-materno').val('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}