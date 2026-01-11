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
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class ObjetosFragment extends Fragment {

    // RecyclerView donde se mostrarán los objetos
    private RecyclerView rvObjetos;

    // Adaptador que gestiona objetos y anuncios
    private ObjetoAdapter adapter;

    // Lista real de objetos recibidos del servidor
    private List<Objeto> listaObjetos;

    // Lista final que mezcla objetos y anuncios
    private List<Object> listaFinal;

    // URL del script PHP que devuelve los objetos
    private final String URL_OBJETOS = "http://10.0.2.2/reutilizate/obtenerObjetos.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_objetos, container, false);

        // Enlazamos el RecyclerView
        rvObjetos = view.findViewById(R.id.rvObjetos);

        // Configuramos el RecyclerView con un layout vertical
        rvObjetos.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos listas
        listaObjetos = new ArrayList<>();
        listaFinal = new ArrayList<>();

        // Creamos el adaptador pasándole la lista final
        adapter = new ObjetoAdapter(listaFinal, getContext());
        rvObjetos.setAdapter(adapter);

        // Cargamos los objetos desde el servidor
        cargarObjetos();

        // Activamos el swipe para LIKE
        activarSwipe();

        return view;
    }

    // Método que obtiene los objetos del servidor
    private void cargarObjetos() {

        Context ctx = getContext();
        if (ctx == null) return;

        // Obtenemos el ID del usuario desde SharedPreferences
        SharedPreferences prefs = ctx.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);

        // Cola de peticiones de Volley
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

                        // Recorremos cada objeto recibido
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

                            listaObjetos.add(objeto);
                        }

                        // Generamos la lista final con anuncios si procede
                        generarListaFinal();

                    } catch (JSONException e) {
                        Toast.makeText(ctx, "Error al procesar datos", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(ctx, "Error al cargar objetos", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                // Obtenemos el ID del usuario otra vez por seguridad
                SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                int idUsuario = prefs.getInt("id_usuario", -1);

                // Parámetros del POST
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));

                return params;
            }
        };

        // Añadimos la petición a la cola
        queue.add(request);
    }

    // Genera la lista final mezclando objetos y anuncios
    private void generarListaFinal() {

        // Comprobamos si el usuario es premium
        SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        boolean esPremium = prefs.getBoolean("premium", false);

        // Limpiamos la lista final
        listaFinal.clear();

        // Si NO es premium, añadimos un anuncio cada 3 objetos
        if (!esPremium) {
            for (int i = 0; i < listaObjetos.size(); i++) {

                listaFinal.add(listaObjetos.get(i));

                // Cada 3 objetos añadimos un anuncio
                if ((i + 1) % 3 == 0) {
                    listaFinal.add("ANUNCIO");
                }
            }

            // Si es premium, solo mostramos objetos
        } else {
            listaFinal.addAll(listaObjetos);
        }

        // Notificamos al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();
    }

    // Activa el swipe para hacer LIKE a un objeto
    private void activarSwipe() {

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(
                        0, // no permitimos mover elementos
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // swipe en ambas direcciones
                ) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false; // no se usa
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();

                        // Si es un anuncio, cancelamos el swipe
                        if (!(listaFinal.get(position) instanceof Objeto)) {
                            adapter.notifyItemChanged(position);
                            return;
                        }

                        // Obtenemos el objeto deslizado
                        Objeto objeto = (Objeto) listaFinal.get(position);

                        // Si desliza a la derecha → LIKE
                        if (direction == ItemTouchHelper.RIGHT) {
                            enviarLike(objeto.getId());
                        }

                        // Eliminamos el objeto de la lista
                        listaFinal.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                };

        // Asociamos el swipe al RecyclerView
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvObjetos);
    }

    // Envía un like al servidor
    private void enviarLike(int idObjeto) {

        String url = "http://10.0.2.2/reutilizate/interaccion.php";

        // Petición POST
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {}, // no necesitamos procesar respuesta
                error -> {}     // tampoco error
        ) {
            @Override
            protected Map<String, String> getParams() {

                // Obtenemos el ID del usuario
                SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                int idUsuario = prefs.getInt("id_usuario", -1);

                // Parámetros del POST
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_objeto", String.valueOf(idObjeto));
                params.put("tipo", "LIKE");

                return params;
            }
        };

        // Añadimos la petición a la cola
        Volley.newRequestQueue(requireContext()).add(request);
    }
}
