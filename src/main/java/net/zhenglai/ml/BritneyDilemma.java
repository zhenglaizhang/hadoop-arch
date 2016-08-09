package net.zhenglai.ml;

import net.sf.classifier4J.ClassifierException;
import net.sf.classifier4J.vector.HashMapTermVectorStorage;
import net.sf.classifier4J.vector.TermVectorStorage;
import net.sf.classifier4J.vector.VectorClassifier;

import java.util.ArrayList;
import java.util.List;

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

        TermVectorStorage storage = new HashMapTermVectorStorage();
        VectorClassifier vc = new VectorClassifier(storage);
        String correctString = "britney spears";

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
