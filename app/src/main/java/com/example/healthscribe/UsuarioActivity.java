package com.example.healthscribe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.healthscribe.Model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsuarioActivity extends AppCompatActivity {

    private ListView listUser;
    private ArrayAdapter<Usuario> usuarioAdapter;
    private DatabaseReference refUsuarios;
    private ChildEventListener userListener;

    final ArrayList<Usuario> listUsuario = new ArrayList<Usuario>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refUsuarios = FirebaseDatabase.getInstance().getReference("Tablas").child("Usuario");

        listUser = (ListView)findViewById(R.id.lv_usuario);

        refUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    Usuario u = data.getValue(Usuario.class);
                    listUsuario.add(u);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());

            }
        });

        final ArrayList<Usuario> lista = new ArrayList<>();
        usuarioAdapter = new ArrayAdapter<Usuario>(UsuarioActivity.this, android.R.layout.simple_list_item_1, lista);
        listUser.setAdapter(usuarioAdapter);

        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UsuarioActivity.this, lista.get(position).getNombre()+" "+lista.get(position).getApellidos(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UsuarioActivity.this, DatosUsuarioActivity.class);
                intent.putExtra("usuarioSelec", lista.get(position).getId());
                startActivity(intent);
            }
        });

        userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario u = snapshot.getValue(Usuario.class);
                usuarioAdapter.add(u);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        refUsuarios.addChildEventListener(userListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busqueda, menu);
        MenuItem item = menu.findItem(R.id.menuBusqueda);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usuarioAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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