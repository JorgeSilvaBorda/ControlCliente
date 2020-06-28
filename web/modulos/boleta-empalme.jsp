<script src="modulos/boleta-empalme.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectInstalacion();
        //getSelectTarifasIdComuna();
    });

</script>
<div class="container-fluid">
    <!-- Modal -->
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
                    <button style="position: absolute; left: 4px;" type="button" class="float-right btn btn-danger" data-dismiss="modal">Cerrar</button>
                    <button style="display:none;" id="btn-generar" onclick="generar();" type="button" class="float-left btn btn-outline-primary">Generar</button>
                    <button style="display:none;" id="btn-imprimir" onclick="imprimir();" type="button" class="float-left btn btn-outline-primary">Imprimir</button>
                </div>

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Emitir Detalle <small>Complemento Servicios de Administración</small></h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-2">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-instalacion" >Instalación</label>
                    <select onchange="getSelectEmpalmesNumEmpalmesInstalacion()" class="form-control form-control-sm small" id="select-instalacion">
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-empalme" >Empalme</label>
                    <select class="form-control form-control-sm small" id="select-empalme" >
                    </select>
                </div>

                <div class="form-group" style="">
                    <label for="mes">Mes</label>
                    <input type="month" id="mes" class="form-control form-control-sm"/>
                </div>
                <div class="form-group">
                    <label data-html2canvas-ignore="true" for="select-tarifa">Tarifa: </label>
                    <select data-html2canvas-ignore="true" class="form-control form-control-sm small" id="select-tarifa"></select>
                </div>
                <div class="form-group" style="">
                    <div class="row">
                        <div class="col-sm-6">
                            <button type="button" class="btn btn-primary btn-sm" onclick="buscar();" >Buscar</button>
                        </div>
                        <div class="col-sm-6">
                            <div class="loader" style="display: none; top: -1.5em; position: absolute;"><!-- Contenedor del Spinner -->
                                <br />
                                <div class="ldio-sa9px9nknjc"> <!-- El Spinner -->
                                    <div>
                                    </div>
                                    <div>
                                        <div></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- button type="button" class="btn btn-outline-primary btn-sm float-right" onclick="limpiar()">Limpiar</button-->

                </div>
            </form>
        </div>
        <div class="col-sm-8 float-sm-right" id="detalle-remarcadores">
        </div>

    </div>
</div>