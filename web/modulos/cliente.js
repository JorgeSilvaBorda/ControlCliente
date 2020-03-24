function getClientes() {
    var datos = {
        tipo: 'get-clientes'
    };
    $.ajax({
        url: 'ClienteController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success: function(resp){
            var obj = JSON.parse(resp);
            if(obj.estado === 'ok'){
                //Pintar Usuarios
                $('#tabla-clientes tbody').html(obj.tabla);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}