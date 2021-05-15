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
        <div class="col-md-3">
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
                    <select style="width: 25%;" class="form-control form-control-sm small" id="select-remarcador" >
                    </select>
                </div>
                <div class="form-group">
                    <div class="form-group">
                        <table style="border: none; border-collapse: collapse">
                            <tr>
                                <td><button id="btn-buscar" onclick="graficar();" type="button" class="btn btn-primary btn-sm">Buscar</button></td>
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
                            </tr>
                        </table>
                    </div>
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