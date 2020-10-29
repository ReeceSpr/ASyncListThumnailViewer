package reece.com.assignment2;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;

/*
 * Cache's image's
 * Uses singletonPattern so it can be referenced when app starts to cut down load time when MEDIASTORE hasn't changed <TO-DO>
 * The doc is used for this class.
 * https://developer.android.com/topic/performance/graphics/cache-bitmap
 */

public class CacheSingleton<String, Bitmap> extends LruCache {

    // Private instance - Allows us to prevent the cache being created more then once
    // Static - Allows us to getCache wherever this calls is imported.
    private static CacheSingleton cache;

    // Returns Cache or creates it if it has not been initialised.
    public static CacheSingleton getCache(){
        if (cache == null) {
            cache = new CacheSingleton<>((int) Runtime.getRuntime().maxMemory()/1024);
        }
        return cache;
    }

    /*
     *Creates cache
     * Takes total memory size and create cache of size memorySize/8
     */
    private CacheSingleton(int maxSize) {
        super(maxSize/8);
    }

}
