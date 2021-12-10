package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNombre, tvApe, tvDni, tvEmail, tvDir, tvPhone;

    private DatabaseReference refU;

    final ArrayList<Usuario> listaUs = new ArrayList<Usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refU= FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");

        tvNombre = (TextView)findViewById(R.id.tv_nom_pac);
        tvApe = (TextView)findViewById(R.id.tv_ape_pac);
        tvDni = (TextView)findViewById(R.id.tv_dni_pac);
        tvEmail = (TextView)findViewById(R.id.tv_email_pac);
        tvDir = (TextView)findViewById(R.id.tv_dir_pac);
        tvPhone = (TextView)findViewById(R.id.tv_phone_pac);

        Integer id = getIntent().getIntExtra("idPacien",-1);

        refU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Usuario us= data.getValue(Usuario.class);
                    listaUs.add(us);
                    for(Usuario usuar: listaUs){
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
    public void accesoModificaPac(View view){
        Integer id = getIntent().getIntExtra("idPacien",-1);
        Intent accesoMod = new Intent(PerfilActivity.this, ModificaPerfilActivity.class);
        accesoMod.putExtra("idPac",id);
        startActivity(accesoMod);
    }

    //Método cerrar sesión
    public void cerrarSesionPac(View view){
        Intent salir = new Intent(PerfilActivity.this, InicioActivity.class);
        startActivity(salir);
        finish();
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