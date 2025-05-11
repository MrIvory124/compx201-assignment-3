import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;


class Tests {
    private StrHashTableCollisions hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new StrHashTableCollisions(10);
    }

    @Test
    @DisplayName("Hash table size")
    void sizeHashTable() {
        assertEquals(10, hashTable.size());
    }

    @Test
    @DisplayName("Regular insert")
    void insertHashTable() {
        hashTable.insert("key1", "value1");
        assertEquals("value1", hashTable.get("key1"));
    }

    @Test
    @DisplayName("Constructor normalizes size 0 to 1")
    void sizeOneHashTable() {
        hashTable = new StrHashTableCollisions(0);
        assertEquals(1, hashTable.size());
    }

    @Test
    @DisplayName("Constructor normalizes negative size to 1")
    void sizeZeroHashTable() {
        hashTable = new StrHashTableCollisions(-1);
        assertEquals(1, hashTable.size());
    }

    @Test
    @DisplayName("Insert with null key throws NPE")
    void insertNull() {
        assertThrows(NullPointerException.class, () -> hashTable.insert(null, null));
    }

    @Test
    @DisplayName("Delete of 1 value after insertion")
    void regularDelete() {
        hashTable.insert("key1", "3");
        hashTable.insert("randomstring1", "3");
        hashTable.delete("key1");
        assertFalse(hashTable.contains("3"));
        // I can ensure the below test works as designed because of the beforeeach condition
        // that we use a fresh table, this would not pass if it wasn't fresh
        assertEquals(1, hashTable.getFullRows());
    }

    @Test
    @DisplayName("Test proper collision handling")
    void collisionHandling() {
        // insert 2 values that calculate to the same position
        // check that full rows is 1
        // check both things are in the table
        hashTable.insert("aaa", "value1");
        hashTable.insert("aak", "value2");
        assertEquals(1, hashTable.getFullRows());
        assertEquals("value2", hashTable.get("aak"));
    }

    @Test
    @DisplayName("Check overwrite of duplicate keys")
    void deleteDuplicate() {
        // the below insertions ensure we are getting a collision
        hashTable.insert("key1", "3");
        hashTable.insert("key1", "2");
        assertEquals("2", hashTable.get("key1"));
    }

    @Test
    @DisplayName("Insert with empty string throws NPE")
    void insertEmptyStrings() {
        assertThrows(NullPointerException.class, () -> hashTable.insert("", ""));
    }

    @Test
    @DisplayName("Insert large key/value pair")
    void insertLargeKeyValuePair() {
        hashTable.insert("keyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy1", "value1");
        assertTrue(hashTable.contains("keyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy1"));
    }

    @Test
    @DisplayName("Rehash triggers when 0.8 full")
    void rehashFull() {
        // below is an array of many different keys to ensure minimal collisions
        String[] keys = {"apple", "banana", "carrot", "delta", "echo", "fig", "grape", "hazel", "iris", "jade", "kiwi", "lemon", "mango", "nectar", "olive"};
        for (String key : keys) {
            hashTable.insert(key, "value");
        }
        assertEquals(1, hashTable.getNumRehashes());
    }

    // TODO this test seems redundant
    @Test
    @DisplayName("Delete with 0 items in there")
    void deleteZeroItems() {
        hashTable.delete("key");
        assertEquals(0, hashTable.getFullRows());
    }

    @Test
    @DisplayName("Testing isEmpty with no elements")
    void isEmptyWithEmptyTable() {
        assertTrue(hashTable.isempty());
    }

    @Test
    @DisplayName("Testing isEmpty with with elements")
    void isEmptyWithFullTable() {
        hashTable.insert("key", "value");
        assertFalse(hashTable.isempty());
    }

    @Test
    @DisplayName("Testing contains with 0 items")
    void containsZeroItems() {
        assertFalse(hashTable.contains("key"));
    }

    @Test
    @DisplayName("Rehash when linked list gets too long")
    void rehashLinkedListTooLong() {
        // this produces keys that end up in the same linked list
        // mod in this case is 10, so all I had to do was fine the ascii numbers
        // that represented actual characters, 41 = ), 51 = 3 etc.
        String[] keys = {")", "3", "=", "G", "Q", "["};
        for (String key : keys) {
            hashTable.insert(key, "value");
        }
        hashTable.dump();
        assertEquals(1, hashTable.getNumLLCollisions());
    }

    @Test
    @DisplayName("Ensure Get doesn't fail")
    void hashtableFetchingNoVal() {
        String[] keys = {")", "3", "=", "G", "Q", "["};
        for (String key : keys) {
            hashTable.insert(key, "value");
        }
        assertNull(hashTable.get("F"));
    }

    @Test
    @DisplayName("Get fetching correct value")
    void hashTableFetchingCorrectVal() {
        String[] keys = {")", "3", "=", "G", "Q", "["};
        for (String key : keys) {
            hashTable.insert(key, "value" + key);
        }
        assertEquals("valueG", hashTable.get("G"));
    }

    // sidenote here, thanks for teaching us testing
    // genuinely found bugs in my code doing this

}
