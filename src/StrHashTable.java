import java.util.Objects;

public class StrHashTable {

    private Node[] table;
    private int size;
    private int fullRows;
    private int numRehashes;
    private int numCollisions;

    public StrHashTable(int size) {
        this.size = size;
        table = new Node[size];
    }

    /**
     * @param k The key of the value you wish to insert
     */
    public void insert (String k, String v) {
        // find the index, check if empty and insert
        int index = hashFunction(k);
        if (Objects.equals(table[index], null)){
            Node newNode = new Node(k, v);
            table[index] = newNode;
            fullRows++;
        }
        else {
            // handle collisions
        }
    }

    public void delete(String k){
        // go to index and remove it
        int index = hashFunction(k);
        if (table[index] != null){
            table[index] = null;
            fullRows--;
        }
        else {
            // handle collisions
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
        if (fullness >= 0.8){
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
            //insert(x);
        }
        // TODO determine if below is needed, i think this bit is for seperate chaining
//        else {
//            System.out.println("Rehashing.");
//            int i = key;
//            while (table[i] != 0 && i < size){
//                i++;
//            }
//            if (i < size){
//                table[i] = x;
//              fullRows++;
//            }
        }

        public boolean contains(String k){
            int index = hashFunction(k);
            return Objects.equals(table[index].key, k);
        }

        public String get(String k){
            int index = hashFunction(k);
            return table[index].value;
        }

        public boolean isempty(){
            return fullRows == 0;
        }

        public int size(){
            return size;
        }

        public void dump(){
            for (int i = 0; i < table.length; i++) {
                System.out.println(i + " : " + table[i]);
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




