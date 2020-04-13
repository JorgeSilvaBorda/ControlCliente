function getSelectClientes() {
    var datos = {
        tipo: 'get-select-clientes'
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

function getSelectRemarcadoresCliente(idcliente) {
    var datos = {
        tipo: 'get-select-remarcadores-cliente',
        idcliente: idcliente
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
                $('#select-remarcador').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function graficar(idremarcador) {
    var idcliente = $('#select-cliente').val();
    var datos = {
        tipo: 'consumo-cliente-remarcador',
        idcliente: idcliente,
        idremarcador: idremarcador
    };

    $.ajax({
        url: 'ReportesController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                console.log(resp);
                new Chart(document.getElementById("line-chart"), {
                    type: 'line',
                    data: obj.data,
                    options: {
                        title: {
                            display: true,
                            text: 'Últimos 100 registros del remarcador'
                        }
                    }
                });
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}