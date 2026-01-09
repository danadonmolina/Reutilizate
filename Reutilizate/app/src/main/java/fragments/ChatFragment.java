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

    private static final String ARG_YO = "yo";
    private static final String ARG_OTRO = "otro";
    private static final String ARG_NOMBRE = "nombre";

    int yo, otro;
    String nombreOtro;

    RecyclerView rv;
    EditText etMensaje;
    Button btnEnviar;

    List<Mensaje> lista;
    MensajeAdapter adapter;

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

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        yo = getArguments().getInt(ARG_YO);
        otro = getArguments().getInt(ARG_OTRO);
        nombreOtro = getArguments().getString(ARG_NOMBRE);

        rv = v.findViewById(R.id.rvMensajes);
        etMensaje = v.findViewById(R.id.etMensaje);
        btnEnviar = v.findViewById(R.id.btnEnviar);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        lista = new ArrayList<>();

        adapter = new MensajeAdapter(lista, yo);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        rv.post(() -> rv.scrollToPosition(lista.size() - 1));

        cargarMensajes();

        btnEnviar.setOnClickListener(view -> enviarMensaje());

        return v;
    }

    private void cargarMensajes() {
        String url = "http://10.0.2.2/reutilizate/obtenerMensajes.php?yo=" + yo + "&otro=" + otro;

        StringRequest req = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        JSONArray arr = new JSONArray(response);
                        lista.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);

                            Mensaje m = new Mensaje(
                                    0,
                                    o.getInt("id_emisor"),
                                    otro,
                                    o.getString("contenido"),
                                    o.getString("fecha")
                            );

                            lista.add(m);
                        }

                        // Se crea un mensaje automatrico de match solo si no se ha hablado antes
                        if (lista.isEmpty()) {
                            Mensaje matchMsg = new Mensaje(
                                    0,
                                    0, // emisor = sistema
                                    yo,
                                    "¡MATCH! Habéis hecho match 🎉",
                                    "" // fecha vacía o puedes poner la actual
                            );
                            lista.add(matchMsg);
                        }


                        adapter.notifyDataSetChanged();
                        rv.scrollToPosition(lista.size() - 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(getContext()).add(req);
    }


    private void enviarMensaje() {
        String contenido = etMensaje.getText().toString().trim();
        if (contenido.isEmpty()) return;

        String url = "http://10.0.2.2/reutilizate/enviarMensaje.php";

        StringRequest req = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    etMensaje.setText("");
                    cargarMensajes();
                },
                error -> error.printStackTrace()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("emisor", String.valueOf(yo));
                p.put("receptor", String.valueOf(otro));
                p.put("contenido", contenido);
                return p;
            }
        };

        Volley.newRequestQueue(getContext()).add(req);
    }
}
