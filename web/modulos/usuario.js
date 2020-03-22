function getUsuarios() {
    var datos = {
        tipo: 'get-usuarios'
    };
    $.ajax({
        url: 'UsuarioController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success: function(resp){
            var obj = JSON.parse(resp);
            if(obj.estado === 'ok'){
                //Pintar Usuarios
                $('#tabla-usuarios tbody').html(obj.tabla);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}