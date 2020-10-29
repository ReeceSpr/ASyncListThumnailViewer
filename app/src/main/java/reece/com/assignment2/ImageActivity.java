package reece.com.assignment2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

/*
    This is the activity is the original image in original scaling, resolution.
    The doc was used as a guide for the scaling aspect.
    https://developer.android.com/training/gestures/scale
 */



public class ImageActivity extends AppCompatActivity {

    //Handles the scaling.
    private ScaleGestureDetector mScaleGestureDetector;

    //Image on screen declared for use in mScaleGestureDetector
    private ImageView mImageView;

    //Current Scaling
    private float mScaleFactor = 1.0f;

    /*
     * Receives image path via intent.
     * Sets image.
     * Creates our customer gesture instance.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        String image_path = intent.getStringExtra("IMAGE_PATH");
        Bitmap bitmap = BitmapFactory.decodeFile(image_path);

        mImageView = findViewById(R.id.imageView);
        mImageView.setImageBitmap(bitmap);

        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    /*
     * Detects touch forwards to scaling
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /*
     * Scales the image
     * Gets ScaleFactor as argument passed from dispatchTouchEvent.
     * Has max/min factor to prevent over scaling.
     * Panning <TO-DO>
     */
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor,10.0f));

            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);

            return true;
        }
    }
}
