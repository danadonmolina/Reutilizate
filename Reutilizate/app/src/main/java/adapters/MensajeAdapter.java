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

    private List<Mensaje> lista;
    private int miId; // ID del usuario actual

    public MensajeAdapter(List<Mensaje> lista, int miId) {
        this.lista = lista;
        this.miId = miId;
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje m = lista.get(position);
        return (m.getIdEmisor() == miId) ? 1 : 0;
    }

    @NonNull
    @Override
    public MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mensaje_derecha, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mensaje_izquierda, parent, false);
        }

        return new MensajeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeViewHolder holder, int position) {
        holder.tvMensaje.setText(lista.get(position).getContenido());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class MensajeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMensaje;

        public MensajeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
        }
    }
}
