package activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reutilizate.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // Campos del formulario
    EditText etNombre, etCorreo, etContrasena;
    Button btnRegistrar;

    // URL del script PHP encargado del registro
    // 10.0.2.2 es la IP especial para acceder al localhost desde el emulador
    private final String URL_REGISTER = "http://10.0.2.2/reutilizate/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Enlazamos los elementos del layout
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Acción del botón de registro
        btnRegistrar.setOnClickListener(v -> {

            // Obtenemos los valores introducidos por el usuario
            String nombre = etNombre.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String pass = etContrasena.getText().toString().trim();

            // Validación básica de campos vacíos
            if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Si todo está correcto, enviamos los datos al servidor
                registrarUsuario(nombre, correo, pass);
            }
        });
    }


    // Envia los datos del usuario al servidor mediante una petición POST.

    private void registrarUsuario(String nombre, String correo, String password) {

        // Cola de peticiones de Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Petición POST al servidor
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_REGISTER,
                response -> {
                    // Eliminamos espacios por si el servidor devuelve saltos de línea
                    response = response.trim();

                    // Si el servidor responde "OK", el registro fue exitoso
                    if (response.equals("OK")) {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                        finish(); // Volvemos a la pantalla anterior (Login)
                    } else {
                        Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Error de conexión o fallo en la petición
                    Toast.makeText(this,
                            "Error de conexión: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                // Parámetros que se enviarán en el POST
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("correo", correo);
                params.put("password", password);

                return params;
            }
        };

        // Añadimos la petición a la cola para que se ejecute
        queue.add(request);
    }
}
