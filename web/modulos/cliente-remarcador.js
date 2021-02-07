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

function getContactosClienteIdCliente(idcliente) {
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
                $('#select-contacto-reasig').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectClienteReasig() {
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
                $('#select-cliente-reasig').html(obj.options);
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
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Remarcadores-libres',
                        exportOptions: {
                            columns: [0, 1, 2, 3, 4, 5, 6]
                        }
                    }

                ];
                $('#tabla-remarcadores-libres').DataTable(OPCIONES);
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

function getRemarcadoresAsignadosIdCliente(idcliente) {
    var datos = {
        tipo: 'get-remarcadores-asignados-idcliente',
        idcliente: $('#select-cliente').val()
    };
    var nomcliente = $('#select-cliente option:selected').text();
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
                var OPCIONES = OPCIONES_DATATABLES;
                OPCIONES.dom = 'Bfrtip';
                OPCIONES.buttons = [
                    {
                        extend: 'excelHtml5',
                        title: 'Remarcadores-' + nomcliente,
                        exportOptions: {
                            columns: [0, 1, 2, 3, 4, 5, 6, 7]
                        }
                    }

                ];
                $('#tabla-cliente-remarcador').DataTable(OPCIONES);
                $('.buttons-html5').addClass("btn-sm");
                $('.buttons-html5').addClass("btn-success");
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

function getSelectContactosIdCliente() {
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
    if (idcontacto === 0 || idcontacto === '0' || idcontacto === '' || idcontacto === null || idcontacto === undefined) {
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

function panelReAsignacion(idremarcador, idclienteanterior) {
    var datos = {
        tipo: "get-last-asignacion-remarcador-cliente",
        idremarcador: idremarcador,
        idclienteanterior: idclienteanterior
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
                $('#modal-title').html("Remarcadores asignados");
                $('.dataTable#tabla-remarcadores-asignados').DataTable().destroy();
                $('#modal-body').html(obj.tabla);

                $('#modal').modal('show');
                getSelectClienteReasig();
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function validaReAsignacion() {
    var nuevoidcliente = $('#select-cliente-reasig').val();
    var idclienteanterior = $('#hid-id-cliente-anterior').val();
    var idcontacto = $('#select-contacto-reasig').val();
    
    if (nuevoidcliente === 0 || nuevoidcliente === '0') {
        alert('Debe seleccionar un cliente del listado para reasignar el remarcador.');
        return false;
    }
    if (parseInt(nuevoidcliente) === parseInt(idclienteanterior)) {
        alert('El nuevo cliente no puede ser el mismo que el anterior.');
        return false;
    }
    if(idcontacto === 0 || idcontacto === '0'){
        alert('Debe seleccionar un contacto de cliente para signar el remarcador.');
        return false;
    }
    return true;
}

function reasignarRemarcador() {

    if (validaReAsignacion()) {
        if (confirm("Si se reasigna el remarcador, dejarán de efectuarse cálculos para el cliente actual y comenzará a registrar para el nuevo.\n¿Está seguro que desea continuar?")) {
            var idcliente = $('#select-cliente-reasig').val();
            var idremarcador = $('#hid-id-remarcador').val();
            var idclienteanterior = $('#hid-id-cliente-anterior').val();
            var idcontacto = $('#select-contacto-reasig').val();

            var datos = {
                tipo: 'reasignar-remarcador-cliente',
                idcliente: idcliente,
                idremarcador: idremarcador,
                idclienteanterior: idclienteanterior,
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
                        getRemarcadoresAsignadosIdCliente(idclienteanterior);
                        $('#btn-cerrar-modal').click();
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


}