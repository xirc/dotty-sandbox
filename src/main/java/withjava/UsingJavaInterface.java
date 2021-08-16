package withjava;

public class UsingJavaInterface {

    interface Printer {
        String print(String message);
    }

    interface Scanner {
        default String scan() {
            return "hello";
        }
    }

}