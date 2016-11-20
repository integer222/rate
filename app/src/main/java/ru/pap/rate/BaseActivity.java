package ru.pap.rate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alex on 09.11.16.
 */

public abstract class BaseActivity extends AppCompatActivity {


    public void setContentFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    public Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }



}
