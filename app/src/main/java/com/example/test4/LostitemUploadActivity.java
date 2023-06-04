package com.example.test4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LostitemUploadActivity extends AppCompatActivity {

    String userID = null;
    String userPassword = null;

    ImageView imageView;
    Button findimgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostitem_upload);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            userPassword = intent.getStringExtra("userPassword");
        }

        imageView = findViewById(R.id.uploadimg);
        findimgButton = findViewById(R.id.findimgButton);

        findimgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        Button finaluploadButton = findViewById(R.id.finaluploadButton);
        finaluploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String uploadID = userID;
                String uploadItem = ((EditText) findViewById(R.id.LostItemNameText)).getText().toString();
                String uploadTime = getCurrentTime();

                try {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                    // Resize bitmap
                    Bitmap resizedBitmap = resizeBitmap(bitmap, 1000); // Adjust this value as needed

                    // 이미지를 Base64 문자열로 인코딩
                    String imageString = encodeImageToString(resizedBitmap);

                    // 데이터베이스에 업로드 요청
                    uploadData(uploadID, uploadItem, uploadTime, imageString);
                } catch (ClassCastException e) {
                    // 이미지가 없거나 이미지 로드에 실패한 경우에 대한 처리
                    Toast.makeText(getApplicationContext(), "Please select an image.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // 에러가 발생한 경우에 대한 처리
                    Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });


    }

    private void uploadData(final String userID, final String itemName, final String currentTime, final String imageString) {
        String url = "http://bestknow98.cafe24.com/LostItem.php";

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // DB에 저장 성공한 경우 activity_lost_item으로 돌아가기
                        Intent intent = new Intent(LostitemUploadActivity.this, LostItemActivity.class);
                        intent.putExtra("userID", userID);
                        intent.putExtra("userPassword", userPassword);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                params.put("LostItemName", itemName);
                params.put("LostDate", currentTime);
                params.put("LostItemPicture", imageString);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String encodeImageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageView.setImageURI(data.getData());
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxResolution) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                height = (int) (height * rate);
                width = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                width = (int) (width * rate);
                height = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

}
