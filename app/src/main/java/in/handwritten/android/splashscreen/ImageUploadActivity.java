package in.handwritten.android.splashscreen;
/*
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import in.handwritten.android.objects.HeadingIndexObject;

public class ImageUploadActivity extends AppCompatActivity {

    boolean changeImage = true;
    ImageView customBackGround;
    float someGlobalXvariable;
    float someGlobalYvariable;
    Bitmap b;
    Bitmap bMutable;
    ArrayList<HeadingIndexObject> headingIndexObjects = new ArrayList<HeadingIndexObject>();
    float xAxis;
    float yAxis;
    float yDiff;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        customBackGround = findViewById(R.id.customBackGround);
        Button changeButton = findViewById(R.id.changeButton);
        Button lockButton = findViewById(R.id.LockButton);
        b = BitmapFactory.decodeResource(getResources(), R.drawable.ruled_paper);

        customBackGround.setOnTouchListener(new View.OnTouchListener() {
            Matrix inverse = new Matrix();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                customBackGround.getImageMatrix().invert(inverse);
                float[] pts = {
                        event.getX(), event.getY()
                };

                inverse.mapPoints(pts);
                double x = Math.floor(pts[0]);
                double y = Math.floor(pts[1]);
                someGlobalXvariable = (float)x;
                someGlobalYvariable = (float) y;
                redrawImage();
                return true;
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(changeImage){
                        customBackGround.setImageDrawable(getDrawable(R.drawable.blank_page_new));
                        changeImage = false;
                    }else{
                        customBackGround.setImageDrawable(getDrawable(R.drawable.ruled_paper));
                        changeImage = true;
                    }
                }
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {*/
                /*
                if (someGlobalXvariable != 0 && someGlobalYvariable != 0) {
                    b = bMutable;
                    HeadingIndexObject obj = new HeadingIndexObject(someGlobalXvariable, someGlobalYvariable);
                    headingIndexObjects.add(obj);
                    Log.d("HeadingIndexObject", headingIndexObjects.toString());
                    if (headingIndexObjects.size() >= 4) {
                        setDimensionValues();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("xAxis", xAxis);
                        returnIntent.putExtra("yAxis", yAxis);
                        returnIntent.putExtra("yDiff", yDiff);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }*//*
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

    }

    public void redrawImage() {

        Bitmap bm = b;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getResources().getDimensionPixelSize(R.dimen._20sdp));
        paint.setColor(Color.BLUE);
        Bitmap proxy = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(proxy);

        //Here, we draw the background image.
        c.drawBitmap(bm, new Matrix(), null);

        //Here, we draw the text where the user last touched.
        c.drawText("Line 1 : Align this text to first line of your page ", someGlobalXvariable, someGlobalYvariable, paint);
        bMutable = proxy;

        customBackGround.setImageBitmap(proxy);
    }

    private void setDimensionValues(){
        xAxis = 0f;
        yAxis = 0f;
        yDiff = 0f;
        yAxis = headingIndexObjects.get(0).getYAxis();
        for (int i=0;i<headingIndexObjects.size();i++){
            xAxis = xAxis + headingIndexObjects.get(i).getXAxis();
            if(i + 1 < headingIndexObjects.size()) {
                yDiff = yDiff + headingIndexObjects.get(i + 1).getYAxis() - headingIndexObjects.get(i).getYAxis();
            }
        }
        xAxis = xAxis / headingIndexObjects.size();
        yDiff = yDiff / headingIndexObjects.size()-1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                customBackGround.setVisibility(View.VISIBLE);
                customBackGround.setImageURI(selectedImage);
                Intent intent = new Intent();
                intent.putExtra("file",new File(selectedImage.getPath()));
                setResult(90,intent);
                finish();

            }
    }
}
*/