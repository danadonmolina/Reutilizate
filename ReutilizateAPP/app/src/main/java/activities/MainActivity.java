package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.example.reutilizate.R;

import fragments.MatchesFragment;
import fragments.MisObjetosFragment;
import fragments.ObjetosFragment;
import fragments.PublicarFragment;
import fragments.SuscripcionFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Contenedor principal del menú lateral
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comprobación de sesión mediante SharedPreferences
        SharedPreferences prefs = getSharedPreferences("usuario", MODE_PRIVATE);
        boolean logueado = prefs.getBoolean("logueado", false);

        // Si el usuario no está logueado, lo enviamos al Login
        if (!logueado) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Cargamos el layout principal
        setContentView(R.layout.activity_main);

        // Configuración de la toolbar personalizada
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ocultamos el título por defecto y cargamos un layout personalizado
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);

        // Configuración del DrawerLayout y el menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Activamos el icono hamburguesa para abrir/cerrar el menú lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Cargamos el fragmento inicial (pantalla de swipe)
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ObjetosFragment())
                    .commit();
        }
    }


     // Método que gestiona las opciones seleccionadas del menú lateral.

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        // Inicio que es la pantalla de swipe
        if (id == R.id.nav_inicio) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ObjetosFragment())
                    .commit();

            // Publicar objeto
        } else if (id == R.id.nav_publicar) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new PublicarFragment())
                    .commit();

            // Matches
        } else if (id == R.id.nav_matches) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MatchesFragment())
                    .commit();

            // Suscripción
        } else if (id == R.id.nav_suscripcion) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SuscripcionFragment())
                    .commit();

            // Cerrar sesión
        } else if (id == R.id.nav_logout) {

            // Borramos todos los datos de sesión
            getSharedPreferences("usuario", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Redirigimos al login limpiando el historial
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            // Mis objetos publicados
        } else if (id == R.id.nav_mis_objetos) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MisObjetosFragment())
                    .commit();
        }

        // Cerramos el menú lateral después de seleccionar una opción
        drawerLayout.closeDrawers();
        return true;
    }
}
