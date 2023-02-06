package org.example;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void parseGraph(String filePath) {
        try {
            File dot = new File(filePath);
            MutableGraph g = null;
            try {
                g = new Parser().read(dot);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File("example/ex4-2.png"));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void outputGraph(String filePath) {
        try {
            File dot = new File(filePath);
            MutableGraph g = null;
            try {
                g = new Parser().read(dot);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            Set<String> nodeSet = new HashSet<>();

            for (MutableNode node : g.nodes()) {
                nodeSet.add(node.name().toString());
            }

            System.out.println("Node List: (" + nodeSet.size() + " nodes)");
            for (String nodeName : nodeSet) {
                System.out.println(nodeName);
            }

            System.out.println("Edge List: (" + g.edges().size() +" edges)");
            for (Link edge : g.edges()) {
                System.out.println(edge.name());
            }

        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        parseGraph("src/main/java/org/example/color.dot");
        outputGraph("src/main/java/org/example/color.dot");
    }
}