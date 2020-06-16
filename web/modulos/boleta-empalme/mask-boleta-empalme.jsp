<script src="modulos/boleta-empalme/mask-boleta-empalme.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>

<script type="text/javascript">
    var idremarcador = <%out.print(request.getParameter("idremarcador"));%>;
    var numremarcador = <%out.print(request.getParameter("numremarcador"));%>;
    var NUMREMARCADOR = <%out.print(request.getParameter("numremarcador"));%>;
    var CONSUMO = <%out.print(request.getParameter("consumo"));%>;
    var mes = <%out.print(request.getParameter("mes"));%>;
    var MES = <%out.print(request.getParameter("mes"));%>;
    var FECHA_LECTURA_INICIAL = <%out.print(request.getParameter("fechalecturaini"));%>;
    var FECHA_LECTURA_FINAL = <%out.print(request.getParameter("fechalecturafin"));%>;
    var NUMSERIE = <%out.print(request.getParameter("numserie"));%>;
    var lecturaactual = <%out.print(request.getParameter("lecturaactual"));%>;
    var lecturaanterior = <%out.print(request.getParameter("lecturaanterior"));%>;
    $(document).ready(function () {
        getRemarcadorClienteIdRemarcador(idremarcador);
    });
</script>
<style>
    .modal-lg{
        max-width: 60%;
        max-height: 120%;
    }
    table.print-friendly tr td, table.print-friendly tr th {
        page-break-inside: avoid;
    }
</style>
<div class="container-fluid" id="cont-boleta" style="height: 100%;">
    <div class="row" style="height: 100%;">
        <div class="col-md-3">
            <div class="form-group" style="font-size: 11px;">
                <label data-html2canvas-ignore="true" for="select-tarifa">Tarifa: </label>
                <select data-html2canvas-ignore="true" onchange="armarDetalleTarifa();" class="form-control form-control-sm small" style="font-size: 11px;" id="select-tarifa"></select>
            </div>
        </div>
        <div class="col-md-4"></div>
        <div class="col-md-3 float-right" style="right: 0px;" >
            <div style="border: 3px solid #FD8104; width: 100%; text-align: center; font-weight: bold; font-size: 14px;">
                RUT: 99.593.200-8
                <br />
                COMPROBANTE DE COBRO
                <br />
                Nº 00000000
            </div>
            <br />
            <br />
            <table style="font-size: 10px;">
                <tr>
                    <td colspan="2">Nº Empalme</td>
                </tr>
                <tr>
                    <td colspan="2" id="num-empalme-boleta" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Nº Remarcador</td>
                </tr>
                <tr>
                    <td colspan="2" id="num-remarcador-boleta" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Nº Serie</td>
                </tr>
                <tr>
                    <td colspan="2" id="num-serie" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Número de cliente</td>
                </tr>
                <tr>
                    <td colspan="2" id="num-cliente" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Fecha Emisión</td>
                </tr>
                <tr>
                    <td colspan="2" id="fecha-emision" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Fecha Estimada Próxima Lectura</td>
                </tr>
                <tr>
                    <td colspan="2" id="fecha-prox-lectura" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Dirección Suministro</td>
                </tr>
                <tr>
                    <td colspan="2" id="direccion-suministro" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Tipo Red</td>
                </tr>
                <tr>
                    <td colspan="2" id="nomred" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Tarifa</td>
                </tr>
                <tr>
                    <td colspan="2" id="tarifa" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Período de Lectura</td>
                </tr>
                <tr>
                    <td>Desde</td>
                    <td>Hasta</td>
                </tr>
                <tr>
                    <td id="desde" style="font-weight: bold;"></td>
                    <td id="hasta" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Demandas Máxmias Leídas kW</td>
                </tr>
                <tr>
                    <td>Suministradas</td>
                    <td>Horas de Punta</td>
                </tr>
                <tr>
                    <td id="suministradas" style="font-weight: bold;"></td>
                    <td id="horas-punta" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Consumo Total kWh</td>
                </tr>
                <tr>
                    <td colspan="2" id="consumo-total-kwh" style="font-weight: bold;"></td>
                </tr>
            </table>
        </div>    
    </div>
    <div class="row">
        <div class="col-md-9 float-left" style="position: absolute; top: 5em; left: 0px;">
            <table style="border-collapse: collapse; font-size: 12px; width: 80%;" id="cabecera">
                <tbody>
                    <tr style="vertical-align: top;">
                        <td rowspan="3">
                            <img width="200" height="100" src="img/logo.jpg" alt="Bodenor"/>
                        </td>
                        <td style="font-weight: bold;">
                            <ul>
                                <li>Rut: </li>
                            </ul>
                        </td>
                        <td style="vertical-align: top;">
                            <span id="rut-cliente"></span>
                        </td>
                    </tr>
                    <tr style="vertical-align: top;">
                        <td style="font-weight: bold;">
                            <ul>
                                <li>Señor(es): </li>
                            </ul>
                        </td>
                        <td style="vertical-align: top;">
                            <span id="nom-cliente"></span>
                        </td>
                    </tr>
                    <tr style="vertical-align: top;">
                        <td style="font-weight: bold;">
                            <ul>
                                <li>Dirección: </li>
                            </ul>
                        </td>
                        <td style="vertical-align: top;">
                            <span id="direccion"></span>
                        </td>
                    </tr>
                    <tr style="vertical-align: top;">
                        <td rowspan="3">
                            Bodenor FlexCenter S.A.
                            <br />
                            Almacenamiento y Logística.
                        </td>
                        <td style="font-weight: bold;">
                            <ul>
                                <li>Don(ña): </li>
                            </ul>
                        </td>
                        <td style="vertical-align: top;">
                            <span id="persona"></span>
                        </td>
                    </tr>
                    <tr style="vertical-align: top;">
                        <td style="font-weight: bold;">
                            <ul>
                                <li>Fono: </li>
                            </ul>
                        </td>
                        <td style="vertical-align: top;">
                            <span id="fono"></span>
                        </td>
                    </tr>
                    <tr style="vertical-align: top;">
                        <td style="font-weight: bold; padding-top: 1px; padding-bottom: 1px;">
                            <ul style="padding-top: 1px; padding-bottom: 1px;" >
                                <li style="padding-top: 1px; padding-bottom: 1px;" >Email: </li>
                            </ul>
                        </td>
                        <td style="vertical-align: top;">
                            <span id="email"></span>
                        </td>
                    </tr>
                </tbody>
            </table>
            <br />
            <h5><strong>Detalle de consumo del remarcador</strong></h5>
            <table style="border-collapse: collapse; border: 3px solid white; font-size: 11px; background-color: #E9EFF8; width: 80%;" id="detalle-remarcador">
                <thead>
                    <tr>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; ">
                            Nº Medidor
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;">
                            Propiedad
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;">
                            Lectura Anterior
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white;">
                            Lectura Actual
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                            Consumo (kWh)
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="padding: 0px 20px 0px 10px;">
                            <%out.print(request.getParameter("numremarcador"));%>
                        </td>
                        <td style="padding: 0px 20px 0px 10px;">Bodenor Flex Center</td>
                        <td>
                            <%out.print(modelo.Util.formatMiles(request.getParameter("lecturaanterior")));%>
                        </td>
                        <td style="padding: 0px 20px 0px 10px;">
                            <%out.print(modelo.Util.formatMiles(request.getParameter("lecturaactual")));%>
                        </td>
                        <td style="padding: 0px 20px 0px 10px;">
                            <%out.print(modelo.Util.formatMiles(request.getParameter("consumo")));%>
                        </td>
                    </tr>
                </tbody>
            </table>
            <br />
            <table style='border-collapse: collapse; border: 3px solid white; font-size: 11px; background-color: #E9EFF8; width: 75%; height:100%;' class="print-friendly" id='detalle-tarifa-remarcador'>
            </table>
        </div>

    </div>
</div>