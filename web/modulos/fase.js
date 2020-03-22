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