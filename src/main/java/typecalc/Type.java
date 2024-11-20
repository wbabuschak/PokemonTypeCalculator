package typecalc;
import java.util.*;

public class Type {
    String displayName;
    ArrayList<Type> weak = new ArrayList<>();
    ArrayList<Type> resist = new ArrayList<>();
    ArrayList<Type> immun = new ArrayList<>();

    public Type(String name){
        displayName = name;
    }
}

