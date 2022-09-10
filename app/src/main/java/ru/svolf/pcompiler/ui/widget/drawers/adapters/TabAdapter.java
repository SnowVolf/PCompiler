package ru.svolf.pcompiler.ui.widget.drawers.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.tabs.TabFragment;
import ru.svolf.pcompiler.tabs.TabManager;

/**
 * Created by radiationx on 02.05.17.
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {
    private int color = Color.argb(48, 128, 128, 128);

    private TabAdapter.OnItemClickListener itemClickListener;
    private TabAdapter.OnItemClickListener closeClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setCloseClickListener(OnItemClickListener closeClickListener) {
        this.closeClickListener = closeClickListener;
    }

    public TabFragment getItem(int position){
        return TabManager.getInstance().get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_tab_item, parent, false);
        return new TabAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return TabManager.getInstance().getSize();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TabFragment fragment = TabManager.getInstance().get(position);
        if (position == TabManager.getActiveIndex()) {
            holder.itemView.setBackgroundColor(color);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.text.setText(fragment.getTabTitle());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text;
        public ImageView close;

        public ViewHolder(View v) {
            super(v);
            text = v.findViewById(R.id.drawer_item_title);
            close = v.findViewById(R.id.drawer_item_close);

            v.setOnClickListener(this);
            close.setOnClickListener(v1 -> {
                if (closeClickListener != null) {
                    closeClickListener.onItemClick(getItem(getLayoutPosition()), getLayoutPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getItem(getLayoutPosition()), getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TabFragment tabFragment, int position);
    }
}