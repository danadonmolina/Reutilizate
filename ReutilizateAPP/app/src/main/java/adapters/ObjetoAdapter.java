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

    private static final int TIPO_OBJETO = 0;
    private static final int TIPO_ANUNCIO = 1;

    private final List<Object> lista;   // La lista esta preparada para coger el anuncio y el objeto
    private final Context context;

    public ObjetoAdapter(List<Object> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
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

        if (viewType == TIPO_OBJETO) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_objeto, parent, false);
            return new ObjetoViewHolder(v);

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

        if (holder instanceof ObjetoViewHolder) {

            Objeto obj = (Objeto) lista.get(position);
            ObjetoViewHolder h = (ObjetoViewHolder) holder;

            h.txtNombre.setText(obj.getNombre());
            h.txtDescripcion.setText(obj.getDescripcion());

            Glide.with(context)
                    .load("http://10.0.2.2/reutilizate/" + obj.getImagen())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(h.imgObjeto);



        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // View holder del objeto
    static class ObjetoViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtDescripcion;
        ImageView imgObjeto;

        public ObjetoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            imgObjeto = itemView.findViewById(R.id.imgObjeto);
        }
    }


    // View holder del anuncio cada 3 objetos
    static class AnuncioViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAnuncio;

        public AnuncioViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnuncio = itemView.findViewById(R.id.imgAnuncio2);
        }
    }
}
