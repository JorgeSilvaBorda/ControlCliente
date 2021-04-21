function Empalme(idempalme = undefined, numempalme = undefined, instalacion = new Instalacion(), bodega = new Bodega(), red = new Red(), remarcadores = []){
    this.idempalme = idempalme;
    this.numempalme = numempalme;
    this.instalacion = instalacion;
    this.bodega = bodega;
    this.red = red;
    this.remarcadores = remarcadores;
}

function Instalacion(idinstalacion = undefined, nominstalacion = undefined, direccion = undefined, comuna = new Comuna()){
    this.idinstalacion = idinstalacion;
    this.nominstalacion = nominstalacion;
    this.direccion = direccion;
    this.comuna = comuna;
}

function Bodega(idbodega = undefined, nombodega = undefined, instalacion = new Instalacion()){
    this.idbodega = idbodega;
    this.nombodega = nombodega;
    this.instalacion = instalacion;
}

function Remarcador(idremarcador = undefined, numremarcador = undefined, marca = undefined, modelo = undefined, numserie = undefined, modulos = undefined, bodega = new Bodega(), empalme = new Empalme(), lastasignacion = undefined){
    this.idremarcador = idremarcador;
    this.numremarcador = numremarcador;
    this.marca = marca;
    this.modelo = modelo;
    this.numserie = numserie;
    this.modulos = modulos;
    this.bodega = bodega;
    this.empalme = empalme;
    this.lastasignacion = lastasignacion;
}

function Comuna(idcomuna = undefined, nomcomuna = undefined, provincia = new Provincia()){
    this.idcomuna = idcomuna;
    this.nomcomuna = nomcomuna;
    this.provincia = provincia;
}

function Provincia(idprovincia = undefined, nomprovincia = undefined, region = new Region()){
    this.idprovincia = idprovincia;
    this.nomprovincia = nomprovincia;
    this.region = region;
}

function Region(idregion = undefined, nomregion = undefined, abreviatura = undefined, capital = undefined){
    this.idregion = idregion;
    this.nomregion = nomregion;
    this.abreviatura = abreviatura;
    this.capital = capital;
}

function Red(idred = undefined, nomred = undefined){
    this.idread = idred;
    this.nomread = nomred;
}