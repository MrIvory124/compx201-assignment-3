public class Main {
    public static void main(String[] args) {

        // for the most part this main is not used, the tests file is instead

       String k = "key1";
        char[] chars;
        chars = k.toCharArray();

        int i, sum;
        for (sum=0, i=0; i < k.length(); i++) {
            sum += chars[i];
        }
        System.out.println(sum % 10);
    }
}