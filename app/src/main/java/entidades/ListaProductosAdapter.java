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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListaProductosAdapter extends RecyclerView.Adapter<ListaProductosAdapter.ProductoViewHolder> implements Filterable {
//********************** Declaracion y inicializacion de Lista de productos ************************
    ArrayList<Productos> listaProductos;
    ArrayList<Productos> listaProductosFull;


    public ListaProductosAdapter(ArrayList<Productos> listaProductos){
        this.listaProductos = listaProductos;
        listaProductosFull = new ArrayList<>(listaProductos);

    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_producto, null,false);
        return new ProductoViewHolder(view);
    }

//********************** Seteo de datos a lista de productos ***************************************
    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {


        //Date now = new Date();
        //DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        //String vDateYMD = dateFormatYMD.format(now);

        //Date nuevo=null;
        //Date comparado=null;

        //try {
        //    nuevo = dateFormatYMD.parse(vDateYMD);
        //    comparado = listaProductos.get(position).loteMasViejo().;
        //} catch (ParseException e) {
        //   e.printStackTrace();
        //}

       // int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);


        holder.viewNombre.setText(listaProductos.get(position).getPRODUCTO_NOMBRE());
       // holder.viewFecha.setText(diff+" dias para Caducar");
       // if(diff<=listaProductos.get(position).loteMasViejo().getLOTE_NOTIFICACION_NARANJA()){
       //     holder.estado.setImageResource(R.drawable.ic_yellow_circle);
       //     if(diff<=listaProductos.get(position).loteMasViejo().getLOTE_NOTIFICACION_ROJA()){
       //         holder.estado.setImageResource(R.drawable.ic_red_circle);
       //     }
       // }



    }

    @Override
    public int getItemCount() {
        return listaProductos.size();

    }

//******************** Manda un producto a VerActivity segun id *****************************
    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewFecha;
        ImageView estado;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.ProductoNombre);
            viewFecha = itemView.findViewById(R.id.Caducidad);
           // estado = itemView.findViewById(R.id.IconoEstado);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SubmenuActivity.class);
                    intent.putExtra("ID",listaProductos.get(getAdapterPosition()).getPRODUCTO_ID());
                    context.startActivity(intent);
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
