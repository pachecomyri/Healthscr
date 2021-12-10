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

import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatosUsuarioActivity extends AppCompatActivity {

    private TextView tvNombreSelec, tvApeSelec, tvDniSelec, tvEmailSelec, tvDirSelec, tvPhoneSelec;

    private Button bEliminar;

    private DatabaseReference refU, refPac;

    final ArrayList<Usuario> listaUs = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refU= FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");

        tvNombreSelec = (TextView)findViewById(R.id.tv_nom_usu_sel);
        tvApeSelec = (TextView)findViewById(R.id.tv_ape_usu_sel);
        tvDniSelec = (TextView)findViewById(R.id.tv_dni_usu_sel);
        tvEmailSelec = (TextView)findViewById(R.id.tv_ema_usu_sel);
        tvDirSelec = (TextView)findViewById(R.id.tv_dir_usu_sel);
        tvPhoneSelec = (TextView)findViewById(R.id.tv_telef_usu_sel);
        bEliminar = (Button)findViewById(R.id.b_eli_usu_sel);

        Integer id = getIntent().getIntExtra("usuarioSelec",-1);

        refU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Usuario us = data.getValue(Usuario.class);
                    listaUs.add(us);
                    for(Usuario usuar: listaUs){
                        if(usuar.getId().equals(id)){
                            tvNombreSelec.setText(usuar.getNombre());
                            tvApeSelec.setText(usuar.getApellidos());
                            tvDniSelec.setText(usuar.getDni());
                            tvEmailSelec.setText(usuar.getEmail());
                            tvDirSelec.setText(usuar.getDireccion());
                            tvPhoneSelec.setText(usuar.getTelefono());

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
                AlertDialog.Builder alerta = new AlertDialog.Builder(DatosUsuarioActivity.this);
                alerta.setMessage("¿Está seguro de que desea eliminar este usuario?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refU.child(String.valueOf(id)).removeValue();
                        refPac.child(String.valueOf(id)).removeValue();
                        Toast.makeText(DatosUsuarioActivity.this,"Este usuario ha sido eliminado",Toast.LENGTH_SHORT).show();
                    }
                });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar usuario");
                titulo.show();
            }
        });
    }

    //método botón modificar
    public void accModificarUsuarioSelec(View view){
        Integer id = getIntent().getIntExtra("usuarioSelec",-1);
        Intent modifica = new Intent(DatosUsuarioActivity.this, CambiaUsuarioActivity.class);
        modifica.putExtra("usuarioSel",id);
        startActivity(modifica);

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

