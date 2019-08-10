package com.climate.fight.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.climate.fight.MoreInfoActivity;
import com.climate.fight.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.climate.fight.recycler.ItemHome.*;

/**
 * Creadito con cariño por alexb el día 23/06/2019.
 */

public class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemHome> items;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");

    public MultiAdapter(List<ItemHome> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public class ViewHolderMani extends RecyclerView.ViewHolder {
        TextView tit, lug, start;

        ViewHolderMani(View itemView) {
            super(itemView);
            tit = itemView.findViewById(R.id.manitit);
            start = itemView.findViewById(R.id.manitime);
            lug = itemView.findViewById(R.id.maniplace);
        }
    }

    public class ViewHolderBati extends RecyclerView.ViewHolder {

        TextView tit, lug, start;
        ViewHolderBati(View itemView) {
            super(itemView);
            tit = itemView.findViewById(R.id.batitit);
            start = itemView.findViewById(R.id.batitime);
            lug = itemView.findViewById(R.id.batiplace);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ItemHome item = items.get(position);
        return item.getTipo();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case TIPO_MANIF:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manifestacion, viewGroup, false);
                return new ViewHolderMani(v);
            case TIPO_BATIDA:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_batida, viewGroup, false);
                return new ViewHolderBati(v);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ItemHome item = items.get(position);
        Date date = new Date(item.getMillisStart());
        String nombre = item.getNombre(), lugar = item.getRefUbi();
        holder.itemView.setOnClickListener(view -> {
            Intent eventoIntent = new Intent(context, MoreInfoActivity.class);
            Gson gson = new Gson();
            String json = gson.toJson(item);
            eventoIntent.putExtra("objeto", json);
            context.startActivity(eventoIntent);
        });
        switch (holder.getItemViewType()) {
            case TIPO_MANIF:
                ViewHolderMani vhMani = (ViewHolderMani) holder;
                vhMani.tit.setText(nombre);
                vhMani.start.setText("Comienza: " + sdf.format(date));
                vhMani.lug.setText("En " + lugar);
                break;
            case TIPO_BATIDA:
                ViewHolderBati vhBati = (ViewHolderBati) holder;
                vhBati.tit.setText(nombre);
                vhBati.start.setText("Comienza: " + sdf.format(date));
                vhBati.lug.setText("En " + lugar);
                break;
        }
    }

}
