<script src="modulos/boleta-empalme/mask-boleta-empalme.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    var idremarcador = <%out.print(request.getParameter("idremarcador"));%>;
    var numremarcador = <%out.print(request.getParameter("numremarcador"));%>;
    var CONSUMO = <%out.print(request.getParameter("consumo"));%>;
    var fechaini = <%out.print(request.getParameter("fechaini"));%>;
    var fechafin = <%out.print(request.getParameter("fechafin"));%>;
    var lecturaactual = <%out.print(request.getParameter("lecturaactual"));%>;
    var lecturaanterior = <%out.print(request.getParameter("lecturaanterior"));%>;
    $(document).ready(function () {
        console.log("idremarcador: " + idremarcador);
        console.log("consumo: " + consumo);
        console.log("fechaini: " + fechaini);
        console.log("fechafin: " + fechafin);
        console.log("lecturaanterior: " + lecturaanterior);
        console.log("lecturactual: " + lecturaactual);
        console.log("lecturactual: " + numremarcador);

        getRemarcadorClienteIdRemarcador(idremarcador);
    });
</script>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3">
            <div class="form-group" style="font-size: 11px;">
                <label for="select-tarifa">Tarifa: </label>
                <select onchange="armarDetalleTarifa();" class="form-control form-control-sm small" style="font-size: 11px;" id="select-tarifa"></select>
            </div>
        </div>
    </div>
</div>
<div id="contenido-boleta-empalme">
    <table style="border-collapse: collapse; font-size: 12px;" id="cabecera">
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
    <table style="border-collapse: collapse; border: 3px solid white; font-size: 12px; background-color: #E9EFF8;" id="detalle-remarcador">
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
                    <%out.print(request.getParameter("lecturaanterior"));%>
                </td>
                <td style="padding: 0px 20px 0px 10px;">
                    <%out.print(request.getParameter("lecturaactual"));%>
                </td>
                <td style="padding: 0px 20px 0px 10px;">
                    <%out.print(request.getParameter("consumo"));%>
                </td>
            </tr>
        </tbody>
    </table>
    <br />
    <table style='border-collapse: collapse; border: 3px solid white; font-size: 12px; background-color: #E9EFF8;' id='detalle-tarifa-remarcador'>
        <thead>
            <tr>
                <th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; '>
                    Cargos
                </th>
                <th style='padding: 0px 20px 0px 10px; background-color: #FD8104; color: #525659; font-weight:bold; text-align: left; border-left: 2px solid white; border-right: 2px solid white; '>
                    Valores
                </th>
            </tr>
        </thead>
    </table>

</div>