package androidtown.org.termproject.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidtown.org.termproject.R;
import androidtown.org.termproject.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 버튼들에 클릭 리스너 등록
        binding.mainButtonMap.setOnClickListener(this);
        binding.mainButtonCustom.setOnClickListener(this);
        binding.mainButtonCamera.setOnClickListener(this);
        binding.mainButtonRank.setOnClickListener(this);

        return root;
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
        // 클릭된 버튼에 따라 다른 fragment로 이동
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
