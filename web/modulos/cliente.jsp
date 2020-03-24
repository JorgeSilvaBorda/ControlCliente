<script src="modulos/cliente.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("input#rut-cliente").rut({
            formatOn: 'keyup',
            minimumLength: 8, // validar largo mínimo; default: 2
            validateOn: null // si no se quiere validar, pasar null
        });
        getClientes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Clientes <small>Mantenimiento y edición</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="rut-cliente" >Rut</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="12" id="rut-cliente" />
                </div>
                <div class="form-group">
                    <label for="nomcliente">Nombre o Razón Social</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="nomcliente" />
                </div>
                <div class="form-group">
                    <label for="direccion-1">Dirección 1</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="direccion-1" />
                </div>
                <div class="form-group">
                    <label for="direccion-2">Dirección 2</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="direccion-2" />
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-9">
            <table id="tabla-clientes" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Rut</th>
                        <th>Nombre</th>
                        <th>Dirección 1</th>
                        <th>Direección 2</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>