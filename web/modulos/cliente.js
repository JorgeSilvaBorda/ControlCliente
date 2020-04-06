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
                $('.dataTable').DataTable().destroy();
                $('#tabla-clientes tbody').html(obj.tabla);
                $('#tabla-clientes').DataTable(OPCIONES_DATATABLES);
            }
        },
        error: function(a, b, c){
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}

function insCliente(callback){
    var rutcliente = $('#rut-cliente').val().replaceAll("\\.", "").split("-")[0];
    var dvcliente = $('#rut-cliente').val().split("-")[1];
    var nomcliente = $('#nomcliente').val();
    var razoncliente = $('#razoncliente').val();
    var direccion = $('#direccion').val();
    var modulos = $('#modulos').val();
    var persona = $('#persona').val();
    var cargo = $('#cargo').val();
    var fono = $('#fono').val();
    var email = $('#email').val();
    
    var datos = {
        tipo: 'ins-cliente',
        rutcliente: rutcliente,
        dvcliente: dvcliente,
        nomcliente: nomcliente,
        razoncliente: razoncliente,
        direccion: direccion,
        modulos: modulos,
        persona: persona,
        cargo: cargo,
        fono: fono,
        email: email
    };

    $.ajax({
        url: 'ClienteController',
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
    $('#rut-cliente').val('');
    $('#nomcliente').val('');
    $('#razoncliente').val('');
    $('#direccion').val('');
    $('#modulos').val('');
    $('#persona').val('');
    $('#cargo').val('');
    $('#fono').val('');
    $('#email').val('');
}