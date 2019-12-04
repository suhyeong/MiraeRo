package org.androidtown.miraero;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    //adapter에 들어갈 list
    private ArrayList<Item> Itemlist = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder itemViewHolder, int i) {
        //item을 보여주는 함수
        itemViewHolder.onBind(Itemlist.get(i));
    }

    @Override
    public int getItemCount() {
        return Itemlist.size();
    }

    void addItem(Item item) {
        //리스트에 item추가
        Itemlist.add(item);
    }

    //subView setting
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView ItemName;
        private TextView ItemContent;
        private ImageView ItemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.item_name);
            ItemContent = itemView.findViewById(R.id.item_content);
            ItemImage = itemView.findViewById(R.id.item_image);
        }

        void onBind(Item item) {
            ItemName.setText(item.getName());
            ItemContent.setText(item.getContent());
            //ItemImage.setImageBitmap(item.getBitmap());
            ItemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ItemImage.setBackgroundResource(item.getId());
        }
    }


}
