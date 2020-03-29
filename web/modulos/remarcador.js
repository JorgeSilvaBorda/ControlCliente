function getRemarcadores() {
    var datos = {
        tipo: 'get-remarcadores'
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
                $('#tabla-remarcadores tbody').html(obj.tabla);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectEmpalme() {
    var datos = {
        tipo: 'get-select-empalme'
    };
    $.ajax({
        url: 'EmpalmeController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-empalme').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectParqueEmpalme(idempalme){
    var datos = {
        tipo: 'get-select-parque-empalme',
        idempalme: idempalme
    };
    $.ajax({
        url: 'ParqueController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-parque').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insRemarcador(callback){
    var idempalme = $('#select-empalme').val();
    var idparque = $('#select-parque').val();
    var numremarcador = $('#num-remarcador').val();
    
    var datos = {
        tipo: 'ins-remarcador',
        idempalme: idempalme, 
        idparque: idparque,
        numremarcador: numremarcador
    };

    $.ajax({
        url: 'RemarcadorController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function(res){
            var obj = JSON.parse(res);
            if(obj.estado === 'ok'){
                limpiar();
                callback();
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function limpiar(){
    $('#num-remarcador').val('');
    $('#select-empalme').val('0');
    $('#select-parque').html('');
}