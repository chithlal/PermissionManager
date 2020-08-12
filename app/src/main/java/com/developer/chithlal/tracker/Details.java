package com.developer.chithlal.tracker;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

public class Details extends AppCompatActivity {
    private ArcProgress arcProgress;
    private TextView protectionLabel,desc_titile,desc,permission_state;
    private Button review;

    int color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent=getIntent();
        PermissionModel permissionModel=(PermissionModel)intent.getSerializableExtra("Permission");
        final String packgeName=intent.getStringExtra("package");
        arcProgress = findViewById(R.id.arc_progress);
        protectionLabel = findViewById(R.id.tv_protection_level);
        desc=findViewById(R.id.tv_description);
        review=findViewById(R.id.bt_rv_permission);
        permission_state=findViewById(R.id.permission_state);
        desc_titile=findViewById(R.id.tv_title_description);
        setArcProgress(arcProgress,permissionModel,protectionLabel);
        desc_titile.setText(permissionModel.getPermissionName());
        permission_state.setTextColor(color);
        review.setBackgroundColor(color);
        try {


            if (permissionModel.getDesc() != 0) {
                desc.setText(permissionModel.getDesc());
            } else {
                desc.setText("No Descriptions available");
            }
        }
        catch (Resources.NotFoundException e){
            desc.setText("No Descriptions available");
        }
        if(permissionModel.isStatus()){
            permission_state.setText("Granted");
        }
        else{
            permission_state.setText("Denied");
        }
        arcProgress.setFinishedStrokeColor(color);
        protectionLabel.setTextColor(color);
        desc_titile.setTextColor(color);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(packgeName);
            }
        });


    }
    void setArcProgress(ArcProgress arcProgress,PermissionModel permissionModel,TextView protectionLabel){

        if(permissionModel.getLevel()==0){
            arcProgress.setProgress(1);
            color=getResources().getColor(R.color.green);

            protectionLabel.setText("Normal");
           // protectionLabel.setTextColor(getResources().getColor(R.color.green));
        }
        else if(permissionModel.getLevel()==1){
            arcProgress.setProgress(4);
           // arcProgress.setFinishedStrokeColor(getResources().getColor(R.color.red));
            color=getResources().getColor(R.color.red);
            protectionLabel.setText("Dangerous");
           // protectionLabel.setTextColor(getResources().getColor(R.color.red));

        }
        else if(permissionModel.getLevel()==2||permissionModel.getLevel()==3){
            arcProgress.setProgress(2);
           // arcProgress.setFinishedStrokeColor(getResources().getColor(R.color.orrange));
            color=getResources().getColor(R.color.orrange);
            protectionLabel.setText("Signature");
           // protectionLabel.setTextColor(getResources().getColor(R.color.orrange));
        }
        else{
            arcProgress.setProgress(3);
           // arcProgress.setFinishedStrokeColor(getResources().getColor(R.color.blue));
            protectionLabel.setText("Moderate");
            color=getResources().getColor(R.color.blue);
           // protectionLabel.setTextColor(getResources().getColor(R.color.blue));
        }
    }
    public void navigate(String packageName){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        startActivity(intent);
    }
}
