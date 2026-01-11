package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;   // ← IMPORTANTE
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

public class LoginActivity extends AppCompatActivity {

    EditText etCorreo, etContrasena;
    Button btnLogin, btnRegistro;

    // URL DEL SCRIPT DEL PHP
    private final String URL_LOGIN = "http://10.0.2.2/reutilizate/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnLogin.setOnClickListener(v -> {

            // Obtenemos los valores que introduce el usuario
            String correo = etCorreo.getText().toString().trim();
            String pass = etContrasena.getText().toString().trim();

            // Logs para ver que lee android
            Log.d("LOGIN", "Correo introducido: '" + correo + "'");
            Log.d("LOGIN", "Password introducida: '" + pass + "'");

            // Validamos los campos vacios
            if (correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Si esta correcto lo intentamos
                login(correo, pass);
            }
        });

        btnRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    // Metodo q se encarga de enviar el post al servidor para validar creedenciales
    private void login(String correo, String password) {

        // Log de la respuesta recibida
        Log.d("LOGIN", "Enviando petición a: " + URL_LOGIN);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_LOGIN,
                response -> {

                    Log.d("LOGIN", "Respuesta cruda del servidor: '" + response + "'");

                    response = response.trim();

                    // Si el servidor devuelve un id el lolgin es correcto
                    if (!response.equals("ERROR")) {

                        int idUsuario = Integer.parseInt(response);

                        // Guardamos los datos
                        getSharedPreferences("usuario", MODE_PRIVATE)
                                .edit()
                                .putBoolean("logueado", true)
                                .putInt("id_usuario", idUsuario)
                                .putString("correo", correo)
                                .apply();

                        Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LOGIN", "Error Volley: " + error.toString());
                    Toast.makeText(this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Log.d("LOGIN", "Enviando POST: correo='" + correo + "', password='" + password + "'");

                // Parametros del post
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                params.put("password", password);
                return params;
            }
        };

        // Añadimos peticion a la cola
        queue.add(request);
    }
}
