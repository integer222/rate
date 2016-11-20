package ru.pap.rate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.pap.rate.fragments.IFragmentScroll;
import ru.pap.rate.fragments.QuoteFragment;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getCurrentFragment();
                if(fragment!=null && fragment instanceof IFragmentScroll){
                    ((IFragmentScroll) fragment).onScrollUp();
                }
            }
        });
        if (savedInstanceState != null) {
            return;
        }

        setContentFragment(new QuoteFragment());





    }

}
