package fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reutilizate.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.MensajeAdapter;
import model.Mensaje;

public class ChatFragment extends Fragment {

    // como recibir los argumentos del fragment
    private static final String ARG_YO = "yo";
    private static final String ARG_OTRO = "otro";
    private static final String ARG_NOMBRE = "nombre";

    // ids de los usuarios implicados en el chat
    int yo, otro;

    // Nombre del otro usuario
    String nombreOtro;

    // Elementos de la interfaz
    RecyclerView rv;
    EditText etMensaje;
    Button btnEnviar;

    // Lista de mensajes y su adaptador
    List<Mensaje> lista;
    MensajeAdapter adapter;

    // Método para crear una instancia del fragment con parámetros
    public static ChatFragment newInstance(int yo, int otro, String nombreOtro) {
        ChatFragment f = new ChatFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_YO, yo);
        b.putInt(ARG_OTRO, otro);
        b.putString(ARG_NOMBRE, nombreOtro);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        // Recuperamos los argumentos enviados
        yo = getArguments().getInt(ARG_YO);
        otro = getArguments().getInt(ARG_OTRO);
        nombreOtro = getArguments().getString(ARG_NOMBRE);

        // Enlazamos los elementos del layout
        rv = v.findViewById(R.id.rvMensajes);
        etMensaje = v.findViewById(R.id.etMensaje);
        btnEnviar = v.findViewById(R.id.btnEnviar);

        // Configuramos el RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos la lista de mensajes
        lista = new ArrayList<>();

        // Creamos el adaptador indicando cuál es mi ID
        adapter = new MensajeAdapter(lista, yo);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Hacemos scroll al final cuando se cargue la vista
        rv.post(() -> rv.scrollToPosition(lista.size() - 1));

        // Cargamos los mensajes desde el servidor
        cargarMensajes();

        // Evento del botón enviar
        btnEnviar.setOnClickListener(view -> enviarMensaje());

        return v;
    }

    // Método para obtener los mensajes del servidor
    private void cargarMensajes() {

        // URL con parámetros GET
        String url = "http://10.0.2.2/reutilizate/obtenerMensajes.php?yo=" + yo + "&otro=" + otro;

        // Petición GET
        StringRequest req = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        // Convertimos la respuesta en un array JSON
                        JSONArray arr = new JSONArray(response);

                        // Limpiamos la lista antes de rellenarla
                        lista.clear();

                        // Recorremos todos los mensajes recibidos
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);

                            // Creamos un objeto Mensaje con los datos del JSON
                            Mensaje m = new Mensaje(
                                    0,
                                    o.getInt("id_emisor"),
                                    otro,
                                    o.getString("contenido"),
                                    o.getString("fecha")
                            );

                            lista.add(m);
                        }

                        // Si no hay mensajes previos, mostramos un mensaje automático de match
                        if (lista.isEmpty()) {
                            Mensaje matchMsg = new Mensaje(
                                    0,
                                    0, // emisor = sistema
                                    yo,
                                    "¡MATCH! Habéis hecho match 🎉",
                                    "" // fecha vacía
                            );
                            lista.add(matchMsg);
                        }

                        // Actualizamos el adaptador y hacemos scroll al final
                        adapter.notifyDataSetChanged();
                        rv.scrollToPosition(lista.size() - 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        // Añadimos la petición a la cola de Volley
        Volley.newRequestQueue(getContext()).add(req);
    }

    // Método para enviar un mensaje al servidor
    private void enviarMensaje() {

        // Obtenemos el texto del mensaje
        String contenido = etMensaje.getText().toString().trim();

        // Si está vacío, no hacemos nada
        if (contenido.isEmpty()) return;

        // URL del script PHP
        String url = "http://10.0.2.2/reutilizate/enviarMensaje.php";

        // Petición POST
        StringRequest req = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    // Limpiamos el campo de texto
                    etMensaje.setText("");

                    // Recargamos los mensajes para ver el nuevo
                    cargarMensajes();
                },
                error -> error.printStackTrace()
        ) {
            @Override
            protected Map<String, String> getParams() {

                // Parámetros que enviamos al servidor
                Map<String, String> p = new HashMap<>();
                p.put("emisor", String.valueOf(yo));
                p.put("receptor", String.valueOf(otro));
                p.put("contenido", contenido);

                return p;
            }
        };

        // Añadimos la petición a la cola
        Volley.newRequestQueue(getContext()).add(req);
    }
}
