package net.zhenglai.jvm;

/**
 * Created by Zhenglai on 8/8/16.
 * <p>
 * <p>
 * export CLASSPATH=.:target/classes/
 * java -cp $CLASSPATH -XX:+TraceClassLoading net.zhenglai.jvm.NotInitialization
 * <p>
 * <p>
 * <p>
 * 22:37:55 › java -cp $CLASSPATH -XX:+TraceClassLoading -XX:+PermSize=10M -XX:+MaxPermSize=10M net.zhenglai.jvm.NotInitialization
 * Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize=10M; support was removed in 8.0
 * Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=10M; support was removed in 8.0
 */

class ConstClass {
    static {
        System.out.println("Const class init");
    }

    public static final String HELLO = "hello";
}

class ArrayClass {
    static {
        System.out.println("ArrayClass init");
    }
}

public class NotInitialization {


    public static void main(String[] args) {
        // the constant is put into the constant pool of the NotInitialization class,
        // it has nothing to do with the ConstClass
        System.out.println(ConstClass.HELLO);

        Object[] array = new ArrayClass[12];
        System.out.println(array.getClass().getCanonicalName());
        System.out.println(array.getClass().getName());
        System.out.println(array.getClass().getSimpleName());
        System.out.println(array.getClass().getTypeName());
        /*
net.zhenglai.jvm.ArrayClass[]
[Lnet.zhenglai.jvm.ArrayClass;
ArrayClass[]
net.zhenglai.jvm.ArrayClass[]
         */
    }
}
