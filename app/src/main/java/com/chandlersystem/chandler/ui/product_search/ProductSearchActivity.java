package com.chandlersystem.chandler.ui.product_search;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.chandlersystem.chandler.R;

public class ProductSearchActivity extends AppCompatActivity implements ProductSearchFragment.ProductSearchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment searchFragment = ProductSearchFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.container, searchFragment).commit();
    }

    @Override
    public void onTopPick() {
        Toast.makeText(this, "onToppick", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrending() {
        Toast.makeText(this, "onTrending", Toast.LENGTH_SHORT).show();
    }
}
