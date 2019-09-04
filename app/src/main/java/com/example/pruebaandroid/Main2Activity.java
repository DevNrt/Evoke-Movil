package com.example.pruebaandroid;

import android.content.Intent;
import android.os.Bundle;

import com.example.pruebaandroid.fragmentos.FragmentoInicio;
import com.example.pruebaandroid.fragmentos.FragmentoMisiones;
import com.example.pruebaandroid.fragmentos.FragmentoPerfil;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.widget.TextView;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView userName, userEmail;
    private String nombre, apellido, correo, id, token;
    Bundle bundle = new Bundle();
    FragmentoPerfil fragmentoPerfil = new FragmentoPerfil();
    FragmentoInicio fragmentoInicio = new FragmentoInicio();
    FragmentoMisiones fragmentoMisiones=new FragmentoMisiones();

    public Main2Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Crea el intent para saber el  fragmento en el que debe iniciarse
        Intent intent = getIntent();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        if (intent.getStringExtra("page").equals("inicio")) {
            token = intent.getStringExtra("token");
            id = intent.getStringExtra("_id");
            bundle.putString("_id", this.id);
            bundle.putString("token", token);
            fragmentoInicio.setArguments(bundle);
            cargarFragmento(fragmentoInicio);
        } else if (intent.getStringExtra("page").equals("perfil")) {
            token = intent.getStringExtra("token");
            id = intent.getStringExtra("_id");
            bundle.putString("token", token);
            bundle.putString("name", nombre);
            bundle.putString("_id", this.id);
            fragmentoPerfil.setArguments(bundle);
            cargarFragmento(fragmentoPerfil);
        }
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        Intent intent = getIntent();
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        token = intent.getStringExtra("token");
        nombre = intent.getStringExtra("name");
        apellido = intent.getStringExtra("lastName");
        correo = intent.getStringExtra("email");
        id = intent.getStringExtra("_id");
        userName.setText(String.format("%s %s", nombre, apellido));
        userEmail.setText(correo);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            bundle.putString("_id", this.id);
            bundle.putString("token", token);
            fragmentoInicio.setArguments(bundle);
            cargarFragmento(fragmentoInicio);
        } else if (id == R.id.nav_profile) {
            bundle.putString("token", token);
            bundle.putString("name", nombre);
            bundle.putString("_id", this.id);
            fragmentoPerfil.setArguments(bundle);
            cargarFragmento(fragmentoPerfil);
        } else if (id == R.id.nav_pqrs) {

        } else if (id == R.id.nav_reservation) {
            cargarFragmento(fragmentoMisiones);
        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargarFragmento(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedorFragmento, fragment).commit();
    }
}
