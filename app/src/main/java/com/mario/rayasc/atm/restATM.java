package com.mario.rayasc.atm;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class restATM {
    private final static String sUrlServer = "http://192.168.100.52:8080/";
    public static Model.cRespuesta getDenominations() {
        Model.cRespuesta oRespuesta = new Model.cRespuesta();
        try {
            //String sToken = "9fa02e5886afc01e332a290c3cd66a26c078fdd7d291d7f9dd56629580cc670ee197eb609198febc6ee3c620603e2e02eb887be528bae2ee274c63299a0be0616e539fe4ecd7e374f4af009a0b8fec2192e1a35d5a2330c3c1c3d18c6fa358d4";

            String urlAdress = sUrlServer+"denomination/getDenominations";



            URL url = new URL(urlAdress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //conn.setDoOutput(true);
            //conn.setDoInput(true);

            int responseCode = conn.getResponseCode();

            oRespuesta.sCodigoRespuestaRemota=responseCode+"";




            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                //JSONObject jsonRespuesta = new JSONObject(response.toString());
                oRespuesta.sJSON= response.toString();
                //String sCodigoResultado=jsonRespuesta.getString("codigoResultado");

                oRespuesta.oCodigoRespuesta.id_codigo_respuesta="OK";
                oRespuesta.oCodigoRespuesta.codigo_respuesta="Éxito";

            } else {

                String sEstatus = String.valueOf(conn.getResponseCode());
                oRespuesta.oCodigoRespuesta.codigo_respuesta="Error en rest";
                oRespuesta.sCodigoRespuestaRemota="["+sEstatus+"] "+conn.getResponseMessage()+" url:\n"+urlAdress;;

            }

            conn.disconnect();

        } catch (Exception e) {
            oRespuesta.oCodigoRespuesta.id_codigo_respuesta="EX";
            oRespuesta.oCodigoRespuesta.codigo_respuesta="Error en la función getDenominations";
            oRespuesta.oCodigoRespuesta.descripcion=e.toString();
        }

        return oRespuesta;
    }

    public static Model.cRespuesta getDenominationsByAmount(String amount) {
        Model.cRespuesta oRespuesta = new Model.cRespuesta();

        try {
            //String sToken = "9fa02e5886afc01e332a290c3cd66a26c078fdd7d291d7f9dd56629580cc670ee197eb609198febc6ee3c620603e2e02eb887be528bae2ee274c63299a0be0616e539fe4ecd7e374f4af009a0b8fec2192e1a35d5a2330c3c1c3d18c6fa358d4";

            String urlAdress = sUrlServer+"denomination/getDenominationsByAmount/"+amount;



            URL url = new URL(urlAdress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //conn.setDoOutput(true);
            //conn.setDoInput(true);

            int responseCode = conn.getResponseCode();

            oRespuesta.sCodigoRespuestaRemota=responseCode+"";




            if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                //JSONObject jsonRespuesta = new JSONObject(response.toString());
                oRespuesta.sJSON= response.toString();
                //String sCodigoResultado=jsonRespuesta.getString("codigoResultado");

                oRespuesta.oCodigoRespuesta.id_codigo_respuesta="OK";
                oRespuesta.oCodigoRespuesta.codigo_respuesta="Éxito";

            } else {

                String sEstatus = String.valueOf(conn.getResponseCode());
                oRespuesta.oCodigoRespuesta.codigo_respuesta="Error al conectarse a+\n"+urlAdress;
                oRespuesta.sCodigoRespuestaRemota="["+sEstatus+"] "+conn.getResponseMessage()+" url:\n"+urlAdress;;

            }

            conn.disconnect();

        } catch (Exception e) {

            oRespuesta.oCodigoRespuesta.id_codigo_respuesta="EX";
            oRespuesta.oCodigoRespuesta.codigo_respuesta="Error en en la funcion getDenominationsByAmount";
            oRespuesta.oCodigoRespuesta.descripcion=e.toString();
        }

        return oRespuesta;
    }
}
