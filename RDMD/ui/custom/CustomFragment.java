package androidtown.org.termproject.ui.custom;

import static java.net.URLEncoder.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* Existing import */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidtown.org.termproject.R;


public class CustomFragment extends Fragment {

    EditText editText1;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inplate the layout of the fragment and return the corresponding View object
        View rootView = inflater.inflate(R.layout.fragment_custom, container, false);

        // Access and initialize UI elements in a View object
        editText1 = rootView.findViewById(R.id.EditText1);
        listView = rootView.findViewById(R.id.listview);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Ueser1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });
        view.findViewById(R.id.Ueser2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick2(v);
            }
        });
        view.findViewById(R.id.Ueser3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick3(v);
            }
        });
        view.findViewById(R.id.Ueser4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick4(v);
            }
        });
    }

    // worker button layout, android:onClick="mOnClick"
    public void mOnClick(View v) {
        if (v.getId() == R.id.Ueser1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Read an xml document with information through the open API and show it to listView
                    items.clear();

                    String location = editText1.getText().toString(); // Save entered as string location

                    // Create uri for api call
                    String queryUrl = "https://apis.data.go.kr/B490001/sjbJhgigwanGwanriInfoService/getSjbWkGigwanInfoList?"
                            + "&ServiceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D"
                            + "&numOfRows=1000&pageNo=1&gigwanFg=12"; //GigwanFg=12 is the meaning of rehabilitation sports institution


                    try {
                        /* Course
                         * 1. Create URL object stream and give it to inputStream.
                         * 2. InputStream is inconvenient to send data byte --> Change the InputStreamReader (byte->text input stream)
                         * 3. Give the InputSteamReader to the XmlPullPacer
                         * 4. Save the contents of the data to be displayed in ui as a string buffer.*/

                        // Create URL Object
                        URL url = new URL(queryUrl);
                        //Open Stream
                        InputStream is = url.openStream();

                        // Create an object to parse read XML documents, create a factory first to create xpp (parase)
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // Read XML data from InputStream

                        String tag;

                        int eventType = xpp.getEventType(); // Obtaining Event Types
                        StringBuffer buffer = new StringBuffer();
                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { // Repeat until the end of XML documentation

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG: // At the beginning of the tag
                                    tag = xpp.getName();

                                    if (tag.equals("addr")) {
                                        currentAddress = xpp.nextText(); // (in addr tag) get text part
                                        if (currentAddress.contains(location)) { // Make it appear when you include a string location in the address text part
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("gigwanNm")) {
                                        if (currentAddress.contains(location)) { // Make it appear when including location
                                            buffer.append("기관명 : ");
                                            xpp.next(); // tag Next move to text
                                            buffer.append(xpp.getText()); // Get xml tag An text and add it to the buffer
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("gigwanFgNm")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telNo")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: // text content in xml tag
                                    break;

                                case XmlPullParser.END_TAG: // At the end of the tag
                                    tag = xpp.getName();
                                    if (tag.equals("items")) { // When end one information
                                        // Add parsing results to items list
                                        items.add(buffer.toString());
                                    }
                                    break;
                            }
                            eventType = xpp.next();
                        }
                        //Handling possible exceptions during xml parsing
                    } catch (MalformedURLException e) { // Network Connection
                        e.printStackTrace();
                    } catch (IOException e) { // Input/output
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { // xml parsing
                        e.printStackTrace();
                    }

                    // Request UI update after loading data
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }

    // the old
    public void mOnClick2(View v) {
        if (v.getId() == R.id.Ueser2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    items.clear();

                    String location = editText1.getText().toString();
                    String queryUrl = "https://apis.data.go.kr/B551182/spclMdlrtHospInfoService1/getRcperHospList1?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                            "&pageNo=1&numOfRows=500";

                    try {
                        
                        URL url = new URL(queryUrl);
                        InputStream is = url.openStream();

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));

                        String tag;

                        int eventType = xpp.getEventType();
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) {

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG:
                                    tag = xpp.getName();

                                    if (tag.equals("addr")) {
                                        currentAddress = xpp.nextText(); 
                                        if (currentAddress.contains(location)) {
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("yadmNm")) {
                                        if (currentAddress.contains(location)) { 
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("clCdNm")) {
                                        if (currentAddress.contains(location)) { 
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("hospUrl")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("홈페이지 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telno")) {
                                        if (currentAddress.contains(location)) { 
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT:
                                    break;

                                case XmlPullParser.END_TAG:

                                    tag = xpp.getName();
                                    if (tag.equals("items")) {

                                        items.add(buffer.toString());
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }

    // children's (child) night care clinic
    public void mOnClick3(View v) {
        if (v.getId() == R.id.Ueser3) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    items.clear();

                    String location = editText1.getText().toString(); 
                    String queryUrl = "https://apis.data.go.kr/B551182/spclMdlrtHospInfoService1/getChildNightMdlrtList1?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D&pageNo=1" +
                            "&numOfRows=500";

                    try {
                        URL url = new URL(queryUrl);
                        InputStream is = url.openStream();

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));

                        String tag;

                        int eventType = xpp.getEventType(); 
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG: 
                                    tag = xpp.getName();

                                    if (tag.equals("addr")) { 
                                        currentAddress = xpp.nextText(); 
                                        if (currentAddress.contains(location)) { 
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("yadmNm")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("clCdNm")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("hospUrl")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("홈페이지 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telno")) {
                                        if (currentAddress.contains(location)) { 
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT:
                                    break;

                                case XmlPullParser.END_TAG:

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { 
                                        items.add(buffer.toString());
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) { 
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }
    // Emergency Patients (Emergency Hospital)
    public void mOnClick4(View v) {
        if (v.getId() == R.id.Ueser4) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    items.clear();

                    String location = editText1.getText().toString();
                    String queryUrl = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D&QT=5" +
                            "&pageNo=1&numOfRows=500";

                    try {
                        URL url = new URL(queryUrl);
                        InputStream is = url.openStream();

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8")); 

                        String tag;

                        int eventType = xpp.getEventType(); 
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { 

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG:
                                    tag = xpp.getName();

                                    if (tag.equals("dutyAddr")) { 
                                        currentAddress = xpp.nextText(); 
                                        if (currentAddress.contains(location)) {
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");}

                                    }
                                    else if (tag.equals("dutyName")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("dutyEmclsName")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("응급의료기관구분 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }  else if (tag.equals("dutyTel1")) {
                                        if (currentAddress.contains(location)) {
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n\n\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: 
                                    break;

                                case XmlPullParser.END_TAG: 

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { 
                                        items.add(buffer.toString());
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }
                    } catch (MalformedURLException e) { 
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }
}
