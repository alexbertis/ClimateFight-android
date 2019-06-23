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

    public class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView stopDir, stopTime;

        ViewHolder0(View itemView) {
            super(itemView);
            //stopDir = itemView.findViewById(R.id.vuelta_dir);
            //stopTime = itemView.findViewById(R.id.vuelta_tiempo);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        ViewHolder2(View itemView) {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            //case 0: return new ViewHolder0();
            //case 2: return new ViewHolder2();

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;

                break;

            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;

                break;
        }
    }

}
