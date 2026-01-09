package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reutilizate.R;

import java.util.List;

import model.Match;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    public interface OnMatchClick {
        void onClick(Match m);
    }

    private List<Match> lista;
    private OnMatchClick listener;

    public MatchAdapter(List<Match> lista, OnMatchClick listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Match m = lista.get(position);

        holder.txtNombreObjeto.setText(m.getNombreObjeto());
        holder.txtUsuario.setText("Con: " + m.getNombreUsuario());

        String imagen = m.getImagenObjeto();

        if (imagen != null && !imagen.equals("null") && !imagen.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2/reutilizate/" + imagen)
                    .placeholder(R.mipmap.placeholder)
                    .into(holder.imgObjeto);
        } else {
            holder.imgObjeto.setImageResource(R.mipmap.placeholder);
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgObjeto;
        TextView txtNombreObjeto, txtUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgObjeto = itemView.findViewById(R.id.imgObjeto);
            txtNombreObjeto = itemView.findViewById(R.id.txtNombreObjeto);
            txtUsuario = itemView.findViewById(R.id.txtUsuario);
        }
    }
}
