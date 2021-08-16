package withjava;

import java.util.ArrayList;
import java.util.List;

public class UsingJavaCollection {
    public static List<String> getStringList() {
        return new ArrayList<String>(List.of("a", "b", "c"));
    }
}
