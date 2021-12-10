package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModificaMedicoActivity extends AppCompatActivity {

    private EditText etNomMod, etApeMod, etDniMod, etEmailMod, etDirMod, etPhoneMod, etContMod;

    private DatabaseReference refU;

    final ArrayList<Usuario> listaUs = new ArrayList<Usuario>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_medico);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refU = FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");

        etNomMod=(EditText)findViewById(R.id.txt_med_nom);
        etApeMod = (EditText)findViewById(R.id.txt_med_ape);
        etDniMod = (EditText) findViewById(R.id.txt_med_dni);
        etEmailMod=(EditText)findViewById(R.id.txt_med_email);
        etDirMod=(EditText)findViewById(R.id.txt_med_dir);
        etPhoneMod=(EditText) findViewById(R.id.txt_med_phone);
        etContMod=(EditText)findViewById(R.id.txt_med_cont);

        Integer id = getIntent().getIntExtra("idMed",-1);

        refU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Usuario us= data.getValue(Usuario.class);
                    listaUs.add(us);
                    for(Usuario usuar:listaUs){
                        if(usuar.getId().equals(id)){
                            etNomMod.setText(usuar.getNombre());
                            etApeMod.setText(usuar.getApellidos());
                            etDniMod.setText(usuar.getDni());
                            etEmailMod.setText(usuar.getEmail());
                            etDirMod.setText(usuar.getDireccion());
                            etPhoneMod.setText(usuar.getTelefono());
                            etContMod.setText(usuar.getCont());
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

    //Método botón Modificar

    public void modificarMed(View view) {
        Integer id = getIntent().getIntExtra("idMed",-1);
        String nombre = etNomMod.getText().toString();
        String ape = etApeMod.getText().toString();
        String dni = etDniMod.getText().toString();
        String email = etEmailMod.getText().toString();
        String dir = etDirMod.getText().toString();
        String phone = etPhoneMod.getText().toString();
        String cont = etContMod.getText().toString();

        for(Usuario usuar:listaUs){
            if(usuar.getId().equals(id)){
                usuar.setNombre(nombre);
                usuar.setApellidos(ape);
                usuar.setDni(dni);
                usuar.setEmail(email);
                usuar.setDireccion(dir);
                usuar.setTelefono(phone);
                usuar.setCont(cont);

                refU.child(String.valueOf(id)).setValue(usuar);

                Toast.makeText(ModificaMedicoActivity.this, "El usuario " + usuar.getId() + " ha sido modificado", Toast.LENGTH_LONG).show();
            }
        }

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