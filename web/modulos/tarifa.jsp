<script src="modulos/tarifa.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getTarifas();
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
            <div id="contenedor-tarifas" class="small float-left">
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
            <form class="form small"  role="form">
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
                                    <label for="concepto">Concepto</label>
                                    <input id="concepto" type="text" class="form-control form-control-sm" placeholder="Ej.: Cargo Fijo Mensual" aria-label="Concepto">
                                </div>
                            </td>
                            <td>
                                <div class="form-group">
                                    <label for="select-comuna">Comuna</label>
                                    <select id="select-comuna" class="form-control form-control-sm"></select>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="row">
        
    </div>
</div>