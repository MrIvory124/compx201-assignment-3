public class Main {
    public static void main(String[] args) {

        StrHashTable table = new StrHashTable(100);

        table.insert("Cat","A fluffy animal");
        table.insert("Doggiesaresocool","A fluffy animal");
        table.insert(null,"A fluffy animal");
        table.insert("Animal","");
    }
}