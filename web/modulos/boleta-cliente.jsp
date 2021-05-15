<script src="modulos/boleta-cliente.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectClientes();
    });

</script>
<div class="container-fluid">
    <div class="modal fade" id="modal">
        <div class="modal-dialog modal-lg" style="width: 55em;">
            <div class="modal-content" style="width: 55em;">

                <!-- Header -->
                <div class="modal-header">
                    <div class="col-md-3">

                    </div>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>

                <!-- Body -->
                <div id="modal-body" class="modal-body" style="height: 57em; width: 45em;">

                </div>

                <!-- Modal footer -->
                <div id="modal-footer" class="modal-footer">
                    <button style="position: absolute; left: 4px;" type="button" class="float-right btn btn-danger" data-dismiss="modal" id="btn-cerrar-modal">Cerrar</button>
                    <button style="display:none;" id="btn-generar" onclick="generar();" type="button" class="float-left btn btn-outline-primary">Generar</button>
                    <button style="display:none;" id="btn-imprimir" onclick="imprimir();" type="button" class="float-left btn btn-outline-primary">Imprimir</button>
                </div>

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Emitir Detalle de Complemento de Servicios de Administración <small>por Cliente</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-2">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-cliente" >Cliente</label>
                    <select onchange="getSelectInstalacionesCliente($(this).val())" class="form-control form-control-sm small" id="select-cliente" >
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-instalacion" >Instalación</label>
                    <select onchange="getSelectRemarcadoresIdInstalacion($(this).val())"class="form-control form-control-sm small" id="select-instalacion">
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-remarcador" >Remarcador</label>
                    <select class="form-control form-control-sm small" id="select-remarcador" >
                    </select>
                </div>
                <div class="form-group">
                    <label or="select-tarifa">Tarifa </label>
                    <select class="form-control form-control-sm small" id="select-tarifa"></select>
                </div>
                <div class="form-group">
                    <label for="fecha-desde" >Desde</label>
                    <input type="date" class="form-control form-control-sm small" id="fecha-desde" />
                </div>
                <div class="form-group">
                    <label for="fecha-hasta" >Hasta</label>
                    <input type="date" class="form-control form-control-sm small" id="fecha-hasta" />
                </div>
                <div class="form-group">
                    <div class="form-group">
                        <table style="border: none; border-collapse: collapse">
                            <tr>
                                <td><button id="btn-buscar" onclick="buscar();" type="button" class="btn btn-primary btn-sm">Buscar</button></td>
                                <td style="padding-left: 2em;">
                                    <div class="loader" style="display: none; margin-top: -1.2em;"><!-- Contenedor del Spinner -->
                                        <div class="ldio-sa9px9nknjc"> <!-- El Spinner -->
                                            <div>
                                            </div>
                                            <div>
                                                <div></div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td><button id="btn-limpiar" onclick="limpiar();" type="button" class="btn btn-default btn-sm">Limpiar</button></td>
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-10 float-sm-right" id="detalle-remarcadores">
        </div>
    </div>
    <div class='row'>

    </div>
</div>