package net.zhenglai.jvm.lang;

import cascading.operation.assertion.AssertEquals;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Zhenglai on 8/8/16.
 */
public class LanaguageFeatureTest {

    @Test
    public void switchTest() {
        String s = "a";
        switch (s) {
            case "a":
                System.out.println("switch on string works");
                assertEquals(s, "a");
                break;
            default:
                break;  // ignore
        }
    }
}
