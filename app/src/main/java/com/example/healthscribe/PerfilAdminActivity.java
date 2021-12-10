package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerfilAdminActivity extends AppCompatActivity {

    private TextView tvNombre, tvApe, tvDni, tvEmail, tvDir, tvPhone;

    private DatabaseReference refU;

    final ArrayList<Usuario> listaUs = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refU= FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");

        tvNombre = (TextView)findViewById(R.id.tv_nom_admin);
        tvApe = (TextView)findViewById(R.id.tv_ape_admin);
        tvDni = (TextView)findViewById(R.id.tv_dni_admin);
        tvEmail = (TextView)findViewById(R.id.tv_email_admin);
        tvDir = (TextView)findViewById(R.id.tv_dir_admin);
        tvPhone = (TextView)findViewById(R.id.tv_phone_admin);

        Integer id = getIntent().getIntExtra("idAdminist",-1);

        refU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Usuario us= data.getValue(Usuario.class);
                    listaUs.add(us);
                    for(Usuario usuar:listaUs){
                        if(usuar.getId().equals(id)){
                            tvNombre.setText(usuar.getNombre());
                            tvApe.setText(usuar.getApellidos());
                            tvDni.setText(usuar.getDni());
                            tvEmail.setText(usuar.getEmail());
                            tvDir.setText(usuar.getDireccion());
                            tvPhone.setText(usuar.getTelefono());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
    }

    //Método para acceder a Modificar
    public void accesoModificaAdmin(View view){
        Integer id = getIntent().getIntExtra("idAdminist",-1);
        Intent accesoMod = new Intent(PerfilAdminActivity.this, ModificaAdminActivity.class);
        accesoMod.putExtra("idAdmin",id);
        startActivity(accesoMod);
    }

    //Método cerrar sesión
    public void cerrarSesionAdmin(View view){
        Intent salir = new Intent(PerfilAdminActivity.this, InicioActivity.class);
        startActivity(salir);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}