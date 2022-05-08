package com.mario.rayasc.atm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import static com.mario.rayasc.atm.restATM.getDenominations;
import static com.mario.rayasc.atm.restATM.getDenominationsByAmount;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog dialogoProcesando = null;
    Spinner spDenominaciones;
    com.getbase.floatingactionbutton.FloatingActionsMenu fabMenuATM;
    com.getbase.floatingactionbutton.FloatingActionButton faRetiro,faSync,faVer,faSalir;

    com.google.android.material.textfield.TextInputLayout tiAmount;
    com.google.android.material.textfield.TextInputEditText tieAmount;
    private String mMETHOD_REST_NAME="";
    Model.Denomination[] maoDenominacionesDisponibles=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spDenominaciones = findViewById(R.id.spDenominaciones);
        tiAmount = findViewById(R.id.tiAmount);
        tieAmount = findViewById(R.id.tieAmount);

        fabMenuATM = findViewById(R.id.fabMenuATM);
        fabMenuATM.expand();

        faSalir = findViewById(R.id.faSalir);
        faSalir.setVisibility(View.VISIBLE);
        faSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        faSync = findViewById(R.id.faSync);
        faSync.setVisibility(View.VISIBLE);
        faSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtieneDenominacionesDisponibles();
            }
        });

        faVer = findViewById(R.id.faVer);
        faVer.setVisibility(View.VISIBLE);
        faVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maoDenominacionesDisponibles!=null){

                    Intent intent = new Intent(MainActivity.this, ListInfoActivity.class);
                    intent.putExtra("sJSONDenominatios",Model.Denomination.fsJSON(maoDenominacionesDisponibles));

                    startActivity(intent);
                }else
                    Toast.makeText(MainActivity.this,
                            "Obtenga primero las denominaciones disponibles desde el servidor 'Sync'",
                            Toast.LENGTH_LONG).show();
            }
        });

        faRetiro = findViewById(R.id.faRetiro);
        faRetiro.setVisibility(View.VISIBLE);
        faRetiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAmount = tieAmount.getText().toString();
                if (!sAmount.equals("") && Double.parseDouble(sAmount) >= 0.5 && Double.parseDouble(sAmount) <= 12550 ){
                    dialogoProcesando = ProgressDialog.show(MainActivity.this, "Procesando...", "Espere unos segundos...", true, false);
                    mMETHOD_REST_NAME = "SolicitudRetiro";
                    new Tarea().execute(sAmount);
                }
                else
                    Toast.makeText(MainActivity.this,
                            "Capture el monto mayor o igual a 0.50 y menor o igual a 12,550.00",
                            Toast.LENGTH_LONG).show();
            }
        });


        obtieneDenominacionesDisponibles();

    }

    public void obtieneDenominacionesDisponibles(){
        dialogoProcesando = ProgressDialog.show(MainActivity.this, "Denominaciones...", "Espere unos segundos...", true, false);
        mMETHOD_REST_NAME = "DenominacionesDisponibles";
        new Tarea().execute("");
    }

    public void poblaDenominaciones (Model.Denomination[] aoObjeto){
        try{

            String[] aObjeto = new String[aoObjeto.length];
            //List<cUsoCFDI> lUsoCFDI=new ArrayList<cUsoCFDI>();
            int iSeleccion=0;
            for(int i=0;i<aoObjeto.length;i++){
                aObjeto[i]="$"+aoObjeto[i].getIdDenomination()+" ["+aoObjeto[i].getQuantity()+ "]";

            }

            ArrayAdapter<String> adObjeto=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,aObjeto);
            spDenominaciones.setAdapter(adObjeto);
            spDenominaciones.setSelection(iSeleccion);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this,
                    "En poblaDenominaciones expecion: "+e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    //---Tarea
    private class Tarea extends AsyncTask<String,Void,String> {

        Model.cRespuesta oRespuesta = new Model.cRespuesta();
        protected String doInBackground(String... params) {
            String amount = params[0];
            try {

                ConexionInternet conexionInternet = new ConexionInternet(MainActivity.this);
                if (!conexionInternet.bConectado()){

                    oRespuesta.oCodigoRespuesta.id_codigo_respuesta="ER";
                    oRespuesta.oCodigoRespuesta.codigo_respuesta="No se detectó conexión a INTERNET";
                    oRespuesta.oCodigoRespuesta.descripcion="";
                }else{
                    //oRespuestaWS= wsSmartLocation.wsSmartLocation(mMETHOD_NAME, mNombreParametro, mValorParametro);

                    if (mMETHOD_REST_NAME.equals("DenominacionesDisponibles"))
                        oRespuesta=getDenominations();
                    else if (mMETHOD_REST_NAME.equals("SolicitudRetiro"))
                        oRespuesta=getDenominationsByAmount(amount);


                }


            } catch (Exception e) {

                oRespuesta.oCodigoRespuesta.id_codigo_respuesta="EX";
                oRespuesta.oCodigoRespuesta.codigo_respuesta="Error en tarea";
                oRespuesta.oCodigoRespuesta.descripcion=e.toString();
            }
            return "";

        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);

        }

        protected void onPostExecute(String result) {
            try{
                dialogoProcesando.dismiss();

                if (oRespuesta.oCodigoRespuesta.id_codigo_respuesta.equals("OK")) {//Exitosa
                    if (mMETHOD_REST_NAME.equals("DenominacionesDisponibles")) {
                        //Mestra las denominaciones disponibles
                        maoDenominacionesDisponibles = Model.Denomination.faoObjet(oRespuesta.sJSON);
                        poblaDenominaciones(maoDenominacionesDisponibles);

                    } else if (mMETHOD_REST_NAME.equals("SolicitudRetiro")){
                        Model.Denomination[]  aoOblect = Model.Denomination.faoObjet(oRespuesta.sJSON);
                        Intent intent = new Intent(MainActivity.this, ListInfoActivity.class);
                        intent.putExtra("sJSONDenominatios",oRespuesta.sJSON);

                        startActivity(intent);


                    }

                }else{//No exitoso
                    Toast.makeText(MainActivity.this,
                            "["+oRespuesta.oCodigoRespuesta.id_codigo_respuesta+"] "+
                                    oRespuesta.oCodigoRespuesta.codigo_respuesta+"\n"+
                                    oRespuesta.oCodigoRespuesta.descripcion,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                dialogoProcesando.dismiss();

            }
        }
        @Override
        protected void onCancelled() {
            dialogoProcesando.dismiss();
        }
    }

    //---Fin tarea
}