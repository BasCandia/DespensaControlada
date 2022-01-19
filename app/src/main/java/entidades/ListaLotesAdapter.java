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
    //********************** Declaracion y inicializacion de Lista de productos ************************
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

    //********************** Seteo de datos a lista de productos ***************************************
    @Override
    public void onBindViewHolder(@NonNull LotesViewHolder holder, int position) {


        Date now = new Date();
        DateFormat dateFormatYMD = new SimpleDateFormat("yyyy/MM/dd");
        String vDateYMD = dateFormatYMD.format(now);

        Date nuevo=null;
        Date comparado=null;

        try {
            nuevo = dateFormatYMD.parse(vDateYMD);
            comparado = listaLotes.get(position).getLOTE_FECHA_CADUCIDAD();
        } catch (ParseException e) {
           e.printStackTrace();
        }

        int diff = (int)((comparado.getTime() - nuevo.getTime())/86400000);


        holder.viewNombre.setText(listaLotes.get(position).getLOTE_NOMBRE());
        holder.viewFecha.setText(diff+" dias para Caducar");
        if(diff<=listaLotes.get(position).getLOTE_NOTIFICACION_NARANJA()){
            holder.estado.setImageResource(R.drawable.ic_yellow_circle);
            if(diff<=listaLotes.get(position).getLOTE_NOTIFICACION_ROJA()){
                holder.estado.setImageResource(R.drawable.ic_red_circle);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listaLotes.size();
    }

    //******************** Manda un producto a VerActivity segun id *****************************
    public class LotesViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre, viewFecha;
        ImageView estado;

        public LotesViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre = itemView.findViewById(R.id.LoteNombre);
            viewFecha = itemView.findViewById(R.id.LoteCaducidad);
            estado = itemView.findViewById(R.id.IconoEstadoLote);

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
