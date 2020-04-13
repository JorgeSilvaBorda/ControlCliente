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

function buscar(idcliente) {
    var datos = {
        tipo: 'resumen-mes-cliente',
        idcliente: idcliente
    };

    $.ajax({
        url: 'ReportesController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            console.log(resp);
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                for(var i in obj.data.datasets){
                    obj.data.datasets[i].borderColor = colorDinamico();
                }
                new Chart(document.getElementById("line-chart"), {
                    type: 'line',
                    data: obj.data,
                    options: {
                        title: {
                            display: true,
                            text: 'Consumo Registrado Mes Actual'
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