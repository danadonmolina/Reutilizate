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

    private Button btnGestionar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_suscripcion, container, false);

        btnGestionar = v.findViewById(R.id.btnGestionarSuscripcion);

        SharedPreferences prefs = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        boolean esPremium = prefs.getBoolean("premium", false);

        //  IMPORTANTE: actualizar el texto al entrar al fragmento
        if (esPremium) {
            btnGestionar.setText("Cancelar suscripción");
        } else {
            btnGestionar.setText("Activar suscripción");
        }

        // Lógica del botón
        btnGestionar.setOnClickListener(view -> {
            boolean premiumActual = prefs.getBoolean("premium", false);

            if (!premiumActual) {
                prefs.edit().putBoolean("premium", true).apply();
                btnGestionar.setText("Cancelar suscripción");
            } else {
                prefs.edit().putBoolean("premium", false).apply();
                btnGestionar.setText("Activar suscripción");
            }
        });

        return v;
    }
}
