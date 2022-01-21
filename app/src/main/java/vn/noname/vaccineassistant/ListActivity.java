package vn.noname.vaccineassistant;

import android.os.Bundle;

import androidx.annotation.Nullable;

import vn.noname.vaccineassistant.base.BaseActivity;

public class ListActivity extends BaseActivity {
    @Override
    public int currentScreen() {
        return LIST_SCREEN;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initBottomNav();
    }
}
