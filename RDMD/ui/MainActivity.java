package androidtown.org.termproject;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidtown.org.termproject.databinding.ActivityMainBinding;
import androidtown.org.termproject.ui.custom.CustomFragment;
import androidtown.org.termproject.ui.rank.RankFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_camera, R.id.navigation_home,
                R.id.navigation_custom, R.id.navigation_rank)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Use NavHostFragment when MainActivity handles bottom navigation events
        NavigationUI.setupWithNavController(navView, navController);

        // Use NavHostFragment to set up ActionBar as well
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Go back to home (when user click the home icon)
                if (navController.getCurrentDestination().getId() != R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home);
                    return true;
                }
            } else {
                // events when user click on another icon
                navController.navigate(item.getItemId());
                return true;
            }
            return false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // processed when user click the Back button
            // To go to HomeFragment, use the NavController to invoke the navigate() method
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // to handle Custom Fragment button click events
    public void mOnClick(View v) {
        if (v.getId() == R.id.Ueser1) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick(v);
            }
        }
    }
    public void mOnClick2(View v) {
        if (v.getId() == R.id.Ueser2) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick2(v);
            }
        }
    }
    public void mOnClick3(View v) {
        if (v.getId() == R.id.Ueser3) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick3(v);
            }
        }
    }
    public void mOnClick4(View v) {
        if (v.getId() == R.id.Ueser4) {
            CustomFragment customFragment = (CustomFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_custom);
            if (customFragment != null) {
                customFragment.mOnClick4(v);
            }
        }
    }
    // to handle RankFragment button click events
    public void mOnClick_rank1(View v) {
        if (v.getId() == R.id.rg_btn1) {
            RankFragment rankFragment = (RankFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_rank);
            if (rankFragment != null) {
                rankFragment.mOnClick_rank1(v);
            }
        }
    }
    public void mOnClick_rank2(View v) {
        if (v.getId() == R.id.rg_btn2) {
            RankFragment rankFragment = (RankFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_rank);
            if (rankFragment != null) {
                rankFragment.mOnClick_rank2(v);
            }
        }
    }
    public void mOnClick_rank3(View v) {
        if (v.getId() == R.id.rg_btn3) {
            RankFragment rankFragment = (RankFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_rank);
            if (rankFragment != null) {
                rankFragment.mOnClick_rank3(v);
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, (AppBarConfiguration) null)
                || super.onSupportNavigateUp();
    }

}
