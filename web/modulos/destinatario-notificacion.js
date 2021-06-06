function getDestinatarios() {
    var datos = {
        tipo: 'get-destinatarios'
    };
    $.ajax({
        url: 'DestinatarioController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-destinatarios tbody').html(obj.tabla);
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Destinatarios',
                        exportOptions: {
                            columns: [0, 1]
                        }
                    }

                ];
                $('#tabla-destinatarios').DataTable(OPCIONES);
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

function validarCampos() {
    var nombres = $('#nombres').val();
    var email = $('#email').val();

    if (nombres.length < 2) {
        alert("Debe ingresar un nombre para el destinatario.");
        return false;
    }
    if (email.length < 6 || (email.indexOf("@") === -1)) {
        alert("Debe ingresar un email válido para el destinatario.");
        return false;
    }

    return true;
}

function insDestinatario() {

    if (validarCampos()) {
        var idinstalacion = $('#select-instalacion').val();
        var nombres = $('#nombres').val();
        var email = $('#email').val();

        var datos = {
            tipo: 'ins-destinatario',
            nombres: nombres,
            email: email,
            idinstalacion: idinstalacion
        };

        $.ajax({
            url: 'DestinatarioController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiar();
                    getDestinatarios();
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

function eliminar(iddestinatario) {

    if (confirm("¿Está seguro de que desea eliminar el destinatario seleccionado?\nDejará de ser notificado ante eventos de comunicación de los remarcadores.")) {

        var datos = {
            tipo: 'del-destinatario',
            iddestinatario: iddestinatario

        };

        $.ajax({
            url: 'DestinatarioController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiar();
                    getDestinatarios();
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
    $('#nombres').val('');
    $('#email').val('');
}