package Examples;

import java.util.ArrayList;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;



class Cache<K, T> {

    private long timeToLive;
    private LRUMap CacheMap;

    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    public Cache(long TimeToLive, final long TimerInterval, int maxItems) {
        this.timeToLive = TimeToLive * 1000;

        CacheMap = new LRUMap(maxItems);

        if (timeToLive > 0 && TimerInterval > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(TimerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value) {
        synchronized (CacheMap) {
            CacheMap.put(key, new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (CacheMap) {
            CacheObject c = (CacheObject) CacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (CacheMap) {
            CacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (CacheMap) {
            return CacheMap.size();
        }
    }

    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (CacheMap) {
            MapIterator itr = CacheMap.mapIterator();

            deleteKey = new ArrayList<K>((CacheMap.size() / 2) + 1);
            K key = null;
            CacheObject c = null;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (CacheObject) itr.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (CacheMap) {
                CacheMap.remove(key);
            }

            Thread.yield();
        }
    }
}
public class CacheDemo {

    public static void main(String[] args) throws InterruptedException {

        CacheDemo cacheDemo = new CacheDemo();

        System.out.println("\n\n==========Test1: TestAddRemoveObjects ==========");
        cacheDemo.TestAddRemoveObjects();
        System.out.println("\n\n==========Test2: TestExpiredCacheObjects ==========");
        cacheDemo.TestExpiredCacheObjects();
        System.out.println("\n\n==========Test3: TestObjectsCleanupTime ==========");
        cacheDemo.TestObjectsCleanupTime();
    }

    private void TestAddRemoveObjects() {


        Cache<String, String> cache = new Cache<String, String>(200, 500, 6);

        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");
        cache.put("Google", "Google");
        cache.put("Microsoft", "Microsoft");
        cache.put("IBM", "IBM");
        cache.put("Facebook", "Facebook");

        System.out.println("6 Cache Object Added.. cache.size(): " + cache.size());
        cache.remove("IBM");
        System.out.println("One object removed.. cache.size(): " + cache.size());

        cache.put("Twitter", "Twitter");
        cache.put("SAP", "SAP");
        System.out.println("Two objects Added but reached maxItems.. cache.size(): " + cache.size());

    }

    private void TestExpiredCacheObjects() throws InterruptedException {


        Cache<String, String> cache = new Cache<String, String>(1, 1, 10);

        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");

        Thread.sleep(3000);

        System.out.println("Two objects are added but reached timeToLive. cache.size(): " + cache.size());

    }

    private void TestObjectsCleanupTime() throws InterruptedException {
        int size = 500000;


        Cache<String, String> cache = new Cache<String, String>(100, 100, 500000);

        for (int i = 0; i < size; i++) {
            String value = Integer.toString(i);
            cache.put(value, value);
        }

        Thread.sleep(200);

        long start = System.currentTimeMillis();
        cache.cleanup();
        double finish = (double) (System.currentTimeMillis() - start) / 1000.0;

        System.out.println("Cleanup times for " + size + " objects are " + finish + " s");

    }
}
