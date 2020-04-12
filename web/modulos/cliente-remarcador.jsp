<script src="modulos/cliente-remarcador.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getClienteRemarcadores();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Clientes con Remarcadores</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="nom-instalacion" >Nombre Instalación</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nom-instalacion" />
                </div>
                <div class="form-group">
                    <button onclick="insInstalacion(getInstalaciones);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-instalaciones" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>