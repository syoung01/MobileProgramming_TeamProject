package com.example.rdmd1_3;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로

    Button btn_picture; //사진 찍는 버튼
    Button btn_ocr; //텍스트 추출 버튼

    private String imageFilePath; //이미지 파일 경로
    private Uri p_Uri;

    static final int REQUEST_IMAGE_CAPTURE = 672;

    // OCR 결과를 저장할 변수 추가
    private String OCRresult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_picture = (Button)findViewById(R.id.takePicture);
        btn_ocr = (Button)findViewById(R.id.ocrButton);

        //언어파일 경로
        datapath = getFilesDir()+ "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"), "kor");
        checkFile(new File(datapath + "tessdata/"), "eng");


        /**
         * Tesseract API
         * 한글 + 영어(함께 추출)
         **/
        String lang = "kor+eng";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);


        // 사진 찍는 버튼 클릭시 카메라 킴
        btn_picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                sendTakePhotoIntent();
            }
        });

        // 텍스트 추출 버튼
        btn_ocr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                // 가져와진 사진을 bitmap으로 추출
                BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.imageView)).getDrawable();
                image = d.getBitmap();

                //String OCRresult = null;
                mTess.setImage(image);

                //텍스트 추출
                OCRresult = mTess.getUTF8Text();
                TextView OCRTextView = (TextView) findViewById(R.id.OCRTextView);
                OCRTextView.setText(OCRresult);
                //Log.d("OCR_Result", OCRresult);


                //대장암, 폐암, 위암 3개 작성
                //대장암
                if (OCRresult.contains("대 장 암")) {
                    // 대장암에 대한 정보를 표시하는 다이얼로그 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("대장암 정보");
                    builder.setMessage( "* 대장암 이란?\n대장암은 결장과 직장에 생기는 악성 종양을 말합니다.\n\n* 주된 증상?\n1) 갑자기 변을 보기 힘들어지거나 변 보는 횟수가 바뀌는 등 배변 습관의 변화\n2) 복부 불편감(복통, 복부 팽만)\n3) 복부에서 덩어리가 만져짐\n\n* 수술 후 환자 생활?\n1) 수술 다음날부터 천천히 자세를 변경하며 걷는 운동 시작\n2) 개복수술의 경우, 1개월 정도까지 복대 착용\n3) 산책은 1회 30분 이내, 하루 4회 이상 가볍게 걷기, 6개월 후 대부분의 운동 가능");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 확인 버튼을 눌렀을 때의 처리
                            dialog.dismiss(); // 다이얼로그 닫기
                        }
                    });
                    builder.setNegativeButton("더보기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 더보기 버튼을 눌렀을 때 웹뷰를 통해 자세한 정보를 제공
                            //국가 암 정보 센터 홈페이지 이동 (대장암 부분)
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cancer.go.kr/lay1/program/S1T211C214/cancer/view.do?cancer_seq=3797"));
                            startActivity(intent);
                        }
                    });
                    // 다이얼로그 표시
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                //폐암
                if (OCRresult.contains("폐 암")) {
                    // 대장암에 대한 정보를 표시하는 다이얼로그 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("폐암 정보");
                    builder.setMessage( "* 폐암 이란? \n\n폐암이란 폐(심장과 함께 흉강, 즉 가슴 안을 채우고 있는 장기)에 생긴 악성 종양\n\n* 주된 증상?\n1) *주의) 초기 증상이 거의 없음, 암이 발생한 위치에 따라 증상이 다름\n2) 기침, 호흡곤란, 흉통 등\n3) 쉰 목소리, 연하곤란(삼키기 어려움)\n\n* 수술 후 환자 생활?\n1) 가벼운 운동 추천\n2) 경미한 통증은 진통제나 국소 찜질\n3) 계속적으로 심호흡\n4) 기침을 유발하는 자극 물질들을 피함");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 확인 버튼을 눌렀을 때의 처리
                            dialog.dismiss(); // 다이얼로그 닫기
                        }
                    });
                    builder.setNegativeButton("더보기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 더보기 버튼을 눌렀을 때 웹뷰를 통해 자세한 정보를 제공
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cancer.go.kr/lay1/program/S1T211C215/cancer/view.do?cancer_seq=5237"));
                            startActivity(intent);
                        }
                    });
                    // 다이얼로그 표시
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                //위암
                if (OCRresult.contains("위 암")) {
                    // 대장암에 대한 정보를 표시하는 다이얼로그 생성
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("위암 정보");
                    builder.setMessage("* 위암 이란?\n\n위암이란 원칙적으로 위에 생기는 모든 암을 일컫는 말이지만, 주로 위점막의 선세포(샘세포)에서 발생한 위선암(adenocarcinoma)을 말합니다.\n\n* 주된 증상?\n\n1) 조기 위암 시 무증상 80% 증상 10%\n2) 진행 위암 시 체중감소 60% 그외 복통, 구토, 식욕감퇴 등\n\n* 수술 후 환자 생활?\n\n1) 퇴원 후 적어도 2주일 정도는 집에서 휴식\n2) 회복에는 3~6개월 또는 그 이상 시간이 필요\n3) 3주째 부터 서서히 활동 (30분 정도 산책 추천)");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 확인 버튼을 눌렀을 때의 처리
                            dialog.dismiss(); // 다이얼로그 닫기
                        }
                    });
                    builder.setNegativeButton("더보기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 사용자가 더보기 버튼을 눌렀을 때 웹뷰를 통해 자세한 정보를 제공
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cancer.go.kr/lay1/program/S1T211C213/cancer/view.do?cancer_seq=4661"));
                            startActivity(intent);
                        }
                    });
                    // 다이얼로그 표시
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }


        });
    }

    // 이미지의 ExifOrientation 값을 각도로 변환
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // 비트맵을 주어진 각도로 회전
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // 카메라 앱을 실행하여 사진을 찍는 인텐트를 보내는
    private void sendTakePhotoIntent(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                p_Uri = FileProvider.getUriForFile(this, getPackageName(), photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, p_Uri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ((ImageView) findViewById(R.id.imageView)).setImageURI(p_Uri);
            ExifInterface exif = null;

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            ((ImageView)findViewById(R.id.imageView)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    // 이미지 파일을 생성
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    //언어 데이터 파일을 장치에 복사
    private void copyFiles(String lang) {
        try{
            //파일이 있을 위치
            String filepath = datapath + "/tessdata/"+lang+".traineddata";

            //AssetManager에 액세스
            AssetManager assetManager = getAssets();

            //읽기/쓰기를 위한 열린 바이트 스트림
            InputStream instream = assetManager.open("tessdata/"+lang+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //filepath에 의해 지정된 위치에 파일 복사
            byte[] buffer = new byte[1024];
            int read;

            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir, String lang) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles(lang);
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/"+lang+".traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles(lang);
            }
        }
    }
}


