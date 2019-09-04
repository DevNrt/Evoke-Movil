package com.example.pruebaandroid.recycle.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pruebaandroid.R;
import com.example.pruebaandroid.fragmentos.ItemPqrs;

import java.util.List;

// Se pone dentro del adaptador el mismo nombre de la clase y dentro se crea la clase del view Holder o como le queramos llamar
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo, descrip;

        public ViewHolder(View itemView) {
            super(itemView);
            titulo  = (TextView) itemView.findViewById(R.id.textViewTitulo);
            descrip = (TextView) itemView.findViewById(R.id.textViewDesc);
        }
    }

    public List<ItemPqrs> listaPqrs;

    public RecycleViewAdapter(List<ItemPqrs> listaPqrs) {
        this.listaPqrs = listaPqrs;
    }

    // Infla el contenido dentro del recycle dentro del layout del fragment de PQRS
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pqrs,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Realiza las modificaciones del contenido
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titulo.setText(listaPqrs.get(position).getTitulo());
        holder.descrip.setText(listaPqrs.get(position).getDescrip());
    }

    @Override
    public int getItemCount() {
        return listaPqrs.size();
    }
}
