package androidtown.org.termproject.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import androidtown.org.termproject.ImageAdapter;
import me.relex.circleindicator.CircleIndicator3;

import java.util.Arrays;
import java.util.List;

import androidtown.org.termproject.R;
import androidtown.org.termproject.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private int[] imageResIds = {R.drawable.img_main_rdmd_position2,
            R.drawable.img_main_rdmd_position1, R.drawable.img_main_rdmd_position3};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize and set up ViewPager2 and CircleIndicator3
        ViewPager2 viewPager = root.findViewById(R.id.viewPager);
        CircleIndicator3 indicator = root.findViewById(R.id.indicator);

        // ViewPager2 and Adapter Settings
        ImageAdapter adapter = new ImageAdapter(imageResIds, position -> {
            onImageClick(position);
        });
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        
        root.findViewById(R.id.mainButtonMap).setOnClickListener(this);
        root.findViewById(R.id.mainButtonCustom).setOnClickListener(this);
        root.findViewById(R.id.mainButtonCamera).setOnClickListener(this);
        root.findViewById(R.id.mainButtonRank).setOnClickListener(this);

        return root;
    }

    private void onImageClick(int position) {

        /*Toast.makeText(getContext(), "Image " + position + " clicked", Toast.LENGTH_SHORT).show();*/

        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);

        if (position == 0) {
            String webViewUrl = "https://www.hira.or.kr/ra/eval/getDiagEvlView.do?pgmid=HIRAA030004000100";
            navController.navigate(R.id.action_navigation_home_to_positionFragment);

        } else if (position == 1) {
            navController.navigate(R.id.navigation_custom);

        } else if (position == 2) {
            navController.navigate(R.id.navigation_map);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        // Move to another fragment according to the clicked button
        if (v.getId() == R.id.mainButtonMap) {
            navController.navigate(R.id.navigation_map);
        } else if (v.getId() == R.id.mainButtonCustom) {
            navController.navigate(R.id.navigation_custom);
        } else if (v.getId() == R.id.mainButtonCamera) {
            navController.navigate(R.id.navigation_camera);
        } else if (v.getId() == R.id.mainButtonRank) {
            navController.navigate(R.id.navigation_rank);
        }
    }
}
