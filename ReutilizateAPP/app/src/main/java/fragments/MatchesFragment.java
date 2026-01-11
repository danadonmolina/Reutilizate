package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reutilizate.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.MatchAdapter;
import model.Match;

public class MatchesFragment extends Fragment {

    // RecyclerView donde se mostrarán los matches
    RecyclerView recyclerView;

    // Lista de matches obtenidos del servidor
    List<Match> lista;

    // Adaptador encargado de mostrar cada match
    MatchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View v = inflater.inflate(R.layout.fragment_matches, container, false);

        // Enlazamos el RecyclerView
        recyclerView = v.findViewById(R.id.recyclerMatches);

        // Configuramos el layout vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializamos la lista de matches
        lista = new ArrayList<>();

        // Creamos el adaptador y definimos qué pasa al pulsar un match
        adapter = new MatchAdapter(lista, match -> abrirChat(match));

        // Asignamos el adaptador al RecyclerView
        recyclerView.setAdapter(adapter);

        // Cargamos los matches desde el servidor
        cargarMatches();

        return v;
    }

    // Método para obtener los matches del servidor
    private void cargarMatches() {

        // URL del script PHP
        String url = "http://10.0.2.2/reutilizate/obtenerMatches.php?id_usuario=1";

        // Petición get al servidor
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        // Convertimos la respuesta en un array JSON
                        JSONArray array = new JSONArray(response);

                        // Limpiamos la lista antes de rellenarla
                        lista.clear();

                        // Recorremos cada elemento del JSON
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);

                            // Creamos un objeto Match con los datos recibidos
                            Match m = new Match(
                                    o.getString("nombreObjeto"),
                                    o.getString("imagenObjeto"),
                                    o.getString("nombreUsuario"),
                                    o.getInt("idUsuarioOtro")
                            );

                            // Añadimos el match a la lista
                            lista.add(m);
                        }

                        // Notificamos al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        // Añadimos la petición a la cola de Volley
        Volley.newRequestQueue(getContext()).add(request);
    }

    // Método para abrir el chat con el usuario del match seleccionado
    private void abrirChat(Match m) {

        // Creamos el fragment del chat con los datos necesarios
        ChatFragment chat = ChatFragment.newInstance(
                1, // ID del usuario logueado (debería venir de SharedPreferences)
                m.getIdUsuarioOtro(),
                m.getNombreUsuario()
        );

        // Reemplazamos el fragment actual por el chat
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, chat)
                .addToBackStack(null) // esto te permite volver atrás
                .commit();
    }
}
