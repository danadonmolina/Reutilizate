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


 // Adaptador encargado de mostrar la lista de matches en un RecyclerView.
 // Cada elemento representa un objeto con el que se ha producido un match
  // y el usuario con el que se ha generado.

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {


     // Interfaz para gestionar el clic sobre un elemento de la lista.
      // Se utiliza para abrir el chat o la pantalla correspondiente.

    public interface OnMatchClick {
        void onClick(Match m);
    }

    private List<Match> lista;          // Lista de matches a mostrar
    private OnMatchClick listener;      // Listener para manejar clics

    public MatchAdapter(List<Match> lista, OnMatchClick listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflamos el layout de cada elemento de la lista
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_match, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Obtenemos el match correspondiente a la posición
        Match m = lista.get(position);

        // Mostramos el nombre del objeto y del usuario con el que se ha hecho match
        holder.txtNombreObjeto.setText(m.getNombreObjeto());
        holder.txtUsuario.setText("Con: " + m.getNombreUsuario());

        // Cargamos la imagen del objeto usando Glide
        String imagen = m.getImagenObjeto();

        if (imagen != null && !imagen.equals("null") && !imagen.isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2/reutilizate/" + imagen)
                    .placeholder(R.mipmap.placeholder)  // Imagen por defecto mientras carga
                    .into(holder.imgObjeto);

        } else {
            // Si no hay imagen válida, mostramos un placeholder
            holder.imgObjeto.setImageResource(R.mipmap.placeholder);
        }

        // Evento de clic sobre el elemento completo
        holder.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


     // ViewHolder que contiene las referencias a los elementos del layou para cada item del RecyclerView.

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
