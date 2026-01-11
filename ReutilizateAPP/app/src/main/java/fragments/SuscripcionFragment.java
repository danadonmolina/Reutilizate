package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reutilizate.R;

public class SuscripcionFragment extends Fragment {

    // Botón para activar o cancelar la suscripción
    private Button btnGestionar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragment
        View v = inflater.inflate(R.layout.fragment_suscripcion, container, false);

        // Enlazamos el botón del layout
        btnGestionar = v.findViewById(R.id.btnGestionarSuscripcion);

        // Obtenemos las preferencias del usuario
        SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        // Comprobamos si el usuario es premium
        boolean esPremium = prefs.getBoolean("premium", false);

        // Actualizamos el texto del botón según el estado actual
        if (esPremium) {
            btnGestionar.setText("Cancelar suscripción");
        } else {
            btnGestionar.setText("Activar suscripción");
        }

        // Lógica del botón para activar o cancelar la suscripción
        btnGestionar.setOnClickListener(view -> {

            // Obtenemos el estado actual
            boolean premiumActual = prefs.getBoolean("premium", false);

            // Si no es premium = activar
            if (!premiumActual) {
                prefs.edit().putBoolean("premium", true).apply();
                btnGestionar.setText("Cancelar suscripción");

                // Si ya es premium = cancelar
            } else {
                prefs.edit().putBoolean("premium", false).apply();
                btnGestionar.setText("Activar suscripción");
            }
        });

        return v;
    }
}
