package com.developer.chithlal.tracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppHolder> implements Filterable {
    private List<Apps> appsList;
    private List<Apps> appsListFiltered;
    Context context;
    public AppAdapter(List<Apps> appsList,Context context){
        this.appsList=appsList;
        appsListFiltered=appsList;
        this.context = context;
    }
    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_card,viewGroup,false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder appHolder, final int i) {
        final Apps app=appsListFiltered.get(i);
        appHolder.appName.setText(app.getName());
        appHolder.appIcon.setImageDrawable(app.getIcon());
        appHolder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,Permissions.class);
                it.putExtra("PACKAGE",app.getPackageName());
                context.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    appsListFiltered = appsList;
                }else {
                    List<Apps> filteredList = new ArrayList<>();
                    for(Apps app:appsList){
                        if(app.getName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(app);
                        }
                    }
                    appsListFiltered=filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = appsListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                appsListFiltered = (ArrayList<Apps>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AppHolder extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView appIcon;
        LinearLayout background;
        public AppHolder(@NonNull View itemView) {
            super(itemView);
            appName=itemView.findViewById(R.id.tv_app_name);
           appIcon=itemView.findViewById(R.id.iv_icon);
           background=itemView.findViewById(R.id.ll_background);
        }
    }
}
