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

    private RecyclerView rvObjetos;
    private ObjetoAdapter adapter;

    private List<Object> listaObjetos;

    private final String URL_OBJETOS = "http://10.0.2.2/reutilizate/obtenerMisObjetos.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_objetos, container, false);

        rvObjetos = view.findViewById(R.id.rvObjetos);
        rvObjetos.setLayoutManager(new LinearLayoutManager(getContext()));

        listaObjetos = new ArrayList<>();

        adapter = new ObjetoAdapter(listaObjetos, getContext());
        rvObjetos.setAdapter(adapter);

        cargarObjetos();

        return view;
    }

    private void cargarObjetos() {
        Context ctx = getContext();
        if (ctx == null) return;

        SharedPreferences prefs = ctx.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        int idUsuario = prefs.getInt("id_usuario", -1);

        RequestQueue queue = Volley.newRequestQueue(ctx);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_OBJETOS,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        listaObjetos.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            Objeto objeto = new Objeto(
                                    obj.getInt("id"),
                                    obj.getString("nombre"),
                                    obj.getString("descripcion"),
                                    null,
                                    obj.optString("imagen", "")
                            );

                            listaObjetos.add(objeto);
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(ctx, "Error al procesar datos", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(ctx, "Error al cargar objetos", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                return params;
            }
        };

        queue.add(request);
    }
}
