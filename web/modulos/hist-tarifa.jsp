<script src="modulos/hist-tarifa.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectAnioTarifa();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Histórico de Tarifa</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-2">
            <form class="form small"  role="form">
                <div class="form-group">
                    <div class="form-group" style="">
                        <select class="form-control form-control-sm" id="select-anio-tarifa"></select>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-2">
            <form class="form small"  role="form">
                <div class="form-group">
                    <div class="form-group" style="">
                        <button type="button" onclick="buscar();" id="btn-buscar" class="btn btn-primary btn-sm">Buscar</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12" id="hist-tarifa">
        </div>
    </div>
</div>