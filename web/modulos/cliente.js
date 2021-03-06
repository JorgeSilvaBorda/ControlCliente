var ID_CLIENTE_EDICION = null;

function getClientes() {
    var datos = {
        tipo: 'get-clientes'
    };
    $.ajax({
        url: 'ClienteController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable').DataTable().destroy();
                $('#tabla-clientes tbody').html(obj.tabla);
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Clientes',
                        exportOptions: {
                            columns: [0, 1, 2]
                        }
                    }

                ];
                $('#tabla-clientes').DataTable(OPCIONES);
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

function insCliente(callback) {
    var rutcliente = $('#rut-cliente').val().replaceAll("\\.", "").split("-")[0];
    var dvcliente = $('#rut-cliente').val().split("-")[1];
    var nomcliente = $('#nomcliente').val();
    var razoncliente = $('#razoncliente').val();

    var datos = {
        tipo: 'ins-cliente',
        rutcliente: rutcliente,
        dvcliente: dvcliente,
        nomcliente: nomcliente,
        razoncliente: razoncliente
    };

    $.ajax({
        url: 'ClienteController',
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
    var idcliente = $(fila).children(0).children(0).val();

    var datos = {
        tipo: 'get-cliente-idcliente',
        idcliente: idcliente
    };

    $.ajax({
        url: 'ClienteController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            var obj = JSON.parse(res);
            if (obj.estado === 'ok') {
                armarCliente(obj.cliente);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function armarCliente(cliente) {
    ID_CLIENTE_EDICION = cliente.idcliente;
    var nomcliente = cliente.nomcliente;
    var razoncliente = cliente.razoncliente;
    $('#rut-cliente').val(cliente.rutfullcliente);
    $('#rut-cliente').keyup();
    $('#nomcliente').val(nomcliente);
    $('#razoncliente').val(razoncliente);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveCliente(callback) {
    var idcliente = ID_CLIENTE_EDICION;
    var rutcliente = $('#rut-cliente').val().replaceAll("\\.", "").split("-")[0];
    var dvcliente = $('#rut-cliente').val().split("-")[1];
    var nomcliente = $('#nomcliente').val();
    var razoncliente = $('#razoncliente').val();

    var datos = {
        tipo: 'upd-cliente',
        cliente: {
            idcliente: idcliente,
            rutcliente: rutcliente,
            dvcliente: dvcliente,
            nomcliente: nomcliente,
            razoncliente: razoncliente
        }
    };

    $.ajax({
        url: 'ClienteController',
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
    var idcliente = $(fila).children(0).children(0).val();

    if (confirm("Está seguro de que desea eliminar el cliente seleccionado?")) {

        var datos = {
            tipo: 'del-cliente',
            cliente: {
                idcliente: idcliente
            }
        };

        $.ajax({
            url: 'ClienteController',
            type: 'post',
            data: {
                datos: JSON.stringify(datos)
            },
            success: function (res) {
                var obj = JSON.parse(res);
                if (obj.estado === 'ok') {
                    limpiar();
                    getClientes();
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
    ID_CLIENTE_EDICION = null;
    $('#rut-cliente').val('');
    $('#nomcliente').val('');
    $('#razoncliente').val('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}