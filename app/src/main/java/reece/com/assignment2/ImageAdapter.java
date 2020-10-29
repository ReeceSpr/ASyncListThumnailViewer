package reece.com.assignment2;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

/*
 * ImageAdapter for MainActivity's GridView
 */


public class ImageAdapter extends BaseAdapter {

    //Needed for MediaStore.
    private final Context mContext;
    //Used to hold thumbViews while they are not in view
    private final ArrayList<ThumbnailView> thumbnailViews;

    //Sets properties and calls addBitmaps.
    public ImageAdapter(Context mContext) {
        this.mContext = mContext;
        thumbnailViews = new ArrayList<>();
        addBitmaps();
    }

    //Returns the number of views
    @Override
    public int getCount() {
        return thumbnailViews.size();
    }

    //Returns the view given the position
    @Override
    public Object getItem(int position) {
        return thumbnailViews.get(position);
    }

    //Returns a views id given its positions.
    @Override
    public long getItemId(int position) {
        return thumbnailViews.get(position).getId();
    }

    //Returns the view to populate the gridView. View is told to set its image here.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<= thumbnailViews.size()) {
            ThumbnailView thumbnailView = thumbnailViews.get(position);
            thumbnailView.setThumbImage();
            return thumbnailView;
        }
        return null;
    }

    /*
     * Gets the MediaStore Cursor
     * Cursor has Images sorted by Date in Desc order.
     * Have commented through this function as it took a bit
     * for me to understand.
     * This still works in API 29 using "android:requestLegacyExternalStorage="true" in manifest.
     */
    private Cursor getMediaStoreCursor(){
        //orderBy - Access the imageColumn and add DESC so it is sorted in that order.
        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        //colReturn - What to return from the query.
        String [] colReturn={MediaStore.Images.Media.DATA,MediaStore.Images.Media.ORIENTATION};
        //Do the query and get a cursor that points to results.
        Cursor cursor = mContext.getContentResolver().query(
                // What Content Provider.
                // I thought to use INTERNAL_CONTENT_URI as a separate query
                // but when using this it got images stored in both SD+Internal
                // in my tests.
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                // What are we querying for.
                colReturn,

                // Using null returns all rows. If you wanted to get only some matching a cond,
                // you'd change this.
                null,

                // Arguments used in to carry out matching in previous argument.
                null,

                //Order the results by this column
                orderBy);
        return cursor;
    }

    /*
     * Creates the thumbnailViews
     * Uses getMediaStoreCursor()
     * Have commented through this function as it took a bit for me to understand. cont.
     */
    private void addBitmaps() {
        //Get Cursor
        Cursor cursor = getMediaStoreCursor();

        //Used to access the 2 columns returned by the query.
        int imagePathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int imageOrientationIndex = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);

        // If the cursor is empty, either 1. No Images, our app does nothing. 2. We didn't access the store (Permissions etc).
        // We should just return.
        if (!cursor.moveToFirst()) return;

        // Do while loop. Cursor is on first row and has at least one row, as tested above.
        // Keep going until all rows have been added.
        do{
            //Get orientation from the orientation row.
            int orientation = 0;
            orientation = cursor.getInt(imageOrientationIndex);

            // Create a ThumbView
            // Context, context given at constructor for calling super();
            // ImagePath from the imageColumn of this row.
            // Orientation given above.
            ThumbnailView thumbnailView = new ThumbnailView(mContext, cursor.getString(imagePathIndex),orientation);

            //Add the view to the list
            thumbnailViews.add(thumbnailView);
        }while(cursor.moveToNext());
        //Clean up the cursor.
        cursor.close();
    }
}

