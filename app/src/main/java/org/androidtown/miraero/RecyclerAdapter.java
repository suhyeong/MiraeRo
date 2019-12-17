package org.androidtown.miraero;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements Comparator<Item> {

    //adapter에 들어갈 list
    private ArrayList<Item> Itemlist = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    @NonNull
    @Override
    public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        //item을 보여주는 함수
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Itemlist.sort(this);
        }
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

    public long getItemID(int position) {
        return Itemlist.get(position).getId();
    }

    //리스트 정렬 기준 설정
    @Override
    public int compare(Item o1, Item o2) {
        if(o1.getSellcount() >= o2.getSellcount())
            return -1;
        else
            return 0;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //subView setting
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView ItemName;
        private TextView ItemContent;
        private ImageView ItemImage;
        private ImageView ItemNorS;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.item_name);
            ItemContent = itemView.findViewById(R.id.item_content);
            ItemImage = itemView.findViewById(R.id.item_image);
            ItemNorS = itemView.findViewById(R.id.item_n_or_s);
            itemView.setOnClickListener(this);
        }

        void onBind(Item item) {
            ItemName.setText(item.getName());
            ItemContent.setText(item.getContent());
            ItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ItemImage.setImageBitmap(item.getBitmap());
            SettingNorSImage(item.getNorth_or_South());
        }

        public void SettingNorSImage(String c) {
            if(c.equals("N"))
                ItemNorS.setImageResource(R.drawable.ic_north_24dp);
            else if(c.equals("S"))
                ItemNorS.setImageResource(R.drawable.ic_south_24dp);
            else if(c.equals("B"))
                ItemNorS.setImageResource(R.drawable.ic_northandsouth_24dp);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                if(onItemClickListener != null) {
                    onItemClickListener.OnItemClick(v, pos);
                }
            }
        }
    }
}
