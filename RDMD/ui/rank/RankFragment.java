package androidtown.org.termproject.ui.rank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*Existing import*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidtown.org.termproject.MainActivity;
import androidtown.org.termproject.R;

public class RankFragment extends Fragment {
    Button button1;
    Button button2;
    Button button3;
    ListView listView;

    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();
    RadioGroup radioGroup;
    RadioButton rg_btn1, rg_btn2, rg_btn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);

        radioGroup = view.findViewById(R.id.radioGroup);
        rg_btn1 = view.findViewById(R.id.rg_btn1);
        rg_btn2 = view.findViewById(R.id.rg_btn2);
        rg_btn3 = view.findViewById(R.id.rg_btn3);

        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_btn1.setText("대장암");
                rg_btn2.setText("위암");
                rg_btn3.setText("폐암");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_btn1.setText("고혈압");
                rg_btn2.setText("당뇨병");
                rg_btn3.setText("천식");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_btn1.setText("폐렴");
                rg_btn2.setText("급성기 뇌졸중");
                rg_btn3.setText("관상동맥우회술");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.rg_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick_rank1(v);
            }
        });
        view.findViewById(R.id.rg_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick_rank2(v);
            }
        });
        view.findViewById(R.id.rg_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick_rank3(v);
            }
        });
    }
    public void mOnClick_rank1(View v) {
        if (v.getId() == R.id.rg_btn1) {
            if(rg_btn1.getText().equals("대장암")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Read an xml document with information through the open API and show it to listView
                        items.clear();

                        // Create uri for api call
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
                                "&numOfRows=500";

                        try {
                            /* Course
*                             1. Create URL object stream and give it to inputStream.
*                             2. InputStream is inconvenient to send data byte --> Change the InputStreamReader (byte->text input stream)
*                             3. Give the InputSteamReader to the XmlPullPacer.
*                             4. Save the contents of the data to be displayed in ui as a string buffer.*/

                            // Create URL Object
                            URL url = new URL(queryUrl);
                            // Open Stream
                            InputStream is = url.openStream();

                            // Create an object to parse read XML documents, create a factory first to create xpp (parase)
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            XmlPullParser xpp = factory.newPullParser();
                            xpp.setInput(new InputStreamReader(is, "UTF-8"));  // Read XML data from InputStream

                            String tag;

                            int eventType = xpp.getEventType(); // get Event Types
                            StringBuffer buffer = new StringBuffer();

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "대장암";
                            String searchAsmGrd = "3"; // check more than 3 times

                            while (eventType != XmlPullParser.END_DOCUMENT) { //Repeat until the end of the XML document

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // At the beginning of the tag
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); // Get current disease name
                                            // Inquiry when the name of the current disease matches the name of the disease you are looking for and when the period of excellence is selected more than three times
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

                                        }

                                        break;

                                    case XmlPullParser.TEXT: // text in xml tag
                                        break;

                                    case XmlPullParser.END_TAG: // At the end of the tag

                                        tag = xpp.getName();
                                        if (tag.equals("items")) { // end one information
                                            // Add parsing results to items list
                                            items.add(buffer.toString());
                                        }
                                        break;
                                }
                                eventType = xpp.next();
                            }
                            // Message main thread for UI update after loading data
                            handler.sendEmptyMessage(0);
                            // Handling possible exceptions during xml parsing
                        } catch (MalformedURLException e) { // Network connection
                            e.printStackTrace();
                        } catch (IOException e) { // Input/output
                            e.printStackTrace();
                        } catch (XmlPullParserException e) { // xml parsing
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (rg_btn1.getText().equals("고혈압")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "고혈압";
                            String searchAsmGrd = "3";

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); 
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else if (rg_btn1.getText().equals("폐렴")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "폐렴";
                            String searchAsmGrd = "3";

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); 
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }


        }

    }

    // Second radio button
    public void mOnClick_rank3(View v) {
        if (v.getId() == R.id.rg_btn3) {
            if(rg_btn3.getText().equals("폐암")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "폐암"; 
                            String searchAsmGrd = "3";

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText();
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            } else if (rg_btn3.getText().equals("천식")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "천식"; 
                            String searchAsmGrd = "3"; 

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: 
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText();
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else if (rg_btn3.getText().equals("관상동맥우회술")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "관상동맥우회술"; 
                            String searchAsmGrd = "3"; 

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); 
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }


        }

    }


    // The third radio button
    public void mOnClick_rank2(View v) {
        if (v.getId() == R.id.rg_btn2) {
            if(rg_btn2.getText().equals("위암")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "위암"; 
                            String searchAsmGrd = "3"; 

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); 
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            } else if (rg_btn2.getText().equals("당뇨병")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "당뇨병"; 
                            String searchAsmGrd = "3";

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); 
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else if (rg_btn2.getText().equals("급성기 뇌졸중")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "급성기 뇌졸중"; 
                            String searchAsmGrd = "3";

                            while (eventType != XmlPullParser.END_DOCUMENT) {

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: 
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); 
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }


        }

    }
    //Handler
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // UI Update Code
            adapter.notifyDataSetChanged();
        }
    };
}

