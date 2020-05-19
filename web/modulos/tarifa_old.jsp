<script src="modulos/tarifa.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('.red-oculto').hide();
        getTarifas();
        getSelectComunas();
        getTarifasConceptos();
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
        <div class="col-sm-3">
            <form class="form small "  role="form">
                <div class="form-group">
                    <label for="nom-tarifa">Nombre Tarifa</label>
                    <div class="input-group mb-3">
                        <input id="nom-tarifa" type="text" class="form-control form-control-sm" placeholder="Ej.: TXA3.1" aria-label="Tarifa" aria-describedby="basic-addon2">
                        <div class="input-group-append">
                            <button onclick="insTarifa(getTarifas);" class="btn btn-outline-success btn-sm" type="button" id="btn-insert">Agregar</button>
                            <button onclick="saveTarifa(getTarifas);" hidden="hidden" type="button" class="btn btn-outline-info btn-sm" id="btn-guardar">Guardar</button>
                        </div>
                        <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                    </div>
                </div>
            </form>
            <div id="contenedor-tarifas" class="float-left">
                <table id="tabla-tarifas" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                    <thead>
                        <tr>
                            <th>Tarifa</th>
                            <th style="width: 25%;">Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-sm-9">
            <form class="form small" role="form">
                <table>
                    <tbody>
                        <tr>
                            <td>
                                <div class="form-group">
                                    <label for="select-tarifa">Tarifa</label>
                                    <select id="select-tarifa" class="form-control form-control-sm"></select>
                                </div>
                            </td>
                            <td>
                                <div class="form-group">
                                    <label for="nomconcepto">Concepto</label>
                                    <input id="nomconcepto" type="text" class="form-control form-control-sm" placeholder="Ej.: Cargo Fijo Mensual" aria-label="Concepto">
                                </div>
                            </td>
                            <td>
                                <div class="form-group">
                                    <label for="select-comuna">Comuna</label>
                                    <select id="select-comuna" class="form-control form-control-sm"></select>
                                </div>
                            </td>
                            <td>
                                <div class="form-group">
                                    <label for="umedida">U. Medida</label>
                                    <input id="umedida" type="text" class="form-control form-control-sm" placeholder="Ej.: kWh" aria-label="UnidadMedida">
                                </div>
                            </td>
                            <td>
                                <div class="form-group">
                                    <label for="valorneto">$ Neto</label>
                                    <div class="input-group mb-3">
                                        <input id="valorneto" type="number" step="0.001" min="0.000" lang="es-CL" class="form-control form-control-sm" placeholder="Ej.: 1,275.25" aria-label="ValorNeto">
                                        <div class="input-group-append">
                                            <button onclick="insTarifaConcepto(getTarifasConceptos);" type="button" class="btn btn-sm btn-primary form-control form-control-sm" id="btn-ins-tarifa-concepto">Insertar</button>
                                            <button onclick="saveConcepto(getTarifasConceptos);" hidden="hidden" type="button" class="btn btn-sm btn-success form-control form-control-sm" id="btn-save-tarifa-concepto">Guardar</button>
                                        </div>
                                        <button onclick="limpiar();" type="button" class="btn btn-default btn-sm">Limpiar</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <table >
                    <tbody>
                        <tr>
                            <td style="vertical-align: bottom; padding-right: 10px;" >
                                <div class="form-group">
                                    <label for="check-red">Dividir Costo / Red</label>
                                    <input onchange="toggleRed();" type="checkbox" id="check-red" class="checkbox" />
                                </div>
                            </td>
                            <td style="vertical-align: bottom;" class="red-oculto" >
                                <div class="form-group">
                                    <label for="btaa">BT_AA</label>
                                    <input style="font-size: 10px; width: 100px;" type="number" id="btaa" class="form-control form-control-sm" />
                                </div>
                            </td>
                            <td style="vertical-align: bottom;" class="red-oculto">
                                <div class="form-group">
                                    <label for="btas">BT_AS</label>
                                    <input style="font-size: 10px; width: 100px;" type="number" id="btas" class="form-control form-control-sm" />
                                </div>
                            </td>
                            <td style="vertical-align: bottom;" class="red-oculto">
                                <div class="form-group">
                                    <label for="btsa">BT_SA</label>
                                    <input style="font-size: 10px; width: 100px;" type="number" id="btsa" class="form-control form-control-sm" />
                                </div>
                            </td>
                            <td style="vertical-align: bottom;" class="red-oculto">
                                <div class="form-group">
                                    <label for="btss">BT_SS</label>
                                    <input style="font-size: 10px; width: 100px;" type="number" id="btss" class="form-control form-control-sm" />
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
            <div id="contenedor-tarifas-conceptos" class="small">
                <table id="tabla-tarifas-conceptos" class="table table-sm table-condensed table-striped table-hover small">
                    <thead>
                    <th>Concepto</th>
                    <th>Tarifa</th>
                    <th>Comuna</th>
                    <th>Red</th>
                    <th>U. Medida</th>
                    <th>$ Neto</th>
                    <th>Acción</th>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>