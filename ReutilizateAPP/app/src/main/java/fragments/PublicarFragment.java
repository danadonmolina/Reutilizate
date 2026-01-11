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

    // Campos del formulario
    EditText etNombre, etDescripcion;
    MaterialButton btnPublicar, btnSeleccionarImagen;
    ImageView imgPreview;

    // Imagen seleccionada por el usuario
    Bitmap bitmapSeleccionado = null;

    // URL del script PHP que inserta el objeto
    private final String URL_INSERTAR =
            "http://10.0.2.2/reutilizate/insertarObjeto.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View view = inflater.inflate(R.layout.fragment_publicar, container, false);

        // Enlazamos los elementos del layout
        etNombre = view.findViewById(R.id.etNombreObjeto);
        etDescripcion = view.findViewById(R.id.etDescripcionObjeto);
        btnPublicar = view.findViewById(R.id.btnPublicar);
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen);
        imgPreview = view.findViewById(R.id.imgPreview);

        // Botón para abrir la galería
        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());

        // Botón para publicar el objeto
        btnPublicar.setOnClickListener(v -> publicarObjeto());

        return view;
    }

    // Abre la galería para seleccionar una imagen
    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    // Recibe la imagen seleccionada por el usuario
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Comprobamos que la imagen se ha seleccionado correctamente
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            // Obtenemos la URI de la imagen
            Uri imageUri = data.getData();

            // Mostramos la imagen en el preview
            imgPreview.setImageURI(imageUri);

            try {
                // Convertimos la URI en un Bitmap
                bitmapSeleccionado = MediaStore.Images.Media.getBitmap(
                        requireActivity().getContentResolver(),
                        imageUri
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Convierte un Bitmap a Base64 para enviarlo al servidor
    private String convertirBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos); // calidad 80%
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // Envía los datos del objeto al servidor
    private void publicarObjeto() {

        // Obtenemos los valores del formulario
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        // Validación del nombre
        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), "Nombre obligatorio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de la imagen
        if (bitmapSeleccionado == null) {
            Toast.makeText(getContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertimos la imagen a Base64
        String imagenBase64 = convertirBase64(bitmapSeleccionado);

        // Cola de peticiones de Volley
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Petición POST al servidor
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_INSERTAR,
                response -> {

                    // Si el servidor responde OK, limpiamos el formulario
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

                // Obtenemos el ID del usuario desde SharedPreferences
                SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                int idUsuario = prefs.getInt("id_usuario", -1);

                // Parámetros que enviamos al servidor
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("descripcion", descripcion);
                params.put("id_usuario", String.valueOf(idUsuario));
                params.put("imagen", imagenBase64);

                return params;
            }
        };

        // Añadimos la petición a la cola
        queue.add(request);
    }
}
