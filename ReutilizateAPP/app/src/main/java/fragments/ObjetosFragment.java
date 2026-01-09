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

    private RecyclerView rvObjetos;
    private ObjetoAdapter adapter;

    private List<Objeto> listaObjetos;
    private List<Object> listaFinal;

    private final String URL_OBJETOS = "http://10.0.2.2/reutilizate/obtenerObjetos.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_objetos, container, false);

        rvObjetos = view.findViewById(R.id.rvObjetos);
        rvObjetos.setLayoutManager(new LinearLayoutManager(getContext()));

        listaObjetos = new ArrayList<>();
        listaFinal = new ArrayList<>();

        adapter = new ObjetoAdapter(listaFinal, getContext());
        rvObjetos.setAdapter(adapter);

        cargarObjetos();
        activarSwipe();

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

                            // OJO: aquí NO tocamos la URL, porque tu adapter ya la construye
                            Objeto objeto = new Objeto(
                                    obj.getInt("id"),
                                    obj.getString("nombre"),
                                    obj.getString("descripcion"),
                                    null,
                                    obj.optString("imagen", "")
                            );

                            listaObjetos.add(objeto);
                        }

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
                SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                int idUsuario = prefs.getInt("id_usuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                return params;
            }
        };

        queue.add(request);
    }

    private void generarListaFinal() {

        SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        boolean esPremium = prefs.getBoolean("premium", false);

        listaFinal.clear();

        if (!esPremium) {
            for (int i = 0; i < listaObjetos.size(); i++) {
                listaFinal.add(listaObjetos.get(i));

                if ((i + 1) % 3 == 0) {
                    listaFinal.add("ANUNCIO");
                }
            }
        } else {
            listaFinal.addAll(listaObjetos);
        }

        adapter.notifyDataSetChanged();
    }

    private void activarSwipe() {

        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                ) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();

                        // Si es un anuncio no hacemos swipe
                        if (!(listaFinal.get(position) instanceof Objeto)) {
                            adapter.notifyItemChanged(position);
                            return;
                        }

                        Objeto objeto = (Objeto) listaFinal.get(position);

                        if (direction == ItemTouchHelper.RIGHT) {
                            enviarLike(objeto.getId());
                        }

                        listaFinal.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvObjetos);
    }

    private void enviarLike(int idObjeto) {

        String url = "http://10.0.2.2/reutilizate/interaccion.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {},
                error -> {}
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                int idUsuario = prefs.getInt("id_usuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("id_objeto", String.valueOf(idObjeto));
                params.put("tipo", "LIKE");
                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
