package com.dandy.smilenews.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Dandy on 2016/10/27.
 */

public class BaseActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    public final <E extends View> E findView(int id){
        try {
            return (E) findViewById(id);
        }catch (ClassCastException e){
            throw  e;
        }
    }
}
