<script src="modulos/resumen-mes-cliente.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        getSelectClientes();
        var fec = new Date();
        var m = '';
        fec.setDate(1);
        //fec.setDate(fec.getDate() - 1);
        if (fec.getMonth() + 1 < 10) {
            m = '0' + (fec.getMonth() + 1).toString();
        } else {
            m = (fec.getMonth() + 1).toString();
        }
        var aniomes = fec.getFullYear().toString() + "-" + m;
        $('#mes').val(aniomes);
    });

</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div class="page-header">
                <h1>Resumen Mes Cliente</h1>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3">
            <form class="form small"  role="form">
                <div class="form-group">
                    <label for="select-cliente" >Cliente</label>
                    <!--select onchange="buscar($(this).val());" class="form-control form-control-sm small" id="select-cliente" -->
                    <select onchange="getSelectInstalacionesCliente($(this).val());" class="form-control form-control-sm small" id="select-cliente" >
                    </select>
                </div>
                <div class="form-group">
                    <label for="select-instalacion" >Instalación</label>
                    <select class="form-control form-control-sm small" id="select-instalacion">
                    </select>
                </div>
                <div class="form-group" style="">
                    <label for="mes">Mes</label>
                    <input type="month" id="mes" class="form-control form-control-sm"/>
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
                            </tr>
                        </table>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-sm-1">

        </div>
        <div class="col-sm-3"></div>
        <div class="col-sm-5 float-sm-right" id="div-tabla-resumen">

        </div>
    </div>
    <div class='row'>
        <div class='col-sm-12'>
            <canvas id="line-chart" width="600" height="200"></canvas>
        </div>
    </div>
    <br />
    <div class='row'>
        <div class='col-sm-12'>
            <canvas id="line-chart-demandas" width="600" height="200"></canvas>
        </div>
    </div>
</div>