package rish.crearo.tools;

public class Printer {

    private static boolean bool = true;

    public static void println(String a) {
        if(bool)
            System.out.println(a);
    }

    public static void println(){
        if (bool) {
            System.out.println();
        }
    }
}
