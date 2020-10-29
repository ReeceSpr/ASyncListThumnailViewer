package reece.com.assignment2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    //Storage Permission Request Code
    private final static int MY_PERMISSIONS_REQUEST_STOREAGE = 1;
    //MediaStore Key for checking if store has changed
    private final static String MEDIASTORE_KEY = "MEDIASTORE_KEY";

    //The key to mediaStore we are currently on.
    private String currentMediaStoreVersion;



    //Creates the layout, sets the mediaStore version and calls checkPermissions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        currentMediaStoreVersion = MediaStore.getVersion(this);
    }

    /* SAME AS ASSIGNMENT 1
     * Callback after permissions activity resolves.
     * This function will either:
     * Proceed with the call if permission are accepted.
     * Explain to the user using Images requires permissions via Toast if permissions are declined.
     * Code as from official documentation.
     * https://developer.android.com/training/permissions/requesting
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STOREAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    addAdaptor();
                } else {
                    Toast.makeText(this, "Permissions Required To Show Images", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    /* SAME AS ASSIGNMENT 1
     * Checks permissions
     * If they are granted it calls addAdaptor
     * Else it requests permissions
     */
    private boolean checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_STOREAGE);
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            addAdaptor();
        }
        return true;
    }

    /*
     * Adds the adaptor to the GridView
     */
    private void addAdaptor(){
        GridView mGridView = (GridView) findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        mGridView.setAdapter(imageAdapter);
    }

    // Here I was considering saving the cache/position for use when activity is destroyed.
    // Due to the limited set of use, reopening the app without adding/removing files and time constraints, I did not pursue it.
    // Testing in other popular apps I do not think the position is saved.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(MEDIASTORE_KEY, currentMediaStoreVersion);
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.getString(MEDIASTORE_KEY)!=(MediaStore.getVersion(this))){
            //  Images Changed.
            //  Will reload anyway.
        } else {
            // We could load a cache/position in the gridView.
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    /*
     * If the user leaves the app and comes back, check if the mediaStore versions have changed.
     * If they have changed reload the adaptor.
     * Else just use our current adaptor.
     */
    @Override
    protected void onResume() {
        if ((currentMediaStoreVersion)!=(MediaStore.getVersion(this))){
            //Reload, images have changed
            addAdaptor();
        }
        super.onResume();
    }
}