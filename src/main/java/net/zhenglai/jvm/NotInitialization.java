package net.zhenglai.jvm;

/**
 * Created by Zhenglai on 8/8/16.
 *
 *
 * export CLASSPATH=.:target/classes/
 * java -cp $CLASSPATH -XX:+TraceClassLoading net.zhenglai.jvm.NotInitialization
 */
public class NotInitialization {

    static class ConstClass {
        static {
            System.out.println("Const class init");
        }

        public static final String HELLO = "hello";
    }

    public static void main(String[] args) {
        // the constant is put into the constant pool of the NotInitialization class,
        // it has nothing to do with the ConstClass
        System.out.println(ConstClass.HELLO);
    }
}
