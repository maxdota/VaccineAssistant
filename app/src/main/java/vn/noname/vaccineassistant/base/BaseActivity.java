package vn.noname.vaccineassistant.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import vn.noname.vaccineassistant.ListActivity;
import vn.noname.vaccineassistant.MainActivity;
import vn.noname.vaccineassistant.PlusActivity;
import vn.noname.vaccineassistant.R;

public abstract class BaseActivity extends FragmentActivity {
    public static final int MAP_SCREEN = 1;
    public static final int PLUS_SCREEN = 2;
    public static final int LIST_SCREEN = 3;

    private ImageView plusIcon;
    private ImageView listIcon;
    private ImageView mapIcon;
    private TextView listText;
    private TextView mapText;

    public abstract int currentScreen();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initBottomNav() {
        plusIcon = findViewById(R.id.plus_icon);
        listIcon = findViewById(R.id.list_icon);
        mapIcon = findViewById(R.id.map_icon);
        listText = findViewById(R.id.list_text);
        mapText = findViewById(R.id.map_text);

        switch (currentScreen()) {
            case MAP_SCREEN:
                mapIcon.setImageResource(R.drawable.ic_map_active);
                mapText.setTextColor(ContextCompat.getColor(this, R.color.bottom_active_text));
                break;
            case PLUS_SCREEN:
                plusIcon.setImageResource(R.drawable.ic_plus_active);
                break;
            case LIST_SCREEN:
                listIcon.setImageResource(R.drawable.ic_list_active);
                listText.setTextColor(ContextCompat.getColor(this, R.color.bottom_active_text));
                break;
        }

        findViewById(R.id.list_container).setOnClickListener(v -> goToListActivity());
        findViewById(R.id.map_container).setOnClickListener(v -> goToMapActivity());
        findViewById(R.id.plus_icon).setOnClickListener(v -> goToPlusActivity());
    }

    private void goToPlusActivity() {
        startActivity(new Intent(this, PlusActivity.class));
        overridePendingTransition(0, 0);
    }

    private void goToMapActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
    }

    private void goToListActivity() {
        startActivity(new Intent(this, ListActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
