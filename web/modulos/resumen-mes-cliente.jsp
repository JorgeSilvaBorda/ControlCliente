<script src="modulos/resumen-mes-cliente.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectClientes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Resumen Mes Cliente</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-cliente" >Cliente</label>
                    <select onchange="buscar($(this).val());" class="form-control form-control-sm small" id="select-cliente" >
                    </select>
                </div>
            </form>
        </div>
        <div class="col-sm-9">
            
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-12'>
            <canvas id="line-chart" width="600" height="200"></canvas>
        </div>
    </div>
</div>