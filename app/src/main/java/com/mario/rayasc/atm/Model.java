package com.mario.rayasc.atm;

import java.io.Serializable;
import com.google.gson.Gson;

import java.util.ArrayList;
public class Model {
    public static class Denomination implements Serializable {
        private double idDenomination;
        private String type;
        private Integer quantity;

        public double getIdDenomination() {
            return idDenomination;
        }

        public void setIdDenomination(double idDenomination) {
            this.idDenomination = idDenomination;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public static String fsJSON(Denomination oObject){
            String sJSON="";
            try {
                Gson gJSON = new Gson();
                sJSON = gJSON.toJson(oObject);
            } catch (Exception ex) {
                sJSON=null;
            }
            return sJSON;
        }

        public static String fsJSON(Denomination[] aoObject){
            String sJSON="";
            try {
                Gson gJSON = new Gson();
                ArrayList<Denomination> laRespuestaWS = new ArrayList();
                for (int i = 0; i < aoObject.length; i++) {
                    laRespuestaWS.add(aoObject[i]);
                }
                sJSON = gJSON.toJson(laRespuestaWS);
            } catch (Exception ex) {
                sJSON=null;
            }
            return sJSON;
        }

        public static Denomination foObjet(String sJSON){
            Denomination oObject=null;
            try {
                Gson gJSON = new Gson();
                oObject = gJSON.fromJson(sJSON, Denomination.class);
            } catch (Exception ex) {
                oObject=null;
            }
            return oObject;
        }
        public static Denomination[] faoObjet(String sJSON){
            Denomination[] aoObject=null;
            try {
                Gson gJSON = new Gson();
                aoObject = gJSON.fromJson(sJSON, Denomination[].class);
            } catch (Exception ex) {
                aoObject=null;
            }
            return aoObject;
        }
    }

    public static class cCodigoRespuesta {
        public String id_codigo_respuesta = "";
        public String codigo_respuesta = "";
        public String descripcion = "";
    }
    public static class cRespuesta {
        public cCodigoRespuesta oCodigoRespuesta = new cCodigoRespuesta();
        public String sCodigoRespuestaRemota = "";
        public String sRespuestaRemota = "";
        public String sXML = "";
        public String sHTML = "";
        public String sJSON = "";
        public String sExepcion = "";
        public String[] asExtra = null;
    }
}
