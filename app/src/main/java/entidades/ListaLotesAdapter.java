package entidades;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpiration.R;
import com.example.xpiration.VerLoteActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListaLotesAdapter extends RecyclerView.Adapter<ListaLotesAdapter.LotesViewHolder>  {
//********************** Declaracion y inicializacion de Lista de Lotes ************************
    ArrayList<Lotes> listaLotes;


    public ListaLotesAdapter(ArrayList<Lotes> listaLotes){
        this.listaLotes = listaLotes;
    }

    @NonNull
    @Override
    public LotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_lote, null,false);
        return new LotesViewHolder(view);
    }

//********************** Seteo de datos a lista de Lotes ***************************************
    @Override
    public void onBindViewHolder(@NonNull LotesViewHolder holder, int position) {


        Date now = new Date();
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        String vDateYMD = dateFormatYMD.format(now);

        Date nuevo=null;
        Date comparado=null;
//************************* Se obtiene la cantidad de dias para que expire *************************
        try {
            nuevo = dateFormatYMD.parse(vDateYMD);
            comparado = listaLotes.get(position).getLOTE_FECHA_CADUCIDAD();
        } catch (ParseException e) {
           e.printStackTrace();
        }

        int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);


        holder.viewNombre.setText(listaLotes.get(position).getLOTE_NOMBRE());
        holder.viewFecha.setText(diff+" dias para Caducar");
        holder.productoNombre.setText(listaLotes.get(position).getPRODUCTO_NOMBRE());


//* Al quedar x dias restantes para que expire se cambian los colores de las notificaciones de manera automatica *
        if(diff<=listaLotes.get(position).getLOTE_NOTIFICACION_NARANJA()){
            holder.estado.setImageResource(R.drawable.ic_yellow_circle);
            if(diff<=listaLotes.get(position).getLOTE_NOTIFICACION_ROJA()){
                holder.estado.setImageResource(R.drawable.ic_red_circle);
                if(diff<= 0){ // Si es el dia en el que expira el lote el circulo sera negro
                    holder.estado.setImageResource(R.drawable.ic_black_circle);
                    if(diff<= -3){ // Si pasaron 3 dias el lote se borrara de manera automatica
                        listaLotes.get(position).Borrar(listaLotes.get(position).getLOTE_ID());
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return listaLotes.size();
    }

//*********************** Manda un Lote a VerLoteActivity segun id *********************************
    public class LotesViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewFecha,productoNombre;
        ImageView estado;

        public LotesViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.LoteNombre);
            viewFecha = itemView.findViewById(R.id.LoteCaducidad);
            estado = itemView.findViewById(R.id.IconoEstadoLote);
            productoNombre = itemView.findViewById(R.id.LoteNombreProducto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    //Toast.makeText(context, "lote con id = " + listaLotes.get(getAdapterPosition()).getLOTE_ID(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, VerLoteActivity.class);
                    intent.putExtra("ID", listaLotes.get(getAdapterPosition()).getLOTE_ID());
                    context.startActivity(intent);
                }
            });

        }
    }

}
