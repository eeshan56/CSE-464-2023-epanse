package org.example;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.node;

public class Main {
    public static void main(String[] args) {
        GraphManager graphManager = new GraphManager("src/main/java/org/example/color.dot");
        // graphManager.addNode("Adam");
        // graphManager.parseGraph();

        /*
        List<List<String>> edgeList = new ArrayList<>();
        for(Integer i = 0; i < 1; i ++) {
            List<String> edgeGroup = new ArrayList<>();
            edgeGroup.add("Eve");
            edgeGroup.add("Adam");

            edgeList.add(edgeGroup);
        }
        graphManager.addEdges(edgeList);
        */

        graphManager.removeNode("white");

        List<String> nodeNames = new ArrayList<>();
        nodeNames.add("black");
        nodeNames.add("white");
        graphManager.removeNodes(nodeNames);
        graphManager.parseGraph();
        graphManager.outputGraph();
    }
}