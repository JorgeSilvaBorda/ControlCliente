$(document).ready(function () {
    $('.dataTable#tabla-cliente-remarcador').DataTable().destroy();
    $('#tabla-cliente-remarcador tbody').html('');
    $('#tabla-cliente-remarcador').DataTable(OPCIONES_DATATABLES);
});

function getSelectClientes() {
    var datos = {
        tipo: 'get-select-clientes-nombre'
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
                $('#select-cliente').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getRemarcadoresLibres() {
    var datos = {
        tipo: 'get-remarcadores-libres'
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
                $('.dataTable#tabla-remarcadores-libres').DataTable().destroy();
                $('#tabla-remarcadores-libres tbody').html(obj.tabla);
                $('#tabla-remarcadores-libres').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getRemarcadoresAsignadosIdCliente(idcliente) {
    var datos = {
        tipo: 'get-remarcadores-asignados-idcliente',
        idcliente: $('#select-cliente').val()
    };

    $.ajax({
        url: 'ClienteRemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('.dataTable#tabla-cliente-remarcador').DataTable().destroy();
                $('#tabla-cliente-remarcador tbody').html(obj.tabla);
                $('#tabla-cliente-remarcador').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function asignar(idremarcador) {
    var idcliente = $('#select-cliente').val();
    var datos = {
        tipo: 'asignar-remarcador-cliente',
        idcliente: idcliente,
        idremarcador: idremarcador
    };
    $.ajax({
        url: 'ClienteRemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                getRemarcadoresLibres();
                getRemarcadoresAsignadosIdCliente(idcliente);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function quitar(idremarcador, idcliente) {
    var datos = {
        tipo: 'quitar-remarcador-cliente',
        idcliente: idcliente,
        idremarcador: idremarcador
    };
    $.ajax({
        url: 'ClienteRemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                getRemarcadoresLibres();
                getRemarcadoresAsignadosIdCliente(idcliente);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}