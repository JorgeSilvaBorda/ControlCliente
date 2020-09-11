<script src="mask-boleta-empalme-masivo.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>

<script type="text/javascript">

    var idremarcador = <%out.print(request.getParameter("idremarcador"));%>;
    var numremarcador = <%out.print(request.getParameter("numremarcador"));%>;
    var NUMREMARCADOR = <%out.print(request.getParameter("numremarcador"));%>;
    var IDTARIFA = <%out.print(request.getParameter("idtarifa"));%>;
    var CONSUMO = <%out.print(request.getParameter("consumo"));%>;
    var mes = <%out.print(request.getParameter("mes"));%>;
    var MES = <%out.print(request.getParameter("mes"));%>;
    var FECHA_LECTURA_INICIAL = <%out.print(request.getParameter("fechalecturaini"));%>;
    var FECHA_LECTURA_FINAL = <%out.print(request.getParameter("fechalecturafin"));%>;
    var MAX_DEMANDA_LEIDA = '<%out.print(request.getParameter("maxdemandaleida"));%>';
    var MAX_DEMANDA_HORA_PUNTA = '<%out.print(request.getParameter("maxdemandahorapunta"));%>';
    var NUMSERIE = <%out.print(request.getParameter("numserie"));%>;
    var lecturaactual = <%out.print(request.getParameter("lecturaactual"));%>;
    var lecturaanterior = <%out.print(request.getParameter("lecturaanterior"));%>;
    var LECTURA_ACTUAL = <%out.print(request.getParameter("lecturaactual"));%>;
    var LECTURA_ANTERIOR = <%out.print(request.getParameter("lecturaanterior"));%>;
    var MASA = <%out.print(request.getParameter("masivo"));%>;

    $(document).ready(function () {
        getRemarcadorClienteIdRemarcador(idremarcador);
    });
</script>
<style>
    .modal-lg{
        max-width: 100%;
        max-height: 120%;
    }
    table.print-friendly tr td, table.print-friendly tr th {
        page-break-inside: avoid;
    }
</style>
<div class="container-fluid" id="cont-boleta" style="height: 100%; width:50em;">
    <div class="row" style="height: 100%; width: 55em;">
        <div class="col-md-3">
        </div>
        <div class="col-md-4"></div>
        <div class="col-md-3 float-right" style="right: 0px; left: -1em;" >
            <div style="border: 3px solid #FD8104; width: 18em; text-align: center; font-weight: bold; font-size: 14px;">
                RUT: 99.593.200-8
                <br />
                DETALLE DE COMPLEMENTO<br /> SERVICIOS DE ADMINISTRACIÓN
                <br />
                Nº <span id="num-boleta"></span>
            </div>
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
                    <td colspan="2">Fecha Emisión</td>
                </tr>
                <tr>
                    <td colspan="2" id="fecha-emision" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Fecha Próxima Lectura</td>
                </tr>
                <tr>
                    <td colspan="2" id="fecha-prox-lectura" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Tarifa</td>
                </tr>
                <tr>
                    <td colspan="2" id="tarifa" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Tipo Red</td>
                </tr>
                <tr>
                    <td colspan="2" id="nomred" style="font-weight: bold;"></td>
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
                    <td colspan="2">Demandas Máximas Leídas kW</td>
                </tr>
                <tr>
                    <td>Suministradas</td>
                    <td>Horas de Punta</td>
                </tr>
                <tr>
                    <td id="leidas-suministradas" style="font-weight: bold;"></td>
                    <td id="leidas-horas-punta" style="font-weight: bold;"></td>
                </tr>
                <tr>
                    <td colspan="2">Demandas Máximas Facturadas kW</td>
                </tr>
                <tr>
                    <td>Suministradas</td>
                    <td>Horas de Punta</td>
                </tr>
                <tr>
                    <td id="suministradas" style="font-weight: bold;"></td>
                    <td id="horas-punta" style="font-weight: bold;"></td>
                </tr>
            </table>
        </div>    
    </div>
    <div class="row">
        <div class="col-md-9 float-left" style="position: absolute; top: 1em; left: 0px;">
            <table style="border-collapse: collapse; font-size: 12px; width: 100%;" id="cabecera">
                <tbody>
                    <tr style="vertical-align: top;">
                        <td rowspan="3">
                            <img width="150" height="75" src="../../img/logo.jpg" alt="Bodenor"/>
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
                            Bodenor Flexcenter S.A. Parques Logísticos 
                            <br />R.U.T.: 99.593.200-8
                            <br />Alonso de Córdova 2700 of. 41, Vitacura.
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
            <h5><strong>Detalle de consumo</strong></h5>
            <table style="border-collapse: collapse; border: 3px solid white; font-size: 11px; background-color: #E9EFF8; width: 100%;" id="detalle-remarcador">
                <thead>
                    <tr>
                        <th style="padding: 0px 0px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white; ">
                            Nº Medidor
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                            Propiedad
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                            Lectura Anterior
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                            Lectura Actual
                        </th>
                        <th style="padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                            Consumo (kWh)
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="padding: 0px 0px 0px 10px; text-align: left;">
                            <%out.print(request.getParameter("numremarcador"));%>
                        </td>
                        <td style="padding: 0px 0px 0px 10px;">Bodenor Flex Center</td>
                        <td style="text-align: right;">
                            <%out.print(modelo.Util.formatMiles(request.getParameter("lecturaanterior")));%>
                        </td>
                        <td style="padding: 0px 0px 0px 10px; text-align: right;">
                            <%out.print(modelo.Util.formatMiles(request.getParameter("lecturaactual")));%>
                        </td>
                        <td style="padding: 0px 0px 0px 10px; text-align: right;">
                            <%out.print(modelo.Util.formatMiles(request.getParameter("consumo")));%>
                        </td>
                    </tr>
                </tbody>
            </table>
            <br />
            <table style='border-collapse: collapse; border: 3px solid white; font-size: 11px; background-color: #E9EFF8; width: 100%; height:100%;' class="print-friendly" id='detalle-tarifa-remarcador'>
            </table>
            <div class="col-md-12">
                <canvas id="grafico" width="1000" height="420" ></canvas>
            </div>
        </div>

    </div>
    <div class="row">

    </div>
</div>