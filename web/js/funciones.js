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