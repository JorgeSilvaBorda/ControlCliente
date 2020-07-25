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
                getSelectContactosIdCliente();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectContactosIdCliente(){
    var idcliente = $('#select-cliente').val();
    var datos = {
        tipo: 'get-contactos-idcliente',
        idcliente: idcliente
    };
    $.ajax({
        url: 'ContactoController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-contacto').html(obj.options);
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
    var idcontacto = $('#select-contacto').val();
    if(idcontacto === 0 || idcontacto === '0' || idcontacto === '' || idcontacto === null || idcontacto === undefined){
        alert("Debe seleccionar un contacto del listado para poder asignar el remarcador.");
        return false;
    }
    var idcliente = $('#select-cliente').val();
    var datos = {
        tipo: 'asignar-remarcador-cliente',
        idcliente: idcliente,
        idremarcador: idremarcador,
        idcontacto: idcontacto
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
    if (confirm("Está seguro que desea quitar este remarcador del cliente?")) {
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

}

function verAsignados() {
    var datos = {
        tipo: 'get-remarcadores-asignados'
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
                $('#modal-title').html("Remarcadores asignados");

                $('.dataTable#tabla-remarcadores-asignados').DataTable().destroy();
                $('#modal-body').html(obj.tabla);
                $('#tabla-remarcadores-asignados').DataTable(OPCIONES_DATATABLES);
                $('#modal').modal('show');

            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}