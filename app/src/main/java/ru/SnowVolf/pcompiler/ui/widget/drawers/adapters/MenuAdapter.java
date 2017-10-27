package ru.SnowVolf.pcompiler.ui.widget.drawers.adapters;

import android.graphics.Color;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.SnowVolf.pcompiler.R;
import ru.SnowVolf.pcompiler.ui.widget.drawers.MenuItems;

/**
 * Created by radiationx on 02.05.17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    List<MenuItems.MenuItem> items;
    private int color = Color.argb(48, 128, 128, 128);

    public MenuAdapter(List<MenuItems.MenuItem> items) {
        this.items = items;
    }

    private MenuAdapter.OnItemClickListener itemClickListener;

    public void setItemClickListener(MenuAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuItems.MenuItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_menu_item, parent, false);
        return new MenuAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {
        MenuItems.MenuItem item = getItem(position);
        assert item != null;

        if (item.isActive()) {
            holder.itemView.setBackgroundColor(color);
            //holder.text.setTextColor(App.getColorFromAttr(holder.itemView.getContext(), R.attr.colorAccent));
            //holder.icon.setColorFilter(App.getColorFromAttr(holder.itemView.getContext(), R.attr.colorAccent));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            //holder.icon.clearColorFilter();
        }

        holder.icon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), item.getIconRes()));
        holder.text.setText(item.getTitle());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text;
        public ImageView icon;

        public ViewHolder(View v) {
            super(v);
            text = v.findViewById(R.id.drawer_item_title);
            icon = v.findViewById(R.id.drawer_item_icon);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getItem(getLayoutPosition()), getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MenuItems.MenuItem menuItem, int position);
    }
}