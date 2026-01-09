package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.reutilizate.R;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PublicarFragment extends Fragment {

    EditText etNombre, etDescripcion;
    MaterialButton btnPublicar, btnSeleccionarImagen;
    ImageView imgPreview;

    Bitmap bitmapSeleccionado = null;

    private final String URL_INSERTAR =
            "http://10.0.2.2/reutilizate/insertarObjeto.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_publicar, container, false);

        etNombre = view.findViewById(R.id.etNombreObjeto);
        etDescripcion = view.findViewById(R.id.etDescripcionObjeto);
        btnPublicar = view.findViewById(R.id.btnPublicar);
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen);
        imgPreview = view.findViewById(R.id.imgPreview);

        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());
        btnPublicar.setOnClickListener(v -> publicarObjeto());

        return view;
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imgPreview.setImageURI(imageUri);

            try {
                bitmapSeleccionado = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(),
                        imageUri
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertirBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void publicarObjeto() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "Nombre obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (bitmapSeleccionado == null) {
            Toast.makeText(getContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagenBase64 = convertirBase64(bitmapSeleccionado);

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_INSERTAR,
                response -> {
                    if (response.trim().equals("OK")) {
                        Toast.makeText(getContext(), "Objeto publicado", Toast.LENGTH_SHORT).show();
                        etNombre.setText("");
                        etDescripcion.setText("");
                        imgPreview.setImageResource(0);
                        bitmapSeleccionado = null;
                    } else {
                        Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error conexión", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                int idUsuario = prefs.getInt("id_usuario", -1);

                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("descripcion", descripcion);
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("imagen", imagenBase64);
                return params;
            }

        };

        queue.add(request);
    }
}
