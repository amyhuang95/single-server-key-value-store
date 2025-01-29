import java.util.HashMap;
import java.util.Map;

/**
 * Class represents a key-value store using Hashmap. Implements 3 operations: PUT, GET, and DELETE.
 */
public class KVStore {
    private Map<String, String> store;

    public KVStore() {
        store = new HashMap<>();
    }

    public void put(String key, String value) {
        store.put(key, value);
    }
    public String get(String key) {
        return store.get(key);
    }
    public void delete(String key) {
        store.remove(key);
    }
}
