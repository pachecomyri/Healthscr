package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthscribe.Model.Incompatibilidad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatosIncompatibilidadActivity extends AppCompatActivity {

    private TextView tvMedSelec, tvMedicSelec, tvEnfSelec, tvAlerSelec;

    private Button bEliminar;

    private DatabaseReference refInc;

    final ArrayList<Incompatibilidad> listaInc = new ArrayList<Incompatibilidad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_incompatibilidad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refInc = FirebaseDatabase.getInstance().getReference("Tablas").child("Incompatibilidad");

        tvMedSelec = (TextView)findViewById(R.id.tv_med_inc_sel);
        tvMedicSelec = (TextView)findViewById(R.id.tv_medic_inc_sel);
        tvEnfSelec = (TextView)findViewById(R.id.tv_enf_inc_sel);
        tvAlerSelec = (TextView)findViewById(R.id.tv_aler_inc_sel);
        bEliminar = (Button)findViewById(R.id.b_eli_inc_sel);

        Integer id = getIntent().getIntExtra("incompatibilidadSelec", -1);

       refInc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Incompatibilidad inc = data.getValue(Incompatibilidad.class);
                    listaInc.add(inc);
                   for(Incompatibilidad in: listaInc){
                        if(in.getId().equals(id)){
                            tvMedSelec.setText(String.valueOf(in.getMedicamento()));
                            String listMed = String.valueOf(in.getListaMedicamentos());
                            String listEnf = String.valueOf(in.getListaEnfermedades());
                            String listAler = String.valueOf(in.getListaAlergias());
                                if(in.getListaMedicamentos()==null){
                                    tvMedicSelec.setText("");
                                }else{
                                    tvMedicSelec.setText(listMed.replace("[", "").replace("]", ""));
                                }
                                if(in.getListaEnfermedades()==null){
                                    tvEnfSelec.setText("");
                                }else{
                                    tvEnfSelec.setText(listEnf.replace("[", "").replace("]", ""));
                                }
                                if(in.getListaAlergias()==null){
                                    tvAlerSelec.setText("");
                                }else{
                                    tvAlerSelec.setText(listAler.replace("[", "").replace("]", ""));
                                }


                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

       bEliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder alerta = new AlertDialog.Builder(DatosIncompatibilidadActivity.this);
               alerta.setMessage("¿Está seguro de que desea eliminar esta incompatibilidad?");
               alerta.setCancelable(false);
               alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       refInc.child(String.valueOf(id)).removeValue();
                       Toast.makeText(DatosIncompatibilidadActivity.this, "Esta incopatibilidad ha sido eliminada", Toast.LENGTH_SHORT).show();

                   }
               });

               alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();

                   }
               });
               AlertDialog titulo = alerta.create();
               titulo.setTitle("Eliminar incompatibilidad");
               titulo.show();
           }
       });

    }

    //Método botón modificar
    public void accModificarIncSelec(View view){
        Integer id = getIntent().getIntExtra("incompatibilidadSelec", -1);
        Intent modificaInc = new Intent(DatosIncompatibilidadActivity.this,CambiaIncompatibilidadActivity.class);
        modificaInc.putExtra("incompatibilidadSel",id);
        startActivity(modificaInc);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}