package net.zhenglai.jvm.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
