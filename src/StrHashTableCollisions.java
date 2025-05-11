import java.util.LinkedList;
import java.util.Objects;

public class StrHashTableCollisions {

    private LinkedList<Node>[] table;
    private int size;
    private int fullRows;
    private int numRehashes;
    private int numCollisions;

    public StrHashTableCollisions(int size) {
        this.size = size;
        table = new LinkedList[size];
    }

    /**
     * @param k The key of the value you wish to insert
     */
    public void insert (String k, String v) {
        insertInternal(k,v,true);
    }


    /**
     * Note to marker: My old insert logic recursively called rehash. This is the easiest
     * way I could fix this without having to rewrite the insert code again in the rehash class.
     * @param rehash A boolean that is only triggered for rehash inserting
     */
    private void insertInternal (String k, String v, boolean rehash) {
        // find the index, check if empty and insert
        int index = hashFunction(k);
        if (Objects.equals(table[index], null)){
            // create new linked list and insert
            Node newNode = new Node(k, v);
            LinkedList<Node> tempList = new LinkedList<>();
            tempList.add(newNode);
            table[index] = tempList;
            fullRows++;
        }
        else {
            // else there is a linked list there already, just insert it
            table[index].add(new Node(k, v));
            numCollisions++;
        }
        if (rehash) { rehash(); }

    }

    public void delete(String k){
        // go to index and remove it
        int index = hashFunction(k);
        LinkedList<Node> tempList = table[index];
        if (tempList != null){
            // go through the linked list, find and remove the matching node
            for (int i = 0; i < tempList.size(); i++) {
                Node node = tempList.get(i);
                if (node.key.equals(k)) { tempList.remove(node); return; }
            }
            // check if anymore items in the linked list, and remove
            if (tempList.isEmpty()){
                table[index] = null;
                fullRows--;
            }

        }
    }

    private int hashFunction(String k){
        int sum = 0;
        int len = k.length();
        for (int i = 0; i < len; i += 3) {
            // each of these queries if there is a number, if not set it to 0
            int a = (i < len ? k.charAt(i)   : 0);
            int b = (i+1 < len ? k.charAt(i+1) : 0);
            int c = (i+2 < len ? k.charAt(i+2) : 0);
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
    private void rehash(){
        float fullness = (float) fullRows / size;
        // if almost full
        if (fullness >= 0.8){
            // double size, moving all linked lists to the new one
            System.out.println("Resizing");
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
                    for (Node node : list) {
                        insertInternal(node.key, node.value, false);
                    }
                }
            }
        }
    }

    public boolean contains(String k) {
        int index = hashFunction(k);
        LinkedList<Node> tempList = table[index];
        if (tempList != null) {
            // go through the linked list, find and remove the matching node
            for (int i = 0; i < tempList.size(); i++) {
                Node node = tempList.get(i);
                if (node.key.equals(k)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String get(String k) {
        int index = hashFunction(k);
        LinkedList<Node> list = table[index];
        if (list != null) {
            for (Node node : list) {
                if (node.key.equals(k)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    public boolean isempty(){
        return fullRows == 0;
    }

    public int size(){
        return size;
    }

    public void dump(){
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                for (Node node : table[i]) {
                    System.out.println(node.key);
                }
            }else {
                System.out.println(i + " : " + table[i]);
            }
        }
        System.out.println("No. of collisions: " + numCollisions);
        System.out.println("No. of full rows: " + fullRows);
        System.out.println("No. of rehashes: " + numRehashes);
    }
}




class Node{
    String key;
    String value;

    /**
     * @param key This is stored in the node and used for the hashtable
     * @param value The value that is paired with the key and stored in the node
     */
    Node(String key, String value){
        this.key = key;
        this.value = value;
    }

}




