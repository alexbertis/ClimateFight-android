package com.brontapps.climatefight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Creadito con cariño por alexb el día 23/06/2019.
 */

public class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> stops;
    private Context context;

    public MultiAdapter(List<String> stops, Context context) {
        this.stops = stops;
        this.context = context;
    }

    public class ViewHolderMani extends RecyclerView.ViewHolder {
        TextView stopDir, stopTime;

        ViewHolderMani(View itemView) {
            super(itemView);
            //stopDir = itemView.findViewById(R.id.vuelta_dir);
            //stopTime = itemView.findViewById(R.id.vuelta_tiempo);
        }
    }

    public class ViewHolderBati extends RecyclerView.ViewHolder {

        ViewHolderBati(View itemView) {
            super(itemView);

        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        switch (i) {
            case 0:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_manifestacion, viewGroup, false);
                return new ViewHolderMani(v);
            case 2:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_batida, viewGroup, false);
                return new ViewHolderBati(v);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderMani viewHolderMani = (ViewHolderMani) holder;

                break;

            case 2:
                ViewHolderBati viewHolderBati = (ViewHolderBati)holder;

                break;
        }
    }

}
