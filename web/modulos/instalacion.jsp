<script src="modulos/instalacion.js?=<% out.print(modelo.Util.generaRandom(10000, 99999)); %>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getInstalaciones();
        getSelectComunas();
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Instalaciones</h1>
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
                    <label for="direccion" >Dirección</label>
                    <input type="text" class="form-control form-control-sm small" maxlength="200" id="direccion" />
                </div>
                <div class="form-group">
                    <label for="comuna" >Comuna</label>
                    <select id="select-comuna" class="form-control form-control-sm smal">
                        
                    </select>
                </div>
                <div class="form-group">
                    <button onclick="existeInstalacion(validarCampos);" type="button" class="btn btn-primary btn-sm" id="btn-insert">Insertar</button>
                    <button onclick="saveInstalacion(getInstalaciones);" hidden="hidden" type="button" class="btn btn-secondary btn-sm" id="btn-guardar">Guardar</button>
                    <button onclick="limpiar();" type="button" class="btn btn-default btn-sm" id="btn-limpiar">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="col-sm-7">
            <table id="tabla-instalaciones" class="table table-condensed table-borderless table-striped table-hover table-sm small">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Dirección</th>
                        <th>Comuna</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
        </div>
    </div>
</div>