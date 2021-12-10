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

import com.example.healthscribe.Model.Enfermedad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CrearEnfermedadActivity extends AppCompatActivity {

    private EditText etnombre, etsintomas;
    private DatabaseReference refEnfermedad;
    final ArrayList<Enfermedad> listEnfermedad = new ArrayList<Enfermedad>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_enfermedad);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refEnfermedad = FirebaseDatabase.getInstance().getReference("Tablas").child("Enfermedad");

        etnombre = (EditText)findViewById(R.id.txt_nombre_enferm);
        etsintomas = (EditText)findViewById(R.id.txt_sintomas_enferm);

        refEnfermedad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Enfermedad e = data.getValue(Enfermedad.class);
                    listEnfermedad.add(e);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });
    }

    public void crearEnfermedad(View view){
        String nombre = etnombre.getText().toString();
        String sintomas= etsintomas.getText().toString();
        Integer tipo = 0;

        if(!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(sintomas)){
            if(!listEnfermedad.isEmpty()){
                for(Enfermedad e: listEnfermedad){
                    if(e.getNombre().equals(nombre)){
                        Toast.makeText(this, "La enfermedad " + nombre + " ya existe", Toast.LENGTH_LONG).show();
                    }else{
                        Enfermedad en = listEnfermedad.get(listEnfermedad.size()-1);
                        Integer id = en.getId()+1;
                        Enfermedad enf = new Enfermedad(id, nombre, tipo, sintomas);
                        refEnfermedad.child(String.valueOf(id)).setValue(enf);
                        Toast.makeText(this, "La enfermedad " + nombre + " ha sido registrada", Toast.LENGTH_LONG).show();
                        etnombre.setText("");
                        etsintomas.setText("");
                    }
                }

            }else{
                Integer id = listEnfermedad.size()+1;
                Enfermedad enf = new Enfermedad(id, nombre, tipo, sintomas);
                refEnfermedad.child(String.valueOf(id)).setValue(enf);
                Toast.makeText(this, "La enfermedad " + nombre + " ha sido registrada", Toast.LENGTH_LONG).show();
                etnombre.setText("");
                etsintomas.setText("");

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}