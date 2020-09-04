package com.example.taxinvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.taxinvoice.Fragments.DownloadingFragment;
import com.example.taxinvoice.Fragments.SettingFragment;
import com.example.taxinvoice.Fragments.UploadingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomnavigationActivity extends AppCompatActivity {
    BottomNavigationView bottomvavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomnavigation);
        bottomvavigation=findViewById(R.id.bottomnavigation);



        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch ((menuItem.getItemId()))
                {
                    case R.id.action_uploading:
                        openFragment(UploadingFragment.newInstance("",""));
                            return true;
                    case R.id.action_downloading:
                        openFragment(DownloadingFragment.newInstance("",""));
                            return true;
                    case R.id.action_setting:
                        openFragment(SettingFragment.newInstance("",""));
                             return true;
                }
                return false;
            }
        };
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}