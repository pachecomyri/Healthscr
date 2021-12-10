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

import com.example.healthscribe.Model.HistoriaClinica;
import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {

    private EditText etnombre, etapellidos, etdni, etemail, etdir, etphone, etcont;

    private DatabaseReference refUsuario, refPaciente, refHistoria;

    final ArrayList<Usuario> listUsuarios = new ArrayList<Usuario>();
    final ArrayList<HistoriaClinica> listaHistoria = new ArrayList<HistoriaClinica>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refUsuario = FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");
        refPaciente = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");
        refHistoria = FirebaseDatabase.getInstance().getReference("Tablas").child("HistoriaClinica");

        etnombre = (EditText) findViewById(R.id.txt_nombre);
        etapellidos = (EditText) findViewById(R.id.txt_apellidos);
        etdni = (EditText) findViewById(R.id.txt_dni);
        etemail = (EditText) findViewById(R.id.txt_email);
        etdir = (EditText) findViewById(R.id.txt_direccion);
        etphone = (EditText) findViewById(R.id.txt_phone);
        etcont = (EditText) findViewById(R.id.txt_cont);

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

        refHistoria.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    HistoriaClinica h = data.getValue(HistoriaClinica.class);
                    listaHistoria.add(h);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
    }

    public void registrarUsuario(View view){
        String nombre = etnombre.getText().toString();
        String apellidos = etapellidos.getText().toString();
        String dni = etdni.getText().toString();
        String email = etemail.getText().toString();
        String direccion = etdir.getText().toString();
        String telefono = etphone.getText().toString();
        String cont = etcont.getText().toString();
        String rol = "paciente";


        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && !TextUtils.isEmpty(dni) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(direccion) && !TextUtils.isEmpty(telefono) && !TextUtils.isEmpty(cont)) {
            if (!listUsuarios.isEmpty()) {
                for (Usuario usuar : listUsuarios) {
                    if (usuar.getDni().equals(dni)) {
                        Toast.makeText(this, "El usuario " + dni + " ya existe", Toast.LENGTH_LONG).show();
                    }else{
                        Usuario u = listUsuarios.get(listUsuarios.size()-1);
                        Integer id = u.getId()+1;
                        Integer idHis = listaHistoria.size()+1;
                        HistoriaClinica h = new HistoriaClinica(idHis, null, null, null,null);
                        refHistoria.child(String.valueOf(idHis)).setValue(h);
                        if(cont.length()>=6){
                            Usuario us = new Usuario(id, nombre, apellidos, dni, email, direccion, telefono, cont, rol);
                            refUsuario.child(String.valueOf(id)).setValue(us);
                            Toast.makeText(this, "El usuario " + dni + " ha sido registrado", Toast.LENGTH_LONG).show();
                            Paciente p = new Paciente(id, us, h);
                            refPaciente.child(String.valueOf(id)).setValue(p);
                            etnombre.setText("");
                            etapellidos.setText("");
                            etdni.setText("");
                            etemail.setText("");
                            etdir.setText("");
                            etphone.setText("");
                            etcont.setText("");}
                        else{
                            Toast.makeText(this, "La contraseña debe contener mínimo 6 dígitos",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }else{
                Integer id = listUsuarios.size()+1;
                Integer idHis = listaHistoria.size()+1;
                HistoriaClinica h = new HistoriaClinica(idHis, null, null, null,null);
                refHistoria.child(String.valueOf(idHis)).setValue(h);





                if(cont.length()>=6) {
                    Usuario us = new Usuario(id, nombre, apellidos, dni, email, direccion, telefono, cont, rol);
                    refUsuario.child(String.valueOf(id)).setValue(us);
                    Toast.makeText(this, "El usuario " + dni + " ha sido registrado", Toast.LENGTH_LONG).show();
                    Paciente p = new Paciente(id, us, h);
                    refPaciente.child(String.valueOf(id)).setValue(p);
                    etnombre.setText("");
                    etapellidos.setText("");
                    etdni.setText("");
                    etemail.setText("");
                    etdir.setText("");
                    etphone.setText("");
                    etcont.setText("");
                }else{
                    Toast.makeText(this, "La contraseña debe contener mínimo 6 dígitos",Toast.LENGTH_LONG).show();
                }
            }

        }else{
            Toast.makeText(this, "Se deben rellenar todos los campos", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}