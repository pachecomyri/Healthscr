package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EntrarActivity extends AppCompatActivity {

    private EditText etdni, etcont;


    private DatabaseReference refUsuario;

    final ArrayList<Usuario> listUsuarios = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        refUsuario = FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");

        etdni = (EditText)findViewById(R.id.t_dni);
        etcont = (EditText)findViewById(R.id.t_cont);

        refUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Usuario us = data.getValue(Usuario.class);
                    listUsuarios.add(us);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });


    }

    public void login(View view){
        String dni = etdni.getText().toString();
        String cont = etcont.getText().toString();
        String rol1 = "paciente";
        String rol2 = "medico";
        String rol3= "administrador";

        if(!TextUtils.isEmpty(dni) && !TextUtils.isEmpty(cont)){
            for(Usuario usuar : listUsuarios){
                if(usuar.getDni().equals(dni) && usuar.getCont().equals(cont) && usuar.getRol().equals(rol3)) {
                    Intent menuAdm = new Intent(EntrarActivity.this, MenuAdminActivity.class);
                    menuAdm.putExtra("idAdministrador", usuar.getId());
                    startActivity(menuAdm);
                    etdni.setText("");
                    etcont.setText("");
                    break;

                } else if(usuar.getDni().equals(dni) && usuar.getCont().equals(cont) && usuar.getRol().equals(rol1)){
                    Intent menuPac = new Intent(EntrarActivity.this, MenuActivity.class);
                    menuPac.putExtra("idPaciente",usuar.getId());
                    startActivity(menuPac);
                    etdni.setText("");
                    etcont.setText("");
                    break;

                }else if(usuar.getDni().equals(dni) && usuar.getCont().equals(cont) && usuar.getRol().equals(rol2)){
                    Intent menuMed = new Intent(EntrarActivity.this, MenuMedicoActivity.class);
                    menuMed.putExtra("idMedico", usuar.getId());
                    startActivity(menuMed);
                    etdni.setText("");
                    etdni.setText("");
                    break;

                } else if (usuar.getDni().equals(dni) && !usuar.getCont().equals(cont)) {
                    Toast.makeText(EntrarActivity.this, "La contraseña es incorrecta", Toast.LENGTH_LONG).show();
                    etcont.setText("");
                    break;

                }else if (!usuar.getDni().equals(dni) && usuar.getCont().equals(cont)){
                    Toast.makeText(EntrarActivity.this, "El dni es incorrecto", Toast.LENGTH_LONG).show();
                    etdni.setText("");
                    break;
                }

            }
        }

        else{
            Toast.makeText(EntrarActivity.this, "Se deben rellenar todos los campos", Toast.LENGTH_LONG).show();
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