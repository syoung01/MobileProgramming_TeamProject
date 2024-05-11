package androidtown.org.termproject;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import androidtown.org.termproject.databinding.ActivityMainBinding;
import androidtown.org.termproject.ui.custom.CustomFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_pet, R.id.navigation_home,
                R.id.navigation_custom, R.id.navigation_rank)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // MainActivity에서 바텀 네비게이션 이벤트를 처리할 때 NavHostFragment를 사용합니다.
        NavigationUI.setupWithNavController(navView, navController);

        // ActionBar를 설정할 때도 NavHostFragment를 사용합니다.
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                //다시 home으로 이동 (home 아이콘 클릭 시)
                if (navController.getCurrentDestination().getId() != R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home);
                    return true;
                }
            } else {
//                다른 아이콘 클릭 시 이벤트 처리
                navController.navigate(item.getItemId());
                return true;
            }
            return false;
        });
    }

    // 버튼 클릭 이벤트를 처리하는 메서드
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
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, (AppBarConfiguration) null)
                || super.onSupportNavigateUp();
    }
}
