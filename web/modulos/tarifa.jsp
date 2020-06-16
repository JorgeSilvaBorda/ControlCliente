<script src="modulos/tarifa.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectTarifas();
        getSelectComunas();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Tarifas</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    Buscar
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-2">
                            <select id="select-tarifa" class="form-control form-control-sm small">

                            </select>
                        </div>
                        <div class="col-md-1">
                            <button onclick="buscar();" id="btn-buscar" type="button" class="btn btn-info btn-sm">Buscar</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br />
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    Insertar - Editar Tarifas
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <table style="font-size: 12px;">
                                <tr>
                                    <td>
                                        Nombre
                                    </td>
                                    <td>
                                        <input style="font-size: 12px;" type="text" id="nom-tarifa" class="form-control form-control-sm small texto-valido" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Comuna
                                    </td>
                                    <td>
                                        <select style="font-size: 12px;" id="select-comuna" class="form-control form-control-sm small select-valido">

                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br />
    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    Cargo Decreto Tarifario 11T 2016
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table style="font-size: 12px;">
                                <thead>
                                    <tr>
                                        <th style="width:35em;"><h5><strong>Concepto</strong></h5></th>
                                        <th style="width:10em;"><h5><strong>Unidad</strong></h5></th>
                                        <th style="width:10em;"><h5><strong>Red</strong></h5></th>
                                        <th><h5><strong>Valor Neto $</strong></h5></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><strong>Cargo Fijo</strong></td>
                                        <td>$/Mes</td>
                                        <td></td>
                                        <td>
                                            <input id="cargo-fijo" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Cargo por Servicio P�blico</strong></td>
                                        <td>$/kWh</td>
                                        <td></td>
                                        <td>
                                            <input id="cargo-servicio-publico" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Transporte de Electricidad</strong></td>
                                        <td>$/kWh</td>
                                        <td></td>
                                        <td>
                                            <input id="transporte-electricidad" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Cargo por Energ�a</strong></td>
                                        <td>$/kWh</td>
                                        <td></td>
                                        <td>
                                            <input id="cargo-energia" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Cargo por Demanda M�xima de Potencia Le�da en Horas de Punta</strong></td>
                                        <td>$/kW/Mes</td>
                                        <td>BT_AA</td>
                                        <td>
                                            <input id="cdmplhp-btaa" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>BT_SA</td>
                                        <td>
                                            <input id="cdmplhp-btsa" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>BT_AS</td>
                                        <td>
                                            <input id="cdmplhp-btas" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>BT_SS</td>
                                        <td>
                                            <input id="cdmplhp-btss" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><strong>Cargo por Demanda M�xima de Potencia Suministrada</strong></td>
                                        <td>$/kW/Mes</td>
                                        <td>BT_AA</td>
                                        <td>
                                            <input id="cdmps-btaa" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>BT_SA</td>
                                        <td>
                                            <input id="cdmps-btsa" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>BT_AS</td>
                                        <td>
                                            <input id="cdmps-btas" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td>BT_SS</td>
                                        <td>
                                            <input id="cdmps-btss" type="number" class="form-control form-control-sm small numero-valido" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <button type="button" id="btn-insert" onclick="insTarifa()" class="btn btn-primary btn-sm">Insertar</button>
                                            <button type="button" id="btn-save" onclick="saveTarifa()" class="btn btn-success btn-sm" style="display:none;">Guardar</button>
                                        </td>
                                        <td>
                                            <button type="button" id="btn-limpiar" onclick="limpiar()" class="btn btn-outline-primary btn-sm">Limpiar</button>
                                        </td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>