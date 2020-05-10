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
                $('#tabla-clientes').DataTable(OPCIONES_DATATABLES);
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
    var direccion = $('#direccion').val();
    var persona = $('#persona').val();
    var cargo = $('#cargo').val();
    var fono = $('#fono').val();
    var email = $('#email').val();

    var datos = {
        tipo: 'ins-cliente',
        rutcliente: rutcliente,
        dvcliente: dvcliente,
        nomcliente: nomcliente,
        razoncliente: razoncliente,
        direccion: direccion,
        persona: persona,
        cargo: cargo,
        fono: fono,
        email: email
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
    var direccion = cliente.direccion;
    var persona = cliente.persona;
    var cargo = cliente.cargo;
    var fono = cliente.fono;
    var email = cliente.email;
    $('#rut-cliente').val(cliente.rutfullcliente);
    $('#rut-cliente').keyup();
    $('#nomcliente').val(nomcliente);
    $('#razoncliente').val(razoncliente);
    $('#direccion').val(direccion);
    $('#persona').val(persona);
    $('#cargo').val(cargo);
    $('#fono').val(fono);
    $('#email').val(email);
    $('#btn-insert').attr("hidden", "hidden");
    $('#btn-guardar').removeAttr("hidden");
}

function saveCliente(callback) {
    var idcliente = ID_CLIENTE_EDICION;
    var rutcliente = $('#rut-cliente').val().replaceAll("\\.", "").split("-")[0];
    var dvcliente = $('#rut-cliente').val().split("-")[1];
    var nomcliente = $('#nomcliente').val();
    var razoncliente = $('#razoncliente').val();
    var direccion = $('#direccion').val();
    var persona = $('#persona').val();
    var cargo = $('#cargo').val();
    var fono = $('#fono').val();
    var email = $('#email').val();

    var datos = {
        tipo: 'upd-cliente',
        cliente: {
            idcliente: idcliente,
            rutcliente: rutcliente,
            dvcliente: dvcliente,
            nomcliente: nomcliente,
            razoncliente: razoncliente,
            direccion: direccion,
            persona: persona,
            cargo: cargo,
            fono: fono,
            email: email
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

function limpiar() {
    ID_CLIENTE_EDICION = null;
    $('#rut-cliente').val('');
    $('#nomcliente').val('');
    $('#razoncliente').val('');
    $('#direccion').val('');
    $('#persona').val('');
    $('#cargo').val('');
    $('#fono').val('');
    $('#email').val('');
    $('#btn-guardar').attr("hidden", "hidden");
    $('#btn-insert').removeAttr("hidden");
}