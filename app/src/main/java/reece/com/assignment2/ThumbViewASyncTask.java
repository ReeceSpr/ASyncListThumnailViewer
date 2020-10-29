package reece.com.assignment2;

/*
 I missed the lecture on ASync so have researched online from here.
 https://stuff.mit.edu/afs/sipb/project/android/docs/training/displaying-bitmaps/process-bitmap.html
 Comments are here are mine to show understanding of the code I have used.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;

/*
    Extends ASyncTask as doc guideline,
     <
     Params - I could of used this to set the path name, but Params
        is an [] so I saved time/memory just setting it in constructor.
     Progress - I don't use or update the user with progress, so void.
     Result - The result back to the UI thread. PostExecute gets this and sets the ThumbView's image.
 */
class ThumbViewASyncTask extends AsyncTask<Void, Void, Bitmap> {
    /* Weak Reference, allows for the view passed to be garbage collected.
     * IMPORTANT as the gridView might want to recycle the view and we should let it.
     * GridView takes priority over this task.
     * Why add the image if its being recycled.
    */
    private final WeakReference<ThumbnailView> thumbnailViewWeakReference;
    //Path to the file.
    private String bitmapPath;
    private int initialOrientation;

    /*
        ThumbView - The view to be updated after task heavy task is completed
        String - Where we are getting the bitmap from.
     */
    ThumbViewASyncTask(ThumbnailView thumbnailView, String filePath, int initialOrientation) {
        this.bitmapPath = filePath;
        thumbnailViewWeakReference = new WeakReference<ThumbnailView>(thumbnailView);
        this.initialOrientation = initialOrientation;
    }

    /* Make the thumbnail, on separate thread, to our requirements.
     * @Return that thumbnail
    */
    @Override
    protected Bitmap doInBackground(Void... voids) {
        CacheSingleton cache = CacheSingleton.getCache();
        Bitmap bitmap = (Bitmap) cache.get(bitmapPath);
        if(bitmap != null){
            return bitmap;
        }
        bitmap = BitmapFactory.decodeFile(bitmapPath);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,256,256);
        //bitmap = rotate(bitmap);
        if(bitmap!=null && bitmapPath!=null) cache.put(bitmapPath, bitmap);
        return bitmap;
    }

    /* Task gets complete and this is called.
     * Check the view exists, if not we took too long so no need to set image.
     *  If view exists make a proper reference (One that cannot be destroyed, as setImage on a null is bad idea)
     *  and set the thumbView Image.
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (thumbnailViewWeakReference != null && bitmap != null) {
            final ThumbnailView thumbnailView = thumbnailViewWeakReference.get();
            if (thumbnailView != null) {
                thumbnailView.setImageBitmap(bitmap);
            }
        }
    }

    /*
     * Rotates a bitmap given initial but looking at examples
     * loaded in this may be handled within setting a image. (Checked by changing EXIF).
     * Tested
     * This is commented out in doInBackGround.
     */
    private Bitmap rotate(Bitmap bitmap) {
        if (initialOrientation == 0) {
            return bitmap;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(360-initialOrientation);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
    }
}
