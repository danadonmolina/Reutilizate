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

    RecyclerView recyclerView;
    List<Match> lista;
    MatchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = v.findViewById(R.id.recyclerMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        lista = new ArrayList<>();
        adapter = new MatchAdapter(lista, match -> abrirChat(match));
        recyclerView.setAdapter(adapter);

        cargarMatches();

        return v;
    }

    private void cargarMatches() {
        String url = "http://10.0.2.2/reutilizate/obtenerMatches.php?id_usuario=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        lista.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);

                            Match m = new Match(
                                    o.getString("nombreObjeto"),
                                    o.getString("imagenObjeto"),
                                    o.getString("nombreUsuario"),
                                    o.getInt("idUsuarioOtro")
                            );

                            lista.add(m);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void abrirChat(Match m) {
        ChatFragment chat = ChatFragment.newInstance(
                1, // id del usuario logueado
                m.getIdUsuarioOtro(),
                m.getNombreUsuario()
        );

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, chat)
                .addToBackStack(null)
                .commit();
    }
}
