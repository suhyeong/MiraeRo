package org.androidtown.miraero;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> implements Filterable {

    private Context context;
    private ArrayList<String> unFilteredlist;
    private ArrayList<String> filteredList;
    private RecyclerAdapter.OnItemClickListener onItemClickListener = null;

    public SearchAdapter(Context context, ArrayList<String> list) {
        super();
        this.context = context;
        this.unFilteredlist = list;
        this.filteredList = list;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, viewGroup, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder viewHolder, int i) {
        viewHolder.textView.setText(filteredList.get(i));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public long getItemID(int position) {
        //return filteredList.get(position).getId();
        return 0;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int pos);
    }

    public void setOnItemClickListener(RecyclerAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class SearchHolder extends ViewHolder implements View.OnClickListener {
        TextView textView;
        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.search_item_textview);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<String> filteringList = new ArrayList<>();
                    for(String name : unFilteredlist) {
                        if(name.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<String>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
