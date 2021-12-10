package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class RegistrarMedicoActivity extends AppCompatActivity {

    private EditText etnombre, etapellidos, etdni, etemail, etdir, etphone, etcont;

    private DatabaseReference refUsuario;

    final ArrayList<Usuario> listUsuarios = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_medico);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        refUsuario = FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");

        etnombre = (EditText) findViewById(R.id.txt_nombre_med);
        etapellidos = (EditText) findViewById(R.id.txt_apellidos_med);
        etdni = (EditText) findViewById(R.id.txt_dni_med);
        etemail = (EditText) findViewById(R.id.txt_email_med);
        etdir = (EditText) findViewById(R.id.txt_direccion_med);
        etphone = (EditText) findViewById(R.id.txt_phone_med);
        etcont = (EditText) findViewById(R.id.txt_cont_med);

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

    public void registrarMedico(View view){
        String nombre = etnombre.getText().toString();
        String apellidos = etapellidos.getText().toString();
        String dni = etdni.getText().toString();
        String email = etemail.getText().toString();
        String direccion = etdir.getText().toString();
        String telefono = etphone.getText().toString();
        String cont = etcont.getText().toString();
        String rol = "medico";

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && !TextUtils.isEmpty(dni) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(direccion) && !TextUtils.isEmpty(telefono) && !TextUtils.isEmpty(cont)) {
            if (!listUsuarios.isEmpty()) {
                for (Usuario usuar : listUsuarios) {
                    if (usuar.getDni().equals(dni)) {
                        Toast.makeText(this, "El médico " + dni + " ya existe", Toast.LENGTH_LONG).show();
                    } else {
                        Usuario u = listUsuarios.get(listUsuarios.size()-1);
                        Integer id = u.getId()+1;
                        if (cont.length() >= 6) {
                            Usuario us = new Usuario(id, nombre, apellidos, dni, email, direccion, telefono, cont, rol);
                            refUsuario.child(String.valueOf(id)).setValue(us);
                            Toast.makeText(this, "El médico " + dni + " ha sido registrado", Toast.LENGTH_LONG).show();
                            etnombre.setText("");
                            etapellidos.setText("");
                            etdni.setText("");
                            etemail.setText("");
                            etdir.setText("");
                            etphone.setText("");
                            etcont.setText("");
                        } else {
                            Toast.makeText(this, "La contraseña debe contener mínimo 6 dígitos",Toast.LENGTH_LONG).show();

                        }
                    }
                }
            } else {
                Integer id = listUsuarios.size() + 1;
                if (cont.length() >= 6) {
                    Usuario us = new Usuario(id, nombre, apellidos, dni, email, direccion, telefono, cont, rol);
                    refUsuario.child(String.valueOf(id)).setValue(us);
                    Toast.makeText(this, "El médico " + dni + " ha sido registrado", Toast.LENGTH_LONG).show();
                    etnombre.setText("");
                    etapellidos.setText("");
                    etdni.setText("");
                    etemail.setText("");
                    etdir.setText("");
                    etphone.setText("");
                    etcont.setText("");
                } else {
                    Toast.makeText(this, "La contraseña debe contener mínimo 6 dígitos",Toast.LENGTH_LONG).show();

                }
            }
        }
        else{
            Toast.makeText(this, "Se deben rellenar todos los campos", Toast.LENGTH_LONG).show();
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