package javacode.org.example;

import guru.nidi.graphviz.model.MutableNode;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<String> nodes;

    public Path() {
        this.nodes = new ArrayList<>();
    }

    public void addToPath(MutableNode newNode) {
        this.nodes.add(newNode.name().toString());
    }

    public List<String> getPath() {
        return this.nodes;
    }
}