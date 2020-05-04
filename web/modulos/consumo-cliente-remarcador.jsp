<script src="modulos/consumo-cliente-remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectClientes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Consumo de cliente</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-cliente" >Cliente</label>
                    <select onchange="getSelectRemarcadoresCliente($(this).val())" class="form-control form-control-sm small" id="select-cliente" >
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-remarcador" >Remarcador</label>
                    <select onchange="graficar($(this).val());" style="width: 25%;" class="form-control form-control-sm small" id="select-remarcador" >
                    </select>
                </div>
            </form>
            <div class='col-md-12 float-sm-left small' id="detalle-remarcador">

            </div>
        </div>
        <div class="col-md-8">
            <canvas id="grafico" width="600" height="300"></canvas>
        </div>
    </div>
    <div class='row'>

    </div>
</div>