package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    List<String> infoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.mrecyclerView);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        infoList = getAppProcessName(this);
        adapter = new InfoAdapter(infoList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }
    InfoAdapter adapter;

    List<String> getAppProcessName(Context context) {
        List<String> data = new ArrayList<>();
        final PackageManager packageManager = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL);
        for (int i = 0; i <apps.size() ; i++) {
            String name = apps.get(i).activityInfo.packageName;
            Log.i("TAG", "getAppProcessName: "+apps.get(i).activityInfo.packageName);
            data.add(name);
        }
//        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
//        for (PackageInfo p_info:installedPackages) {
//            if(!(p_info.packageName.startsWith("com.android")||p_info.packageName.startsWith("com.oneplus")))
//            data.add(p_info.packageName);
//            System.out.println("===="+p_info.packageName);
//        }
        return data;
    }

    class InfoAdapter extends RecyclerView.Adapter<InfoViewHolder>{
        private List<String> mList;
        private Context mContext;

        public InfoAdapter(List<String> list, Context context) {
            this.mList = list;
            this.mContext = context;
        }

        @NonNull
        @Override
        public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
            InfoViewHolder holder = new InfoViewHolder(view);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String packageName =  holder.infoView.getText().toString();
                    Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                    mContext.startActivity(intent);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
            TextView textView = holder.infoView;
            textView.setText(infoList.get(position));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class InfoViewHolder extends RecyclerView.ViewHolder{
        TextView infoView;
        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            infoView = itemView.findViewById(R.id.infoText);
        }
    }
}