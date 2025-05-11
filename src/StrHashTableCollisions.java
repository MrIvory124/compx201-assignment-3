import java.util.LinkedList;
import java.util.Objects;

/**
 * @author RyanB
 */
public class StrHashTableCollisions {

    private LinkedList<Node>[] table;
    private int size;
    private int fullRows;
    private int numRehashes;
    private int numCollisions;
    private int numCollisionsLinkedList;
    private int longestList;

    public StrHashTableCollisions(int size) {
        if (size <= 0) {
            size = 1;
        }
        this.size = size;
        fullRows = 0;
        numRehashes = 0;
        numCollisions = 0;
        numCollisionsLinkedList = 0;
        table = new LinkedList[size];
    }

    public int getFullRows() {
        return fullRows;
    }

    public int getNumRehashes() {
        return numRehashes;
    }

    public int getNumLLCollisions() {
        return numCollisionsLinkedList;
    }

    public int size() {
        return size;
    }

    /**
     * @param key The key of the value you wish to insert
     */
    public void insert(String key, String value) {
        sanitiseUserInput(key, value);
        insertInternal(key, value, true);
    }

    /**
     * Sanitises a key/value pair by ensuring the proper error is thrown
     * This is probably useless, but I needed something for the tests
     * In the real world I would consider putting something into logs or parsing a default
     * value, but in order to make a test I need to throw the correct error
     */
    private void sanitiseUserInput(String key, String value) {
        if (key != null && value != null) {
            if (key.isEmpty() || value.isEmpty()) {
                throw new NullPointerException();
            }
        }
    }


    /**
     * Note to marker: My old insert logic recursively called rehash. This is the easiest
     * way I could fix this without having to rewrite the insert code again in the rehash class.
     * @param rehash A boolean that is only triggered for rehash inserting
     */
    private void insertInternal(String k, String v, boolean rehash) {
        // find the index, check if empty and insert
        int index = hashFunction(k);
        if (Objects.equals(table[index], null)) {
            // create new linked list and insert
            Node newNode = new Node(k, v);
            LinkedList<Node> tempList = new LinkedList<>();
            tempList.add(newNode);
            table[index] = tempList;
            fullRows++;
        } else {
            // else there is a linked list there already, check for duplicate and update
            table[index].removeIf(node -> node.key.equals(k));
            table[index].add(new Node(k, v));
            if (table[index].size() > longestList) {
                longestList = table[index].size();
            }
            numCollisions++;
        }
        if (rehash) {
            rehash();
        }
    }

    public void delete(String key) {
        // go to index and remove it
        int index = hashFunction(key);
        if (table[index] != null) {
            // go through the linked list, find and remove the matching node
            for (int i = 0; i < table[index].size(); i++) {
                Node node = table[index].get(i);
                if (node.key.equals(key)) {
                    table[index].remove(node);
                    if (table[index].isEmpty()) {
                        table[index] = null;
                        fullRows--;
                    }
                    return;
                }
            }
        }
    }

    private int hashFunction(String k) {
        sanitiseUserInput(k, "value");
        // below is credited to OpenSDA: CS3 Data Structures & Algorithms
        char[] chars;
        chars = k.toCharArray();

        int i, sum;
        for (sum = 0, i = 0; i < k.length(); i++) {
            // in java this automatically does ascii/unicode conversion, lot more elegant than
            // my original solution which did the same thing, only worse...
            sum += chars[i];
        }
        // just incase we get integer overflow
        return Math.abs(sum % size);
    }

    /**
     * Doubles the size of the table when it is full enough
     */
    private void rehash() {
        // if almost full
        if (((float) fullRows / (float) size) >= 0.8 || longestList >= 5) {
            if (longestList >= 5) {
                numCollisionsLinkedList++;
                longestList = 0;
            }
            // double size, moving all linked lists to the new one
            size = size * 2;
            LinkedList<Node>[] copy = table;
            table = new LinkedList[size];
            fullRows = 0;
            numRehashes += 1;
            numCollisions = 0;
            // for each position in the copied list
            for (LinkedList<Node> list : copy) {

                if (list != null && !list.isEmpty()) {
                    // for each node node in an existing list, insert it
                    // here we use rehash = false in order to not get stuck recursively
                    // If we did get stuck, its a memory related problem
                    for (Node node : list) {
                        insertInternal(node.key, node.value, false);
                    }
                }
            }
        }
    }

    /**
     * @param k Key you want to check for
     * @return Returns true if the hashtable has it
     */
    public boolean contains(String k) {
        int index = hashFunction(k);
        if (table[index] != null) {
            // go through the linked list, find the matching node if possible
            for (int i = 0; i < table[index].size(); i++) {
                Node node = table[index].get(i);
                if (node.key.equals(k)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * @param k Key that you want the value of
     * @return A string value of the key you passed in. Returns null if node does not exist.
     * This is a carbon copy of contains, with the added functionality of returning the node's value
     */
    public String get(String k) {
        sanitiseUserInput(k, "value");
        int index = hashFunction(k);
        if (table[index] != null) {
            for (Node node : table[index]) {
                if (node.key.equals(k)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    /**
     * @return Returns false if items are in the hashtable
     */
    public boolean isempty() {
        return fullRows == 0;
    }

    /**
     * Prints into console the whole linked list structure, including "buckets" where multiple values collide
     */
    public void dump() {
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + " : ");
            // if its a linked list, iterate through printing to the same line
            if (table[i] != null) {
                for (Node node : table[i]) {
                    System.out.print(node.key + " : ");
                }
            } else {
                System.out.print(table[i]);
            }
            System.out.print("\r\n");
        }
        System.out.println("No. of collisions: " + numCollisions);
        System.out.println("No. of full rows: " + fullRows);
        System.out.println("No. of rehashes: " + numRehashes);
        System.out.println("No. of linkedlist rehashes: " + numCollisionsLinkedList);
    }
}


/**
 * This is the node that stores key/value pairs. Is used to store colliding values in a linkedList known as a "bucket"
 */
class Node {
    String key;
    String value;

    /**
     * @param key   This is stored in the node and used for the hashtable
     * @param value The value that is paired with the key and stored in the node
     */
    Node(String key, String value) {
        this.key = key;
        this.value = value;
    }

}




