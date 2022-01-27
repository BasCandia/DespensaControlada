package entidades;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpiration.R;
import com.example.xpiration.SubmenuActivity;

import java.util.ArrayList;

public class ListaProductosAdapter extends RecyclerView.Adapter<ListaProductosAdapter.ProductoViewHolder> implements Filterable {
//********************** Declaracion y inicializacion de Lista de productos ************************
    ArrayList<Productos> listaProductos;
    ArrayList<Productos> listaProductosFull;
    private OnItemClickListener mListener;



    public ListaProductosAdapter(ArrayList<Productos> listaProductos){
        this.listaProductos = listaProductos;
        listaProductosFull = new ArrayList<>(listaProductos);

    }

    public void updateArray(ArrayList<Productos> lista){
        this.listaProductos = lista;
        listaProductosFull = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_producto, null,false);
        return new ProductoViewHolder(view, mListener);
    }


//********************** Seteo de datos a lista de productos ***************************************
    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {

        holder.viewNombre.setText(listaProductos.get(position).getPRODUCTO_NOMBRE());
        holder.viewCantidadLotes.setText("Cantidad de lotes: "+listaProductos.get(position).getCantidadLotes());

        //Agregar iconos a los productos por categoria
        int categoria = listaProductos.get(position).getCATEGORIA_ID();
        switch(categoria){
            case 1:
                holder.icon.setImageResource(R.drawable.ic_milk);
                break;
            case 2:
                holder.icon.setImageResource(R.drawable.ic_apple_alt);
                break;
            case 3:
                holder.icon.setImageResource(R.drawable.ic_meat);
                break;
            case 4:
                holder.icon.setImageResource(R.drawable.ic_grain);
                break;
            case 5:
                holder.icon.setImageResource(R.drawable.ic_bread);
                break;
            case 6:
                holder.icon.setImageResource(R.drawable.ic_legume);
                break;
            case 7:
                holder.icon.setImageResource(R.drawable.ic_oil);
                break;
            case 8:
                holder.icon.setImageResource(R.drawable.ic_soda);
                break;
            case 9:
                holder.icon.setImageResource(R.drawable.ic_dots);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();

    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

//******************** Manda un producto a VerActivity segun id *****************************
    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewCantidadLotes;
        ImageView estado;
        ImageView borrar;
        ImageView icon;


        public ProductoViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.ProductoNombre);
            viewCantidadLotes = itemView.findViewById(R.id.CantidadLote);
           // estado = itemView.findViewById(R.id.IconoEstado);
            borrar = itemView.findViewById(R.id.image_delete);
            icon = itemView.findViewById(R.id.ImagenProducto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SubmenuActivity.class);
                    intent.putExtra("ID",listaProductos.get(getAdapterPosition()).getPRODUCTO_ID());
                    context.startActivity(intent);
                }
            });

            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }



    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Productos> listaFiltrada = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                listaFiltrada.addAll(listaProductosFull);
            }else{
                String patronFiltro = charSequence.toString().toLowerCase().trim();

                for(Productos prod : listaProductosFull) {
                    if(prod.getPRODUCTO_NOMBRE().toLowerCase().contains(patronFiltro)){
                       listaFiltrada.add(prod);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltrada;

            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listaProductos.clear();
            listaProductos.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
