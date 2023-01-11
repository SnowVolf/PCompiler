package ru.svolf.pcompiler.ui.widget.drawers.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import ru.svolf.pcompiler.R;
import ru.svolf.pcompiler.patch.PatchCollection;
import ru.svolf.pcompiler.tabs.TabFragment;
import ru.svolf.pcompiler.tabs.TabManager;

/**
 * Created by radiationx on 02.05.17.
 */

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

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

    @NonNull
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
            Drawable select = AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.tab_selected);
            select.setAlpha(100);
            holder.itemView.setBackground(select);
            holder.source.setText(PatchCollection.getCollection().getItem(TabManager.getActiveTag()));
        } else {
            holder.itemView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        holder.text.setText(fragment.getTabTitle());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text;
        public TextView source;
        public ImageButton close;

        public ViewHolder(View v) {
            super(v);
            text = v.findViewById(R.id.drawer_item_title);
            source = v.findViewById(R.id.drawer_item_source);
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