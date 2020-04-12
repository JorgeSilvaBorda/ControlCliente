<script src="modulos/cliente.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
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
    <form class="form small" role="form">
        <div class="row">
            <div class="col-sm-2">
                <div class="form-group">
                    <label for="rut-cliente" >Rut</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="12" id="rut-cliente" />
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="nomcliente">Nombre</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="nomcliente" />
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="razoncliente">R. Social</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="razoncliente" />
                </div>
            </div>
            <div class="col-sm-4">
                <div class="form-group">
                    <label for="direccion">Dirección</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="50" id="direccion" />
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-2">
                <div class="form-group">
                    <label for="modulos" >Módulos</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="modulos" />
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-2">
                <div class="form-group">
                    <label for="persona">Contacto</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="100" id="persona" />
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="cargo">Cargo</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="100" id="cargo" />
                </div>
            </div>
            <div class="col-sm-2">
                <div class="form-group">
                    <label for="fono">Fono</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="9" id="fono" />
                </div>
            </div>
            <div class="col-sm-3">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="email" />
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-2">
                <div class="form-group">
                    <label for="btn-insert">&nbsp;</label>
                    <button onclick="insCliente(getClientes);" type="button" class="btn btn-primary btn-sm" id="btn-insert" >Insertar</button>
                    <button onclick="saveCliente(getClientes);" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="col-sm-12">
            <table id="tabla-clientes" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Rut</th>
                        <th>Nombre</th>
                        <th>R. Social</th>
                        <th>Dirección</th>
                        <th>Módulos</th>
                        <th>Contacto</th>
                        <th>Cargo</th>
                        <th>Fono</th>
                        <th>Email</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>