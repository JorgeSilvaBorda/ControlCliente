<script src="modulos/cliente-remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        //getClienteRemarcadores();
        getRemarcadoresLibres();
        getSelectClientes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Asignar Remarcador a Cliente</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-cliente" >Cliente</label>
                    <select onchange="getRemarcadoresAsignadosIdCliente($(this).val())" class="form-control form-control-sm small" id="select-cliente" >
                    </select>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <h4>Remarcadores sin asignar</h4>
            <table id="tabla-remarcadores-libres" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>ID Remarcador</th>
                        <th># Empalme</th>
                        <th>Bodega</th>
                        <th>Módulos</th>
                        <th>Instalacion</th>
                        <th>Asignar</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
    <div class='row'>
        <div class='col-sm-12'>
            <table id='tabla-cliente-remarcador' class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>ID Remarcador</th>
                        <th>Empalme</th>
                        <th>Bodega</th>
                        <th>Instalación</th>
                        <th>Fecha Asignación</th>
                        <th>Quitar</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>