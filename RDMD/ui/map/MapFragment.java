package androidtown.org.termproject.ui.map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import androidtown.org.termproject.MainActivity;
import androidtown.org.termproject.R;

public class MapFragment extends Fragment {
    ArrayList<Double> lat_list;
    ArrayList<Double> lng_list;
    ArrayList<String> name_list;
    ArrayList<String> vicinity_list;
    // List of objects that manage the marked markers (marking the surroundings) on the map

    public int selectedCategoryIndex = -1;
    Toast t;

    ArrayList<Marker> markers_list;
    // to Construct Dialogue
    String[] category_name_array={
            "모두","병원","동물병원"
    };
    // to types value
    String[] category_value_array={
            "all","hospital","veterinary_care"
    };

    String[] permission_list = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    // Current User Location
    Location myLocation;
    TextView text;
    // location information Manager
    LocationManager manager;
    // Object that manages the map
    GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        lat_list = new ArrayList<>();
        lng_list = new ArrayList<>();
        name_list = new ArrayList<>();
        vicinity_list = new ArrayList<>();
        markers_list = new ArrayList<>();

        checkPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                }
            });
        }

        // using the Options menu
        setHasOptionsMenu(true);

        return view;
    }

    public void checkPermission() {
        boolean isGrant = false;
        for (String str : permission_list) {
            if (ContextCompat.checkSelfPermission(getContext(), str) == PackageManager.PERMISSION_GRANTED) {
            } else {
                isGrant = false;
                break;
            }
        }
        if (!isGrant) {
            requestPermissions(permission_list, 0);
        } else {
            getMyLocation();
        }
    }

    // Method called when the user presses the Allow/Reject Permissions button
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant = true;
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                isGrant = false;
                break;
            }
        }
        // If all permissions are allowed, measure the user location
        if (isGrant == true) {
            getMyLocation();
        }
    }

    // get the current location.
    public void getMyLocation() {
        manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        // Make sure to operate only when all permissions are granted
        int chk1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int chk2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (chk1 == PackageManager.PERMISSION_GRANTED && chk2 == PackageManager.PERMISSION_GRANTED) {
            myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            showMyLocation();
        }
        // Measure a new location
        GpsListener listener = new GpsListener();
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, listener);
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }
    }

    // GPS Listener
    class GpsListener implements LocationListener {

        public void onLocationChanged(Location location) {
            // Save current location value
            myLocation = location;

            // Stop update location
            manager.removeUpdates(this);

            // Move map to current location
            showMyLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }
    }

    // Display the map based on the current location
    public void showMyLocation() {
        // Defensive Coding
        if (myLocation == null) {
            return;
        }
        // Extract current location values
        double lat = myLocation.getLatitude();
        double lng = myLocation.getLongitude();

        // Creating Latitude Variation Objects
        LatLng position = new LatLng(lat, lng);

        // Set current location
        CameraUpdate update1 = CameraUpdateFactory.newLatLng(position);
        map.moveCamera(update1);

        // Expansion
        CameraUpdate update2 = CameraUpdateFactory.zoomTo(15);
        map.animateCamera(update2);

        // Show current location
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        // Change Map Mode
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Method called when touching a menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // id extraction
        int id = item.getItemId();
        if (id == R.id.item1) {
            // Measure the current location
            getMyLocation();
        } else if (id == R.id.item2) {
            // Import Peripheral Information
            showCategoryList();
        }
        return super.onOptionsItemSelected(item);
    }
    // List of Peripheral Categories
    private void showCategoryList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("장소 타입 선택");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, category_name_array);
        DialogListener listener = new DialogListener();
        builder.setAdapter(adapter, listener);
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    // DialogListener
    class DialogListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            // Gets the type value of the item index selected by the user
            String type=category_value_array[i];

            selectedCategoryIndex = i; // Store the index of the item selected by the user

            // get near by place
            getNearbyPlace(type);
        }
    }

    // get near by place
    public void getNearbyPlace(String type_keyword){
        NetworkThread thread=new NetworkThread(type_keyword);
        thread.start();
    }
    public void showMarker(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Marking Markers on a Map

                // Remove all existing markers
                for (Marker marker : markers_list) {
                    marker.remove();
                }
                markers_list.clear();

                // Create as many marker objects as the list
                for (int i = 0; i < lat_list.size(); i++) {
                    // value extraction
                    double lat = lat_list.get(i);
                    double lng = lng_list.get(i);
                    String name = name_list.get(i);
                    String vicinity = vicinity_list.get(i);
                    // Create Information Retention Objects for Markers to Create
                    MarkerOptions options = new MarkerOptions();
                    // Positioning
                    LatLng pos = new LatLng(lat, lng);
                    options.position(pos);
                    // Setting the value for speech balloons to be displayed
                    options.title(name);
                    options.snippet(vicinity);
                    // Marker Maps
                    Marker marker = map.addMarker(options);
                    markers_list.add(marker);
                }
            }
        });
    }
    class NetworkThread extends Thread {
        String type_keyword;

        public NetworkThread(String type_keyword) {
            this.type_keyword = type_keyword;
        }

        @Override
        public void run() {
            try {
                // Initialize the list to contain the data
                lat_list.clear();
                lng_list.clear();
                name_list.clear();
                vicinity_list.clear();

                // Set search page address
                String site = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
                site += "?location=" + myLocation.getLatitude() + ","
                        + myLocation.getLongitude()
                        // Set the range here, 1km now
                        + "&radius=1000&sensor=false&language=ko"
                        + "&key=AIzaSyC2kNOA7cqmGt9fxWfJioxewwrwqi7tgiA";
                if (type_keyword != null && type_keyword.equals("all") == false) {
                    site += "&type=" + type_keyword;
                }
                Log.d("URL", site);
                // access
                URL url = new URL(site);
                URLConnection conn = url.openConnection();
                // Stream extraction
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                StringBuffer buf = new StringBuffer();
                // read
                do {
                    str = br.readLine();
                    if (str != null) {
                        buf.append(str);
                    }
                } while (str != null);
                String rec_data = buf.toString();
                // JSON Data analysis
                JSONObject root = new JSONObject(rec_data);
                // Extract the status value
                String status = root.getString("status");
                // Mark the imported value on the map if any
                if (status.equals("OK")) {
                    // get results array
                    JSONArray results = root.getJSONArray("results");
                    // Repeat as many times as possible
                    for (int i = 0; i < results.length(); i++) {
                        // Extract the object (information about a place)
                        JSONObject obj1 = results.getJSONObject(i);
                        // Latitude longitude extraction
                        JSONObject geometry = obj1.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        // Extract Place Names
                        String name = obj1.getString("name");
                        // approximate address extraction
                        String vicinity = obj1.getString("vicinity");
                        // contains data
                        lat_list.add(lat);
                        lng_list.add(lng);
                        name_list.add(name);
                        vicinity_list.add(vicinity);
                    }
                    showMarker();
                } else {
                    String selectedOption = category_name_array[selectedCategoryIndex]; // User-selected option name
                    String message = "주변에 " + selectedOption + "이(가) 없습니다.";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                            Gravity Gravity = null;
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });
                    Log.d("Toast", message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
