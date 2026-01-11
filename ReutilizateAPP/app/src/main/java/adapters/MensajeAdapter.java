package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reutilizate.R;

import java.util.List;

import model.Mensaje;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder> {

    // Lista de mensajes a mostrar
    private List<Mensaje> lista;

    // ID del usuario actual (sirve para saber si el mensaje es enviado o recibido)
    private int miId;

    public MensajeAdapter(List<Mensaje> lista, int miId) {
        this.lista = lista;
        this.miId = miId;
    }

    // Determina qué tipo de vista usar según quién envió el mensaje
    // 1  mensaje enviado por mí
    // 0  mensaje recibido
    @Override
    public int getItemViewType(int position) {
        Mensaje m = lista.get(position);
        return (m.getIdEmisor() == miId) ? 1 : 0;
    }

    // Infla el layout correspondiente según el tipo de mensaje
    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        // Si el mensaje es mío = layout alineado a la derecha
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mensaje_derecha, parent, false);

            // Si el mensaje es del otro usuario = layout alineado a la izquierda
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mensaje_izquierda, parent, false);
        }

        return new MensajeViewHolder(v);
    }

    // Asigna el contenido del mensaje al TextView
    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        holder.tvMensaje.setText(lista.get(position).getContenido());
    }

    // Devuelve el número total de mensajes
    @Override
    public int getItemCount() {
        return lista.size();
    }

    // ViewHolder que contiene la referencia al TextView del mensaje
    static class MensajeViewHolder extends RecyclerView.ViewHolder {

        // TextView donde se muestra el contenido del mensaje
        TextView tvMensaje;

        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);

            // Enlazamos el TextView del layout
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
        }
    }
}
