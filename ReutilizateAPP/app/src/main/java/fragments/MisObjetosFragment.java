package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reutilizate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.ObjetoAdapter;
import model.Objeto;

public class MisObjetosFragment extends Fragment {

    // RecyclerView donde se mostrarán los objetos del usuario
    private RecyclerView rvObjetos;

    // Adaptador que gestiona cómo se muestran los objetos
    private ObjetoAdapter adapter;

    // Lista que contendrá los objetos del usuario
    private List<Object> listaObjetos;

    // URL del script PHP que devuelve los objetos del usuario
    private final String URL_OBJETOS = "http://10.0.2.2/reutilizate/obtenerMisObjetos.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_objetos, container, false);

        // Enlazamos el RecyclerView del layout
        rvObjetos = view.findViewById(R.id.rvObjetos);

        // Configuramos el RecyclerView con un layout vertical
        rvObjetos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos la lista de objetos
        listaObjetos = new ArrayList<>();

        // Creamos el adaptador pasándole la lista y el contexto
        adapter = new ObjetoAdapter(listaObjetos, getContext());

        // Asignamos el adaptador al RecyclerView
        rvObjetos.setAdapter(adapter);

        // Cargamos los objetos del usuario desde el servidor
        cargarObjetos();

        return view;
    }

    // Método que obtiene los objetos del usuario desde el servidor
    private void cargarObjetos() {

        // Obtenemos el contexto del fragment
        Context ctx = getContext();
        if (ctx == null) return;

        // Recuperamos el ID del usuario almacenado en SharedPreferences
        SharedPreferences prefs = ctx.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);

        // Creamos la cola de peticiones de Volley
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Petición POST al servidor
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_OBJETOS,
                response -> {
                    try {
                        // Convertimos la respuesta en un array JSON
                        JSONArray array = new JSONArray(response);

                        // Limpiamos la lista antes de rellenarla
                        listaObjetos.clear();

                        // Recorremos cada objeto del JSON
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            // Creamos un objeto Objeto con los datos recibidos
                            Objeto objeto = new Objeto(
                                    obj.getInt("id"),
                                    obj.getString("nombre"),
                                    obj.getString("descripcion"),
                                    null, // este campo no se usa aquí
                                    obj.optString("imagen", "")
                            );

                            // Añadimos el objeto a la lista
                            listaObjetos.add(objeto);
                        }

                        // Notificamos al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        // Error al procesar el JSON
                        Toast.makeText(ctx, "Error al procesar datos", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Error de conexión o fallo en la petición
                    Toast.makeText(ctx, "Error al cargar objetos", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                // Parámetros que enviamos al servidor en el POST
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));

                return params;
            }
        };

        // Añadimos la petición a la cola para que se ejecute
        queue.add(request);
    }
}
