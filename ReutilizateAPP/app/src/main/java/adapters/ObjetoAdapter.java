package adapters;

import android.content.Context;
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

import model.Objeto;

public class ObjetoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constantes para diferenciar el tipo de vista
    private static final int TIPO_OBJETO = 0;
    private static final int TIPO_ANUNCIO = 1;

    // Lista que contiene tanto objetos como anuncios
    private final List<Object> lista;

    // Contexto necesario para Glide y para inflar layouts
    private final Context context;

    public ObjetoAdapter(List<Object> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // Si el elemento es un Objeto =  tipo objeto
        // Si no, es un anuncio
        if (lista.get(position) instanceof Objeto) {
            return TIPO_OBJETO;
        } else {
            return TIPO_ANUNCIO;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        // Si es un objeto, inflamos el layout del objeto
        if (viewType == TIPO_OBJETO) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_objeto, parent, false);
            return new ObjetoViewHolder(v);

            // Si es un anuncio, inflamos el layout del anuncio
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_anuncio, parent, false);
            return new AnuncioViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {

        // Solo los objetos tienen datos que mostrar
        if (holder instanceof ObjetoViewHolder) {

            // Obtenemos el objeto de la lista
            Objeto obj = (Objeto) lista.get(position);

            // Cast del holder
            ObjetoViewHolder h = (ObjetoViewHolder) holder;

            // Asignamos nombre y descripción
            h.txtNombre.setText(obj.getNombre());
            h.txtDescripcion.setText(obj.getDescripcion());

            // Cargamos la imagen usando Glide
            Glide.with(context)
                    .load("http://10.0.2.2/reutilizate/" + obj.getImagen())
                    .placeholder(R.drawable.ic_launcher_background) // Imagen mientras carga
                    .error(R.drawable.ic_launcher_foreground)       // Imagen si falla
                    .into(h.imgObjeto);
        }
    }

    @Override
    public int getItemCount() {
        // Número total de elementos (objetos + anuncios)
        return lista.size();
    }

    // ViewHolder para los objetos
    static class ObjetoViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtDescripcion;
        ImageView imgObjeto;

        public ObjetoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Enlazamos los elementos del layout
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            imgObjeto = itemView.findViewById(R.id.imgObjeto);
        }
    }

    // ViewHolder para los anuncios cada 3 objetos
    static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAnuncio;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);

            // Enlazamos la imagen del anuncio
            imgAnuncio = itemView.findViewById(R.id.imgAnuncio2);
        }
    }
}
