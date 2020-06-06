<script src="modulos/boleta-empalme/mask-boleta-empalme.js?=<% out.print(modelo.Util.generaRandom(10000, 99999));%>" type="text/javascript"></script>
<script type="text/javascript">
    var idremarcador = <%out.print(request.getParameter("idremarcador"));%>;
    var consumo = <%out.print(request.getParameter("consumo"));%>;
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
    });
</script>
<div id="contenido-boleta-empalme">
    <table style="border-collapse: collapse;; font-size: 12px;" id="cabecera">
        <tbody>
            <tr style="vertical-align: top;">
                <td rowspan="3">
                    <img width="200" height="100" src="img/logo.jpg" alt="Bodenor"/>
                </td>
                <td style="font-weight: bold;">
                    <ul>
                        <li>Rut:</li>
                    </ul>
                </td>
                <td style="vertical-align: top;">
                    99.999.999-9
                </td>
            </tr>
            <tr style="vertical-align: top;">
                <td style="font-weight: bold;">
                    <ul>
                        <li>Señor(es):</li>
                    </ul>
                </td>
                <td style="vertical-align: top;">
                    Nombre CLiente S.A.
                </td>
            </tr>
            <tr style="vertical-align: top;">
                <td style="font-weight: bold;">
                    <ul>
                        <li>Dirección</li>
                    </ul>
                </td>
                <td style="vertical-align: top;">
                    Dirección del remarcador # 123
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
                        <li>Don(ña):</li>
                    </ul>
                </td>
                <td style="vertical-align: mtopiddle;">
                    Nombre de persona
                </td>
            </tr>
            <tr style="vertical-align: middltope;">
                <td style="font-weight: bold;">
                    <ul>
                        <li>Campo 2:</li>
                    </ul>
                </td>
                <td style="vertical-align: top;">
                    Valor Campo 2
                </td>
            </tr>
            <tr style="vertical-align: top;">
                <td style="font-weight: bold;">
                    <ul>
                        <li>Campo 3:</li>
                    </ul>
                </td>
                <td style="vertical-align: top;">
                    Valor Campo 3
                </td>
            </tr>
        </tbody>
    </table>
    <br />
    <h5><strong>Detalle de consumo del remarcador</strong></h5>
    <table style="border-collapse: collapse; border: 3px solid white; font-size: 12px; background-color: #E9EFF8;" id="detalle-remarcador">
        <thead>
            <tr>
                <th style="background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                    Nº Medidor
                </th>
                <th style="background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                    Propiedad
                </th>
                <th style="background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                    Lectura Anterior
                </th>
                <th style="background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                    Lectura Actual
                </th>
                <th style="background-color: #FD8104; color: #525659; font-weight:bold; text-align: center; border-left: 2px solid white; border-right: 2px solid white;">
                    Consumo (kWh)
                </th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>123</td>
                <td>Bodenor Flex Center</td>
                <td>34234</td>
                <td>15234</td>
                <td>4000</td>
            </tr>
        </tbody>
    </table>
</div>