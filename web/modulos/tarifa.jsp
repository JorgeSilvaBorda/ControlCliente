<script src="modulos/tarifa.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getTarifas();
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
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="nom-tarifa" >Nombre Tarifa</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nom-tarifa" />
                </div>
                <div class="form-group">
                    <label for="valor-tarifa" >Valor Tarifa</label>
                    <input type="number" class="form-control form-control-sm small" maxlength="200" id="valor-tarifa" />
                </div>
                <div class="form-group">
                    <button onclick="insTarifa(getTarifas);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-4">
            <table id="tabla-tarifas" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Tarifa</th>
                        <th>Valor</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>