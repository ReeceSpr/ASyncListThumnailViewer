package reece.com.assignment2;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;

/*
 * The custom views in the gridView
 */

public class ThumbnailView extends AppCompatImageView {

    // Where to load the thumbnail from
    String imagePath;
    // Orientation
    int initialOrientation;

    // Initialises property,
    // Adds on click
    public ThumbnailView(Context context, String imagePath, int initialOrientation) {
        super(context);
        this.imagePath = imagePath;
        this.initialOrientation = initialOrientation;
        setOnClick();
    }

    // Creates a ASync Task to load the thumbnail into memory and sets it to the imageView.Image;
    public void setThumbImage(){
        ThumbViewASyncTask bitmapTask = new ThumbViewASyncTask(this, imagePath,initialOrientation);
        bitmapTask.execute();
    }

    // On click listener to open new dialog when clicked.
    private void setOnClick(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    // Create a new dialog passing the ImagePath.
    // Image path is used to load image at full res.
    // Activity Start.
    private void openDialog (){
        Intent intent = new Intent(getContext(), ImageActivity.class);
        intent.putExtra("IMAGE_PATH",imagePath);
        getContext().startActivity(intent);
    }
}
