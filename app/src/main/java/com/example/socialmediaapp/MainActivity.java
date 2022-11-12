package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.socialmediaapp.Fragments.AddFragment;
import com.example.socialmediaapp.Fragments.HomeFragment;
import com.example.socialmediaapp.Fragments.NotificationFragment;
import com.example.socialmediaapp.Fragments.ProfileFragment;
import com.example.socialmediaapp.Fragments.SearchFragment;
import com.example.socialmediaapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");


        binding.toolbar.setVisibility(View.GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i){

                    case 0:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new HomeFragment());
                        //Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new NotificationFragment());

                        break;
                    case 2:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new AddFragment());

                        break;
                    case 3:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new SearchFragment());

                        break;
                    case 4:
                        binding.toolbar.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.container, new ProfileFragment());

                        break;
                }
                transaction.commit();


                return false;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.setting:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}