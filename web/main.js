function cargarModulo(nombre) {
    $('#contenido-principal').load("modulos/" + nombre + ".jsp");
}

function salir() {
    var datos = {
        tipo: 'salir'
    };
    $.ajax({
        url: 'UsuarioController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (res) {
            window.location.href = "login.jsp";
        }
    });
}

function actualizaNuevosEventos() {
    var datos = {
        tipo: 'actualiza-nuevos-eventos'
    };
    $.ajax({
        url: 'EventosController',
        type: 'post',
        data: {
            datos: JSON.stringify(datos)
        },
        success: function (resp) {
            var obj = JSON.parse(resp);
            if (obj.estado === 'ok') {
                var comunicacion = parseInt(obj.comunicacion);
                var continuidad = parseInt(obj.continuidad);
                var total = parseInt(obj.total);
                if(total > 0 ){
                    $('#cant-alertas-general').text(total);
                    if(comunicacion > 0){
                        $('#cant-alertas-comunicacion').text(comunicacion);
                    }
                    if(continuidad > 0){
                        $('#cant-alertas-continuidad').text(continuidad);
                    }
                }
            }
        },
        error: function (a, b, c) {
            console.log(a);
            console.log(b);
            console.log(c);
        }
    });
}