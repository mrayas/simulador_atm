package com.mario.rayasc.atm;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;


import java.util.ArrayList;

import com.squareup.picasso.Picasso;

public class ListInfoActivity extends AppCompatActivity {


    Model.Denomination[] maoObject=null;
    int INDEX=0;
    boolean bHacerDistribucion=false;
    boolean bVerTienda=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list_info);



            Intent intent = getIntent();

            //Equipo
            String sJSONDenominatios = intent.getStringExtra("sJSONDenominatios");
            if (sJSONDenominatios!=null && !sJSONDenominatios.equals("")) {

                maoObject=Model.Denomination.faoObjet(sJSONDenominatios);

                ArrayList<Model.Denomination> alObject = new ArrayList<Model.Denomination>();
                Double dTotal=0.0;
                for(int i=0;i<maoObject.length;i++){

                    alObject.add(maoObject[i]);
                    dTotal=dTotal+(maoObject[i].getIdDenomination() * maoObject[i].getQuantity());
                }

                RecyclerViewAdapter adListaInfo = new RecyclerViewAdapter(alObject);
                RecyclerView rvListaInfo =  (RecyclerView)findViewById(R.id.rvListaInfo);
                rvListaInfo.setHasFixedSize(true);
                rvListaInfo.setAdapter(adListaInfo);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rvListaInfo.setLayoutManager(llm);
                Toast.makeText(ListInfoActivity.this,
                        maoObject.length+" denominaciones diferentes por un total de $"+dTotal,
                        Toast.LENGTH_LONG).show();

            }



        } catch (Exception e) {
            //Funciones.fEscribeLog("En ListInfoActivity.onCreate exepción:\n"+e.toString());

        }
    }

    @Override
    public void finish() {
        // Reset the animation to avoid flickering.
        Intent returnIntent = new Intent();
        try {



        } catch (Exception e) {
            //showResponse("En setupAutoCompleteFragment: "+ e.toString());
            setResult(ListInfoActivity.RESULT_CANCELED, returnIntent);
        }
        super.finish();
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        public ArrayList<Model.Denomination> myValues;
        public RecyclerViewAdapter (ArrayList<Model.Denomination> myValues){
            this.myValues= myValues;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_info, parent, false);
            return new MyViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            //holder.myTextView.setText(myValues.get(position));
            try {
                INDEX=position-1;


                //String sURLLogo="";
                //Picasso.with(ListInfoActivity.this).load(sURLLogo).into(holder.imgInfo);

                holder.txtDenomination.setText("$"+maoObject[position].getIdDenomination()+"");
                String sType="";
                if (maoObject[position].getType().equals("M")) {
                    sType = "Moneda";
                    holder.imgType.setImageResource(R.drawable.coin);
                }
                else {
                    sType="Billete";
                    holder.imgType.setImageResource(R.drawable.bill);
                }
                holder.txtType.setText("Tipo "+sType);

                holder.txtQuantity.setText("Cantidad "+maoObject[position].getQuantity()+"");








            } catch (Exception e) {

                Toast.makeText(ListInfoActivity.this,
                        "En ListInfoActivity.onBindViewHolder exepción:\n"+e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public int getItemCount() {
            return myValues.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView txtDenomination,txtQuantity,txtType;
            ImageView imgType;
            public MyViewHolder(View itemView) {
                super(itemView);

                //imgInfo=findViewById(R.id.imgInfo);

                imgType = (ImageView)itemView.findViewById(R.id.imgType);
                txtDenomination = (TextView)itemView.findViewById(R.id.txtDenomination);
                txtQuantity = (TextView)itemView.findViewById(R.id.txtQuantity);
                txtType = (TextView)itemView.findViewById(R.id.txtType);


            }
        }
    }


}

