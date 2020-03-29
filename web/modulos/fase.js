function getFases() {
    var datos = {
        tipo: 'get-fases'
    };
    $.ajax({
        url: 'FaseController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success: function(resp){
            var obj = JSON.parse(resp);
            if(obj.estado === 'ok'){
                $('#tabla-fases tbody').html(obj.tabla);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insFase(callback){
    var nomfase = $('#nom-fase').val();
    
    var datos = {
        tipo: 'ins-fase',
        nomfase: nomfase
    };

    $.ajax({
        url: 'FaseController',
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
    $('#nom-fase').val('');
}