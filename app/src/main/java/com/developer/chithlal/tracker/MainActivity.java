package com.developer.chithlal.tracker;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.SearchManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.Object;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.chithlal.tracker.Services.AnalyzerJob;
import com.developer.chithlal.tracker.Services.AppService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static android.support.v4.app.AppOpsManagerCompat.MODE_ALLOWED;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private SearchView searchView;
    AppAdapter appAdapter;

    List<Apps> apps=new ArrayList<>();
    private boolean system=false;
    SwipeRefreshLayout refreshLayout;
    Context context;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshLayout=findViewById(R.id.app_refresh);

        getSupportActionBar().setTitle("Apps");
        recyclerView=findViewById(R.id.rv_app_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       final backgroundProcess b= new backgroundProcess();
       b.execute("");
        context=this;

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.green),getResources().getColor(R.color.red));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                appAdapter = new AppAdapter(getCurentList(system),context);
                recyclerView.setAdapter(appAdapter);
                refreshLayout.setRefreshing(false);
            }
        });


    }
    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        Process process;
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }
    private List<Apps> getCurentList(boolean system){
        PackageManager packageManager = this.getPackageManager();
        List<Apps> appList=new ArrayList<>();
        Drawable icon=null;
        ApplicationInfo applicationInfo = null;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        List<ApplicationInfo> installedApps=packageManager.getInstalledApplications(0);
        for(ApplicationInfo info:installedApps){
            try {
                applicationInfo = packageManager.getApplicationInfo(info.packageName, 0);
                 icon=packageManager.getApplicationIcon(info.packageName);

            } catch (final PackageManager.NameNotFoundException e) {}
            final String title = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "No Name");
            if (system)
            appList.add(new Apps(title,icon,info.packageName));
            else {
                if (info.sourceDir.startsWith("/data/app/")){
                    appList.add(new Apps(title,icon,info.packageName));
                }
            }
        }
        Collections.sort(appList,Apps.AppNameComparator);

        return appList;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                appAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                appAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {
            return true;
        }
        if (id==R.id.service){
            refreshLayout.setRefreshing(true);
          if (system) {
              system = false;
              item.setTitle("Show System Apps");

          }
          else{
              system=true;
              item.setTitle("Hide System Apps");
          }


              appAdapter = new AppAdapter(getCurentList(system),context);
              recyclerView.setAdapter(appAdapter);
            refreshLayout.setRefreshing(false);

        }

        return super.onOptionsItemSelected(item);
    }

    class backgroundProcess extends AsyncTask<String,String,List<Apps>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(List<Apps> appsList) {
            super.onPostExecute(appsList);
            apps=appsList;
            appAdapter = new AppAdapter(appsList,context);
            recyclerView.setAdapter(appAdapter);
           // appAdapter.notifyDataSetChanged();
          //  createAnalyzerJob();
            refreshLayout.setRefreshing(false);
        }

        @Override
        protected List<Apps> doInBackground(String... strings) {
            apps=getCurentList(system);
            return apps;
        }
    }
}
