package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthscribe.Model.Paciente;
import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CambiaUsuarioActivity extends AppCompatActivity {

    private EditText etNomSelec, etApeSelec, etDniSelec, etEmailSelec, etDirSelec, etPhoneSelec, etContSelec;

    private DatabaseReference refU, refPac;

    final ArrayList<Usuario> listaUs = new ArrayList<Usuario>();
    final ArrayList<Paciente> listaPac = new ArrayList<Paciente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refU= FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");
        refPac = FirebaseDatabase.getInstance().getReference("Tablas").child("Paciente");


        etNomSelec = (EditText)findViewById(R.id.txt_nom_usu_sel);
        etApeSelec = (EditText)findViewById(R.id.txt_ape_usu_sel);
        etDniSelec = (EditText)findViewById(R.id.txt_dni_usu_sel);
        etEmailSelec = (EditText)findViewById(R.id.txt_ema_usu_sel);
        etDirSelec = (EditText)findViewById(R.id.txt_dir_usu_sel);
        etPhoneSelec = (EditText)findViewById(R.id.txt_telef_usu_sel);
        etContSelec = (EditText)findViewById(R.id.txt_cont_usu_sel);

        Integer id = getIntent().getIntExtra("usuarioSel", -1);

        refU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Usuario us = data.getValue(Usuario.class);
                    listaUs.add(us);
                    for(Usuario usuar: listaUs){
                        if(usuar.getId().equals(id)){
                            etNomSelec.setText(usuar.getNombre());
                            etApeSelec.setText(usuar.getApellidos());
                            etDniSelec.setText(usuar.getDni());
                            etEmailSelec.setText(usuar.getEmail());
                            etDirSelec.setText(usuar.getDireccion());
                            etPhoneSelec.setText(usuar.getTelefono());
                            etContSelec.setText(usuar.getCont());

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        refPac.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Paciente p = data.getValue(Paciente.class);
                    listaPac.add(p);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });
    }

    //Método modificar
    public void modificarUsuarioSelec(View view){
        Integer id = getIntent().getIntExtra("usuarioSel",-1);
        String nomS= etNomSelec.getText().toString();
        String apeS= etApeSelec.getText().toString();
        String dniS= etDniSelec.getText().toString();
        String emailS=etEmailSelec.getText().toString();
        String dirS=etDirSelec.getText().toString();
        String telefS=etPhoneSelec.getText().toString();
        String contS=etContSelec.getText().toString();

        for(Usuario usuar:listaUs) {
            if (usuar.getId().equals(id)) {
                usuar.setNombre(nomS);
                usuar.setApellidos(apeS);
                usuar.setDni(dniS);
                usuar.setEmail(emailS);
                usuar.setDireccion(dirS);
                usuar.setTelefono(telefS);
                usuar.setCont(contS);

                refU.child(String.valueOf(id)).setValue(usuar);

                Toast.makeText(CambiaUsuarioActivity.this, "El usuario " + usuar.getDni() + " ha sido modificado", Toast.LENGTH_LONG).show();

                if(usuar.getRol().equals("paciente")){
                    for(Paciente pac: listaPac){
                        if(pac.getId().equals(id)){
                            pac.setUsuario(usuar);
                            refPac.child(String.valueOf(id)).setValue(pac);
                        }
                    }
                }
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