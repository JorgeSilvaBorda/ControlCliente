function getParques() {
    var datos = {
        tipo: 'get-parques'
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
                $('.dataTable').DataTable().destroy();
                $('#tabla-parques tbody').html(obj.tabla);
                $('#tabla-parques').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function getSelectFase() {
    var datos = {
        tipo: 'get-select-fase'
    };
    $.ajax({
        url: 'FaseController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                $('#select-fase').html(obj.options);
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}
function insParque(callback){
    var idfase = $('#select-fase').val();
    var nomparque = $('#nom-parque').val();
    
    var datos = {
        tipo: 'ins-parque',
        idfase: idfase, 
        nomparque: nomparque
    };

    $.ajax({
        url: 'ParqueController',
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
    $('#nom-parque').val('');
    $('#select-fase').val('0');
}