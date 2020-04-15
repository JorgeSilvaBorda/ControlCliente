

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

function buscar(){
    
    var idcliente = $('#select-cliente').val();
    var fechaini = $('#fecha-ini').val();
    var fechafin = $('#fecha-fin').val();
    
    var datos = {
        tipo: 'armar-pre-boleta',
        idcliente: idcliente,
        fechaini: fechaini,
        fechafin: fechafin
    };
    
    $.ajax({
        url: 'BoletaController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                console.log(obj.preboleta);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

