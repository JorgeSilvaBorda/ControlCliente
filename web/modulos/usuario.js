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

function insUsuario(callback){
    var idtipousuario = $('#select-tipo-usuario').val(); 
    var rutusuario = $('#rut-usuario').val().replaceAll("\\.", "").split("-")[0];
    var dvusuario = $('#rut-usuario').val().split("-")[1];
    var nombres = $('#nombres').val();
    var appaterno = $('#ap-paterno').val();
    var apmaterno = $('#ap-materno').val();
    
    var datos = {
        tipo: 'ins-usuario',
        idtipousuario: idtipousuario,
        rutusuario: rutusuario,
        dvusuario: dvusuario,
        nombres: nombres,
        appaterno: appaterno,
        apmaterno: apmaterno
    };

    $.ajax({
        url: 'UsuarioController',
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
    $('#select-tipo-usuario').val('0'); 
    $('#rut-usuario').val('');
    $('#nombres').val('');
    $('#ap-paterno').val('');
    $('#ap-materno').val('');
}