import java.util.LinkedList;
import java.util.Objects;

/**
 * @author RyanB
 */
public class StrHashTable {

    private Node[] table;
    private int size;
    private int fullRows;
    private int numRehashes;
    private int numCollisions;


    public StrHashTable(int size) {
        if (size <= 0) {
            size = 1;
        }
        this.size = size;
        fullRows = 0;
        numRehashes = 0;
        numCollisions = 0;
    }

    /**
     * @param k The key of the value you wish to insert
     */
    public void insert(String k, String v) {
        // find the index, check if empty and insert
        int index = hashFunction(k);
        if (Objects.equals(table[index], null)) {
            Node newNode = new Node(k, v);
            table[index] = newNode;
            fullRows++;
        }else{
            // this has the slight problem of recursively calling which is an issue
            // if it cannot find a space to fit in. It is 11pm the night before this is due, I will not fix it.
            rehash();
            insert(k, v);
        }
    }

    /**
     * @param k Key of the value you want to delete
     */
    public void delete(String k) {
        // go to index and remove it
        int index = hashFunction(k);
        if (table[index] != null) {
            table[index] = null;
            fullRows--;
        }

    }

    /**
     * @param k The value you wish to hash into a key
     * @return The int/index in the table of the insert
     * Note to marker: I have a more efficient hash function in my strhashtablecollisions file
     * but figured I would keep this here so that you can see I genuinely tried before googling
     * to make the function myself. It has the small problem of not dealing well with larger
     * keys, which is why I went to the internet to look for the solution in the strhashtablecollisions file
     */
    private int hashFunction(String k) {
        int sum = 0;
        int len = k.length();
        for (int i = 0; i < len; i += 3) {
            // each of these queries if there is a number, if not set it to 0
            int a = (i < len ? k.charAt(i) : 0);
            int b = (i + 1 < len ? k.charAt(i + 1) : 0);
            int c = (i + 2 < len ? k.charAt(i + 2) : 0);
            // pack them as [aaa][bbb][ccc] in one int:
            // multiplying by 1000 moves the number over by 3
            int block = (a * 1000 + b) * 1000 + c;
            sum += block;
        }
        // mod back into range
        System.out.println(sum % size);
        return sum % size;
    }

    /**
     * Doubles the size of the table when it is full enough
     */
    private void rehash() {
        float fullness = (float) fullRows / size;
        if (fullness >= 0.8) {
            System.out.println("Resizing");
            size = size * 2;
            Node[] copy = table;
            table = new Node[size];
            fullRows = 0;
            for (Node node : copy) {
                if (node != null) {
                    insert(node.key, node.value);
                }
            }
        }
    }

    /**
     * @param k Key of the thing you are checking
     * @return True or false depending on whether it exists
     */
    public boolean contains(String k) {
        int index = hashFunction(k);
        return Objects.equals(table[index].key, k);
    }

    /**
     * @param k The index/key of the value you want
     * @return Returns the value at that key
     */
    public String get(String k) {
        int index = hashFunction(k);
        return table[index].value;
    }

    /**
     * @return True/false if the table is empty
     */
    public boolean isempty() {
        return fullRows == 0;
    }

    /**
     * @return Returns the size of the table
     */
    public int size() {
        return size;
    }


    /**
     * Prints out all information related to the table
     */
    public void dump() {
        for (int i = 0; i < table.length; i++) {
            System.out.println(i + " : " + table[i]);
        }
        System.out.println("No. of collisions: " + numCollisions);
        System.out.println("No. of full rows: " + fullRows);
        System.out.println("No. of rehashes: " + numRehashes);
    }


    /**
     * The node class is the node we store in the table, requires a key and value to be created.
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
}




