<script src="modulos/cliente-contacto.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getContactos();
        getSelectClientes();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Contactos<small> Cliente</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-cliente">Cliente</label>
                    <select class="form-control-sm form-control small" id="select-cliente">
                    </select>
                </div>
                <div class="form-group">
                    <label for="persona" >Nombre Contacto</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="persona" />
                </div>
                <div class="form-group">
                    <label for="cargo" >Cargo</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="cargo" />
                </div>
                <div class="form-group">
                    <label for="fono" >Fono</label>
                    <input type="number" class="form-control form-control-sm small" maxlength="9" id="fono" />
                </div>
                <div class="form-group">
                    <label for="email" >Email</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="400" id="email" />
                </div>
                <div class="form-group">
                    <button onclick="insContacto();" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                    <button onclick="saveContacto();" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-9">
            <table id="tabla-contactos" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Nombre Cliente</th>
                        <th>Nombre Contacto</th>
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