package androidtown.org.termproject.ui.camera;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidtown.org.termproject.R;

public class CameraFragment extends Fragment {
    Bitmap image; // Image used
    private TessBaseAPI mTess; // Tess API reference
    String datapath = "" ; // Paths (language data)

    LinearLayout btn_picture; // taking pictures button
    LinearLayout btn_ocr; // Text extraction button

    private String imageFilePath; // Image file path
    private Uri p_Uri;
    ImageView imageView;

    static final int REQUEST_IMAGE_CAPTURE = 672;

    // variables to store OCR results
    private String OCRresult = "";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        btn_picture = view.findViewById(R.id.takePicture);
        btn_ocr = view.findViewById(R.id.ocrButton);
        imageView = view.findViewById(R.id.imageView);

        // Language file path
        datapath = getActivity().getFilesDir() + "/tesseract/";

        // Check if training data is copied
        checkFile(new File(datapath + "tessdata/"), "kor");
        checkFile(new File(datapath + "tessdata/"), "eng");

        // Tesseract API
        // Korean + English (Extract together)
        String lang = "kor+eng";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

        // On Camera when user click the button to take a picture
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTakePhotoIntent();
            }
        });

        // Text extraction button
        btn_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract the imported pictures with bitmap
                if (imageView != null) { // Check ImageView is not null
                    BitmapDrawable d = (BitmapDrawable) imageView.getDrawable();
                    if (d != null) { // Check if getDrawable() is not null
                        image = d.getBitmap();
                        Log.d("TextExtraction", "Image is set for OCR"); // Image Settings Log
                    } else {
                        // exceptions if images null
                        Log.e("TextExtraction", "No drawable found in ImageView"); // No Drawable Log
                        return;
                    }
                } else {
                    // exceptions when imageView is null
                    Log.e("TextExtraction", "ImageView is null"); // ImageView null Log
                    return;
                }

                //String OCRresult = null;
                mTess.setImage(image);

                // text extraction
                OCRresult = mTess.getUTF8Text();
                Log.d("TextExtraction", "OCR Result: " + OCRresult);
                TextView OCRTextView = getView().findViewById(R.id.OCRTextView);
                OCRTextView.setText(OCRresult);

                // "대장암, 폐암, 위암" writing out
                handleOCRResult(OCRresult);
            }
        });

        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button takePictureButton = view.findViewById(R.id.takePicture);

        String text1 = "진단서 촬영";
        String text2 = "\n빠르게 암 정보 검색";

        SpannableString spannableString = new SpannableString(text1 + text2);

        // Style Settings
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, text1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), text1.length(), (text1 + text2).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.8f), text1.length(), (text1 + text2).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set text styled on the button
        takePictureButton.setText(spannableString);
    }*/

    private void handleOCRResult(String OCRresult) {
        if (OCRresult.contains("대 장 암")) {
            showCancerInfoDialog("대장암 정보",
                    "* 대장암 이란?\n대장암은 결장과 직장에 생기는 악성 종양을 말합니다.\n\n* 주된 증상?\n1) 갑자기 변을 보기 힘들어지거나 변 보는 횟수가 바뀌는 등 배변 습관의 변화\n2) 복부 불편감(복통, 복부 팽만)\n3) 복부에서 덩어리가 만져짐\n\n* 수술 후 환자 생활?\n1) 수술 다음날부터 천천히 자세를 변경하며 걷는 운동 시작\n2) 개복수술의 경우, 1개월 정도까지 복대 착용\n3) 산책은 1회 30분 이내, 하루 4회 이상 가볍게 걷기, 6개월 후 대부분의 운동 가능",
                    "https://www.cancer.go.kr/lay1/program/S1T211C214/cancer/view.do?cancer_seq=3797");
        }

        if (OCRresult.contains("폐 암")) {
            showCancerInfoDialog("폐암 정보",
                    "* 폐암 이란? \n\n폐암이란 폐(심장과 함께 흉강, 즉 가슴 안을 채우고 있는 장기)에 생긴 악성 종양\n\n* 주된 증상?\n1) *주의) 초기 증상이 거의 없음, 암이 발생한 위치에 따라 증상이 다름\n2) 기침, 호흡곤란, 흉통 등\n3) 쉰 목소리, 연하곤란(삼키기 어려움)\n\n* 수술 후 환자 생활?\n1) 가벼운 운동 추천\n2) 경미한 통증은 진통제나 국소 찜질\n3) 계속적으로 심호흡\n4) 기침을 유발하는 자극 물질들을 피함",
                    "https://www.cancer.go.kr/lay1/program/S1T211C215/cancer/view.do?cancer_seq=5237");
        }

        if (OCRresult.contains("위 암")) {
            showCancerInfoDialog("위암 정보",
                    "* 위암 이란?\n\n위암이란 원칙적으로 위에 생기는 모든 암을 일컫는 말이지만, 주로 위점막의 선세포(샘세포)에서 발생한 위선암(adenocarcinoma)을 말합니다.\n\n* 주된 증상?\n\n1) 조기 위암 시 무증상 80% 증상 10%\n2) 진행 위암 시 체중감소 60% 그외 복통, 구토, 식욕감퇴 등\n\n* 수술 후 환자 생활?\n\n1) 퇴원 후 적어도 2주일 정도는 집에서 휴식\n2) 회복에는 3~6개월 또는 그 이상 시간이 필요\n3) 3주째 부터 서서히 활동 (30분 정도 산책 추천)",
                    "https://www.cancer.go.kr/lay1/program/S1T211C213/cancer/view.do?cancer_seq=4661");
        }
    }

    private void showCancerInfoDialog(String title, String message, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("더보기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Converting the ExifOrientation value of the image to an angle (not to come out by turning it around)
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

    // Rotating the bitmap at a given angle
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // Running the camera app to take pictures
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }

            if (photoFile != null) {
                p_Uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, p_Uri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //Call when you take an image and receive the result, call createImageFile within onActivityResult to create an image file
    //Put it in the image view
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            ImageView imageView = getActivity().findViewById(R.id.imageView);
            imageView.setImageURI(p_Uri);

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
            imageView.setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    // Create an image file
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,       //prefix
                ".jpg",          //suffix
                storageDir           //directory
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    // Copying a Language Data File to a Device
    private void copyFiles(String lang) {
        try {
            // file location
            String filepath = datapath + "/tessdata/" + lang + ".traineddata";

            // Access AssetManager
            AssetManager assetManager = getActivity().getAssets();

            // Open byte stream for read/write
            InputStream instream = assetManager.open("tessdata/" + lang + ".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            // Copy the file to the location specified by filepath
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

    // Verify that the file is on user device
    private void checkFile(File dir, String lang) {
        // If the directory does not exist, create a directory and then copy the file
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(lang);
        }
        // If have a directory but don't have a file, proceed to copy the file
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/" + lang + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(lang);
            }
        }
    }
}
