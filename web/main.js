function cargarModulo(nombre){
    $('#contenido-principal').load("modulos/" + nombre + ".jsp");
}

function salir(){
    var datos = {
        tipo: 'salir'
    };
    $.ajax({
        url: 'UsuarioController',
        type: 'post',
        data:{
            datos: JSON.stringify(datos)
        },
        success:function(res){
            window.location.href = "login.jsp";
        }
    });
}