package net.zhenglai.ml;

import net.sf.classifier4J.ClassifierException;
import net.sf.classifier4J.vector.HashMapTermVectorStorage;
import net.sf.classifier4J.vector.TermVectorStorage;
import net.sf.classifier4J.vector.VectorClassifier;

import java.util.ArrayList;
import java.util.List;

// export CLASSPATH=.:target/classes/:lib/net/sf/classifier4J/0.6/classifier4J-0.6.jar
// java -cp $CLASSPATH -XX:+TraceClassLoading -XX:+PermSize=10M -XX:+MaxPermSize=10M net.zhenglai.ml.BritneyDilemma
public class BritneyDilemma {
    public BritneyDilemma() {
        List<String> terms = new ArrayList<>();

        terms.add("brittany spears");
        terms.add("brittney spears");
        terms.add("britany spears");
        terms.add("britny spears");
        terms.add("briteny spears");
        terms.add("britteny spears");
        terms.add("briney spears");
        terms.add("brittny spears");
        terms.add("brintey spears");
        terms.add("britanny spears");
        terms.add("britiny spears");
        terms.add("britnet spears");
        terms.add("britiney spears");
        terms.add("christina aguilera");

        // The confi dence is always a number between 0 and 0.9999
        terms.add("britney spears");

        TermVectorStorage storage = new HashMapTermVectorStorage();
        VectorClassifier vc = new VectorClassifier(storage);
        String correctString = "britney spears";

        /*
        Classifer4J library to run a basic vector space search
on the incoming spellings of Britney; it then ranks them against the correct
string
         */
        for (String term : terms) {
            try {
                vc.teachMatch("sterm", correctString);
                double result = vc.classify("sterm", term);
                System.out.println(term + " = " + result);
            } catch (ClassifierException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new BritneyDilemma();
    }
}
