String.prototype.replaceAll = function (search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

function colorDinamico() {
    var r = Math.floor(Math.random() * 255);
    var g = Math.floor(Math.random() * 255);
    var b = Math.floor(Math.random() * 255);
    return "rgb(" + r + "," + g + "," + b + ")";
}


function colorDinamicoArr() {
    var r = Math.floor(Math.random() * 255);
    var g = Math.floor(Math.random() * 255);
    var b = Math.floor(Math.random() * 255);
    return [r, g, b];
}

function formatFechaYYYYMMDD(fecha){
    var dt = new Date(fecha);
    var dia = dt.getDate();
    var diaString = dia.toString();
    var mes = dt.getMonth() + 1;
    var mesString = mes.toString();
    var anio = dt.getFullYear();
    if(dia < 10){
        diaString = '0' + dia.toString();
    }
    if(mes < 10){
        mesString = '0' + mes.toString();
    }
    return anio.toString() + '-' + mesString + '-' + diaString;
}

function formatFechaDDMMYYYY(fecha){
    var dt = new Date(fecha);
    var dia = dt.getDate();
    var diaString = dia.toString();
    var mes = dt.getMonth() + 1;
    var mesString = mes.toString();
    var anio = dt.getFullYear();
    if(dia < 10){
        diaString = '0' + dia.toString();
    }
    if(mes < 10){
        mesString = '0' + mes.toString();
    }
    return  diaString + '-' + mesString + '-' + anio.toString();
}

function formatMiles(valor) {
    valor = valor.toString();
    var num = valor.replace(/\./g, "");
    if (!isNaN(num)) {
        num = num.toString().split("").reverse().join("").replace(/(?=\d*\.?)(\d{3})/g, '$1.');
        num = num.split("").reverse().join("").replace(/^[\.]/, "");
        return num;
    } else {
        console.log("No se puede formatear.");
        valor = valor.replace(/[^\d\.]*/g, "");
        return valor;
    }
}