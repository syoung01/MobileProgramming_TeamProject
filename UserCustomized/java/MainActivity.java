package com.example.rdmd;

import static java.net.URLEncoder.*;

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
public class MainActivity extends AppCompatActivity {
    EditText editText1;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.EditText1);
        listView = findViewById(R.id.listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

    }

    //근로자 버튼 layout에 android:onClick="mOnClick"
    public void mOnClick(View v) {
        if (v.getId() == R.id.Ueser1) {
            new Thread() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B490001/sjbJhgigwanGwanriInfoService/getSjbWkGigwanInfoList?"
                            + "&ServiceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D"
                            + "&numOfRows=1000&pageNo=1&gigwanFg=12"; //gigwanFg=12가 재활스포츠기관 의미


                    try {
                        /*과정
                         * 1. URL 객체 만들어 stream 열고 inputStream에게 준다.
                         * 2. InputStream은 byte씩 데이터 전송으로 불편 --> InputStreamReader(바이트->문자 입력 스트림으로) 변경
                         * 3. InputSteamReader를 XmlPullParser에게 준다
                         * 4. ui로 띄울 데이터의 내용들은 stringBuffer로 저장.*/


                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();
                        ;
                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:

                                    runOnUiThread(new Runnable() { //현재 시점이 ui thread 면 실행
                                        @Override
                                        public void run() {
                                            //   Toast.makeText(MainActivity.this, "파싱을 시작했다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("item")) ; // <item> 일땐 아무동작도 X
                                    else if (tag.equals("addr")) {
                                        currentAddress = xpp.nextText(); //addr tag 안 text 부분 가져오기
                                        if (currentAddress.contains(location)) { //주소 text 부분에 문자열 location 포함시 나타나게 하기
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("gigwanNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관명 : ");
                                            xpp.next(); //tag 다음 text 로 이동
                                            buffer.append(xpp.getText()); //xml tag 안 text을 얻어서 버퍼에 추가
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("gigwanFgNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telNo")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }

                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());

                                        //listView 업데이트(갱신)
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(MainActivity.this, "파싱종료!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    //노약자
    public void mOnClick2(View v) {
        if (v.getId() == R.id.Ueser2) {
            new Thread() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B551182/spclMdlrtHospInfoService1/getRcperHospList1?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                            "&pageNo=1&numOfRows=500";

                    try {
                        /*과정
                         * 1. URL 객체 만들어 stream 열고 inputStream에게 준다.
                         * 2. InputStream은 byte씩 데이터 전송으로 불편 --> InputStreamReader(바이트->문자 입력 스트림으로) 변경
                         * 3. InputSteamReader를 XmlPullParser에게 준다.
                         * 4. ui로 띄울 데이터의 내용들은 stringBuffer로 저장.*/


                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:

                                    runOnUiThread(new Runnable() { //현재 시점이 ui thread 면 실행
                                        @Override
                                        public void run() {
                                            //   Toast.makeText(MainActivity.this, "파싱을 시작했다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("item")) ;
                                    else if (tag.equals("addr")) { //<addr> 태그라면
                                        currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("yadmNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("clCdNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("hospUrl")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("홈페이지 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telno")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());

                                        //listView 업데이트(갱신)
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(MainActivity.this, "파싱종료!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    //어린이 (소아) 야간 진료 병원

    public void mOnClick3(View v) {
        if (v.getId() == R.id.Ueser3) {
            new Thread() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B551182/spclMdlrtHospInfoService1/getChildNightMdlrtList1?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D&pageNo=1" +
                            "&numOfRows=500";

                    try {
                        /*과정
                         * 1. URL 객체 만들어 stream 열고 inputStream에게 준다.
                         * 2. InputStream은 byte씩 데이터 전송으로 불편 --> InputStreamReader(바이트->문자 입력 스트림으로) 변경
                         * 3. InputSteamReader를 XmlPullParser에게 준다.
                         * 4. ui로 띄울 데이터의 내용들은 stringBuffer로 저장.*/


                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:

                                    runOnUiThread(new Runnable() { //현재 시점이 ui thread 면 실행
                                        @Override
                                        public void run() {
                                            //   Toast.makeText(MainActivity.this, "파싱을 시작했다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("item")) ;
                                    else if (tag.equals("addr")) { //<addr> 태그라면
                                        currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("yadmNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("clCdNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("hospUrl")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("홈페이지 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telno")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());

                                        //listView 업데이트(갱신)
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(MainActivity.this, "파싱종료!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    //응급환자 (응급병원)
    public void mOnClick4(View v) {
        if (v.getId() == R.id.Ueser4) {
            new Thread() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D&QT=5" +
                            "&pageNo=1&numOfRows=500";

                    try {
                        /*과정
                         * 1. URL 객체 만들어 stream 열고 inputStream에게 준다.
                         * 2. InputStream은 byte씩 데이터 전송으로 불편 --> InputStreamReader(바이트->문자 입력 스트림으로) 변경
                         * 3. InputSteamReader를 XmlPullParser에게 준다.
                         * 4. ui로 띄울 데이터의 내용들은 stringBuffer로 저장.*/


                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:

                                    runOnUiThread(new Runnable() { //현재 시점이 ui thread 면 실행
                                        @Override
                                        public void run() {
                                            //   Toast.makeText(MainActivity.this, "파싱을 시작했다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("item")) ;
                                    else if (tag.equals("dutyAddr")) { //<addr> 태그라면
                                        currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");}

                                    }
                                    else if (tag.equals("dutyName")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("dutyEmclsName")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("응급의료기관구분 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }  else if (tag.equals("dutyTel1")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n\n\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());

                                        //listView 업데이트(갱신)
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(MainActivity.this, "파싱종료!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}






--------------------------------------------------------------------------
---------------------------------------------------------------------------

<변경 전> 
    
package com.example.rdmd;

import static java.net.URLEncoder.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText1;

    TextView textView1;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = findViewById(R.id.EditText1);
        textView1 = findViewById(R.id.textView1);

    }

    //근로자 버튼 layout에 android:onClick="mOnClick"
    public void mOnClick(View v) {

          if(v.getId() == R.id.Ueser1){ //산재재활병원 버튼 클릭시
              //data를 얻어올때 uri에서 내용 비교 후 내가 입력한 주소를 포함한 것만 나오도록 해야 해서 시간이 걸리고 이런건 thread를 사용
                new Thread(new Runnable() {
                    @Override

                    public void run() {
                        data = getXmlData(); //getXmlData 함수로 가서 데이터 얻기
                        runOnUiThread(new Runnable() { //현재 thread 가 main thread면 Runnable 객체의 run() 메소드를 실행 ,(Activity 클래스에서 제공되는 method)
                            @Override
                            public void run() {
                                textView1.setText(data); //그걸 스크롤 뷰안 textView로 띄우기, 이게 ui관련 이니까 main thread 필요
                            }
                        });
                    }
                }).start();

        }
    }

    //노약자
    public void mOnClick2(View v) {

        if(v.getId() == R.id.Ueser2){
            new Thread(new Runnable() {
                @Override

                public void run() {
                    data = getXmlData2();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setText(data);
                        }
                    });
                }
            }).start();

        }
    }

    //어린이(소아)
    public void mOnClick3(View v) {

        if(v.getId() == R.id.Ueser3){
            new Thread(new Runnable() {
                @Override

                public void run() {
                    data = getXmlData3();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setText(data);
                        }
                    });
                }
            }).start();

        }
    }

    //응급환자
    public void mOnClick4(View v) {

        if(v.getId() == R.id.Ueser4){
            new Thread(new Runnable() {
                @Override

                public void run() {
                    data = getXmlData4();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setText(data);
                        }
                    });
                }
            }).start();

        }
    }


//근로자
    String getXmlData() {
        StringBuffer buffer = new StringBuffer(); // 데이터를 저장할 StringBuffer 생성

        String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

        //api 호출을 위한 uri 생성
        String queryUrl = "https://apis.data.go.kr/B490001/sjbJhgigwanGwanriInfoService/getSjbWkGigwanInfoList?"
                + "&ServiceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D"
                + "&numOfRows=500&pageNo=1&gigwanFg=12"; //gigwanFg=12가 재활스포츠기관 의미



        try {
            URL url = new URL(queryUrl);  // URL 객체 생성
            InputStream is = url.openStream();

            // XML 파서 설정
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); // InputStream으로부터 XML 데이터를 읽어오기

            String tag;


            int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
            String currentAddress = "";

            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT: // 문서의 시작일 때
                        xpp.next(); //시작 에서 SRTART_TAG 로 이동
                        //buffer.append("passing start...\n\n");
                        break;

                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                        tag = xpp.getName();

                        if (tag.equals("item")) ; // <item> 일땐 아무동작도 X
                        else if (tag.equals("addr")) {
                            currentAddress = xpp.nextText(); //tag 안 text 부분
                            if (currentAddress.contains(location)) { //주소 text 부분에 문자열 location 포함시 나타나게 하기
                            buffer.append("주소 : ");
                            xpp.next();
                            buffer.append(currentAddress);
                            buffer.append("\n");}

                        }
                        else if (tag.equals("gigwanNm")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("기관명 : ");
                                xpp.next(); //tag 다음 text 로 이동
                                buffer.append(xpp.getText()); //xml tag 안 text을 얻어서 버퍼에 추가
                                buffer.append("\n");
                            }
                        } else if (tag.equals("gigwanFgNm")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("기관구분명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        }  else if (tag.equals("telNo")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("전화번호 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n\n\n");
                            }
                        }
                        break;

                    case XmlPullParser.TEXT: //xml tag안의 text 내용
                        break;

                    case XmlPullParser.END_TAG: // 태그의 끝일 때
                        tag = xpp.getName();
                        if (tag.equals("items")) { //하나의 정보가 끝나면
                            buffer.append("\n");
                        }

                        break;
                }
                eventType = xpp.next(); //다시 tag의 시작부분으로 상태 START_TAG 변경 xml 파일이 다 끝날때까지 반복
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //buffer.append("passing the end\n");

        return buffer.toString(); // 작업이 끝나면 결과를 반환
    }


    //노약자
    String getXmlData2() {
        StringBuffer buffer = new StringBuffer();

        String location = editText1.getText().toString();
        //  String location = URLEncoder.encode(str);

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
            String currentAddress = "";

            while (eventType != XmlPullParser.END_DOCUMENT) { //끝날때까지 반복

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        xpp.next();
                       // buffer.append("passing start...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("addr")) { //<addr> 태그라면
                            currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("주소 : ");
                                xpp.next();
                                buffer.append(currentAddress);
                                buffer.append("\n");}

                        }
                        else if (tag.equals("yadmNm")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("병원명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        } else if (tag.equals("clCdNm")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("기관구분명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        }   else if (tag.equals("hospUrl")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("홈페이지 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        } else if (tag.equals("telno")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
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
                        if (tag.equals("items")) buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // buffer.append("passing the end\n");

        return buffer.toString();
    }


    //소아 (어린이)
    String getXmlData3() {
        StringBuffer buffer = new StringBuffer();

        String location = editText1.getText().toString();
        //  String location = URLEncoder.encode(str);

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
            String currentAddress = "";

            while (eventType != XmlPullParser.END_DOCUMENT) { //끝날때까지 반복

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        xpp.next();
                        //buffer.append("passing start...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("addr")) { //<addr> 태그라면
                            currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("주소 : ");
                                xpp.next();
                                buffer.append(currentAddress);
                                buffer.append("\n");}

                        }
                        else if (tag.equals("yadmNm")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("병원명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        } else if (tag.equals("clCdNm")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("기관구분명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        }   else if (tag.equals("hospUrl")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("홈페이지 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        }else if (tag.equals("telno")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
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
                        if (tag.equals("items")) buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  buffer.append("passing the end\n");

        return buffer.toString();
    }


    String getXmlData4() {
        StringBuffer buffer = new StringBuffer();

        String location = editText1.getText().toString();
        //  String location = URLEncoder.encode(str);

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
            String currentAddress = "";

            while (eventType != XmlPullParser.END_DOCUMENT) { //끝날때까지 반복

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        xpp.next();
                        //buffer.append("passing start...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) ;
                        else if (tag.equals("dutyAddr")) { //<addr> 태그라면
                            currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("주소 : ");
                                xpp.next();
                                buffer.append(currentAddress);
                                buffer.append("\n");}

                        }
                        else if (tag.equals("dutyName")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("병원명 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        } else if (tag.equals("dutyEmclsName")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                buffer.append("응급의료기관구분 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                        }  else if (tag.equals("dutyTel1")) {
                            if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
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
                        if (tag.equals("items")) buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // buffer.append("passing the end\n");

        return buffer.toString();
    }
}

