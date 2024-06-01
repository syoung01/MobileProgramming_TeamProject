package androidtown.org.termproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import androidtown.org.termproject.R;

public class positionFragment extends Fragment {

    private WebView webView;

    public positionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position, container, false);

        // Initialize WebView
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // Enabling JavaScript
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl("https://www.hira.or.kr/ra/eval/getDiagEvlView.do?pgmid=HIRAA030004000100");

        return view;
    }
}
