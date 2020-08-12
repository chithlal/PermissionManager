package com.developer.chithlal.tracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.Permission;
import java.security.Permissions;
import java.util.List;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.Holder> {
    List<PermissionModel> permissionModels;
    private Context context;
    private String packageName;


    public PermissionAdapter(List<PermissionModel> list, Context context,String packageName){
       permissionModels=list;

        this.context = context;

        this.packageName = packageName;
    }
    @NonNull
    @Override
    public PermissionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_permission,viewGroup,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionAdapter.Holder holder, int i) {
       final PermissionModel permissionModel=permissionModels.get(i);
       holder.permissionName.setText(permissionModel.getPermissionName());
       holder.permissionIcon.setImageResource(permissionModel.getPermissioinIcon());
       if(permissionModel.getLevel()==1){
           holder.details.setColorFilter(context.getResources().getColor(R.color.red));
            holder.underline.setBackgroundColor(context.getResources().getColor(R.color.red));

       }
       else if(permissionModel.getLevel()==0){
           holder.details.setColorFilter(context.getResources().getColor(R.color.green));
           holder.underline.setBackgroundColor(context.getResources().getColor(R.color.green));

       }
       else  if(permissionModel.getLevel()==2 ||permissionModel.getLevel()==3){
           holder.details.setColorFilter(context.getResources().getColor( R.color.orrange));
           holder.underline.setBackgroundColor(context.getResources().getColor(R.color.orrange));

       }
       else {
           holder.details.setColorFilter(context.getResources().getColor(R.color.blue));
           holder.underline.setBackgroundColor(context.getResources().getColor(R.color.blue));

       }
       holder.details.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context,Details.class);
               intent.putExtra("Permission",permissionModel);
               intent.putExtra("package",packageName);

               context.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return permissionModels.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView permissionName,underline;
        ImageView permissionIcon,details;
        LinearLayout background;
        public Holder(@NonNull View itemView) {
            super(itemView);
            permissionName=itemView.findViewById(R.id.tv_app_name);
            permissionIcon=itemView.findViewById(R.id.iv_icon);
            background=itemView.findViewById(R.id.ll_background);
            underline=itemView.findViewById(R.id.tv_underline);
            details=itemView.findViewById(R.id.bt_detailed);
        }
    }
}
