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

function formatFechaYYYYMMDD(fecha) {
    const date = new Date(fecha);
    const dateTimeFormat = new Intl.DateTimeFormat('es', {year: 'numeric', month: '2-digit', day: '2-digit'});
    const [{value: month}, , {value: day}, , {value: year}] = dateTimeFormat.formatToParts(date);

    return (`${year}-${day}-${month}`);
}

function formatFechaDDMMYYYY(fecha) {
    var fec = fecha.toString().split("-");
    return fec[2] + "-" + fec[1] + "-" + fec[0];
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

function fechaAMesPalabraCorto(fecha) {
    //return fecha;
    var mes = parseInt(fecha.split("-")[1]);
    switch (mes) {
        case 1:
            return "Ene";
            break;
        case 2:
            return "Feb";
            break;
        case 3:
            return "Mar";
            break;
        case 4:
            return "Abr";
            break;
        case 5:
            return "May";
            break;
        case 6:
            return "Jun";
            break;
        case 7:
            return "Jul";
            break;
        case 8:
            return "Ago";
            break;
        case 9:
            return "Sep";
            break;
        case 10:
            return "Oct";
            break;
        case 11:
            return "Nov";
            break;
        case 12:
            return "Dic";
            break;
        default:
            return "";
    }
}