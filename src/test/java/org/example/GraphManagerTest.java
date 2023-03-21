package org.example;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableNode;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphManagerTest {

    @Test
    void testIsDirected() {
        GraphManager graphManager = new GraphManager();
        Boolean result = graphManager.isDirected();
        assertEquals(false, result);

        graphManager = new GraphManager("src/main/java/org/example/color.dot");
        result = graphManager.isDirected();
        assertEquals(true, result);

        graphManager = new GraphManager("src/main/java/org/example/undirectedColor.dot");
        result = graphManager.isDirected();
        assertEquals(false, result);
    }

    @Test
    void testGetGraph() {
        GraphManager graphManager = new GraphManager();
        graphManager.getGraph("src/main/java/org/example/color.dot");

        List<String> nodeNames = List.of("white", "cyan", "pink", "yellow", "blue", "green", "red", "black");
        List<MutableNode> nodeList = graphManager.getNodeSet();

        for(MutableNode node : nodeList) {
            Boolean isFound = false;
            for(String nodeName : nodeNames) {
                if(nodeName.equals(node.name().toString())) {
                    isFound = true;
                }
            }

            assert isFound;
        }

        Boolean excReached = false;
        try {
            graphManager.getGraph("ex4-3.png");
        }
        catch (RuntimeException exc) {
            excReached = true;
        }

        assertEquals(true, excReached);

        excReached = false;
        try {
            graphManager.getGraph("example/ex4-3.png");
        }
        catch (RuntimeException exc) {
            excReached = true;
        }

        assertEquals(true, excReached);
    }

    @Test
    public void testInitGraphWithDefaultFilePath() {
        String input = "y";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        GraphManager graphManager = new GraphManager();
        graphManager.initGraph();
        assertEquals("src/main/java/org/example/color.dot", graphManager.getDotFilePath());
    }

    @Test
    public void testInitGraphWithCustomFilePath() {
        String input = "n\nsrc/main/java/org/example/undirectedColor.dot";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        GraphManager graphManager = new GraphManager();
        graphManager.initGraph();
        assertEquals("src/main/java/org/example/undirectedColor.dot", graphManager.getDotFilePath());
    }
    @Test
    public void testParseGraphWithPNG() {
        GraphManager graph = new GraphManager();
        graph.parseGraph("png");
        File outputFile = new File("example/ex4-2.png");
        assertTrue(outputFile.exists());
    }

    @Test
    public void testParseGraphWithJPEG() {
        GraphManager graph = new GraphManager();
        graph.parseGraph("jpeg");
        File outputFile = new File("example/ex4-1.jpeg");
        assertTrue(outputFile.exists());
    }

    @Test
    void testOutputGraph() {
        GraphManager graph = new GraphManager();
        String input = "y";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        graph.initGraph();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        graph.outputGraph();

        String[] actualOutput = outContent.toString().split(System.lineSeparator());
        Set<String> actualNodeSet = new HashSet<>(Arrays.asList(actualOutput).subList(1, 9));
        Set<String> expectedNodeSet = new HashSet<>(Arrays.asList("white", "cyan", "pink", "yellow", "blue", "green", "red", "black"));
        String[] actualEdgeList = Arrays.copyOfRange(actualOutput, 10, actualOutput.length);
        String[] expectedEdgeList = new String[]{"green--black", "white--pink", "white--cyan", "cyan--green", "yellow--red", "white--yellow", "pink--blue", "yellow--green", "pink--red", "blue--black", "red--black", "cyan--blue"};
        assertEquals(expectedNodeSet, actualNodeSet);
        assertEquals(Arrays.toString(expectedEdgeList), Arrays.toString(actualEdgeList));

        List<MutableNode> nodeList = graph.getNodeSet();
        assertEquals(8, nodeList.size());

        List<Link> edgeList = graph.getEdgeList();
        assertEquals(12, edgeList.size());
    }

    @Test
    void testAddNode() {
        GraphManager graph = new GraphManager();
        graph.addNode("newNode");

        List<MutableNode> nodeSet = graph.getNodeSet();
        assertEquals(1, nodeSet.size());
        assertEquals("newNode", nodeSet.get(0).name().toString());
    }

    @Test
    void testAddEdges() {
        GraphManager graphCreator = new GraphManager();

        // Add some nodes
        graphCreator.addNode("A");
        graphCreator.addNode("B");
        graphCreator.addNode("C");

        // Add some edges
        List<List<String>> edgeList = new ArrayList<>();
        edgeList.add(List.of("A", "B"));
        edgeList.add(List.of("B", "C"));
        edgeList.add(List.of("C", "A"));
        graphCreator.addEdges(edgeList);

        assertEquals(3, graphCreator.getEdgeList().size());
    }

    @Test
    void testRemoveNode() {
        GraphManager graphCreator = new GraphManager();
        List<String> nodes = List.of("A", "B", "C", "D");
        graphCreator.addNodes(nodes);
        List<List<String>> edges = List.of(List.of("A", "B"), List.of("B", "C"), List.of("C", "D"), List.of("D", "A"));
        graphCreator.addEdges(edges);

        List<MutableNode> nodeList = graphCreator.getNodeSet();
        assertEquals(nodes.size(), nodeList.size());
        for(String nodeName : nodes) {
            boolean containsNode = false;
            for(MutableNode node : nodeList) {
                if(node.name().toString().equals(nodeName)) {
                    containsNode = true;
                    break;
                }
            }
            assertTrue(containsNode);
        }

        ArrayList<Link> edgeList = graphCreator.getEdgeList();
        assertEquals(edges.size(), edgeList.size());
        for(List<String> edge : edges) {
            boolean containsEdge = false;
            for(Link link : edgeList) {
                String[] linkSplit = link.name().toString().split("--");
                if(linkSplit[0].equals(edge.get(0)) && linkSplit[1].equals(edge.get(1))) {
                    containsEdge = true;
                    break;
                }
            }
            assertTrue(containsEdge);
        }

        graphCreator.removeNode("C");

        nodeList = graphCreator.getNodeSet();
        assertEquals(nodes.size() - 1, nodeList.size());
        for(String nodeName : nodes) {
            if(!nodeName.equals("C")) {
                boolean containsNode = false;
                for(MutableNode node : nodeList) {
                    if(node.name().toString().equals(nodeName)) {
                        containsNode = true;
                        break;
                    }
                }
                assertTrue(containsNode);
            }
        }

        edgeList = graphCreator.getEdgeList();
        assertEquals(edges.size() - 2, edgeList.size());
        for(List<String> edge : edges) {
            if(!edge.get(0).equals("C") && !edge.get(1).equals("C")) {
                boolean containsEdge = false;
                for(Link link : edgeList) {
                    String[] linkSplit = link.name().toString().split("--");
                    if(linkSplit[0].equals(edge.get(0)) && linkSplit[1].equals(edge.get(1))) {
                        containsEdge = true;
                        break;
                    }
                }
                assertTrue(containsEdge);
            }
        }
    }

    @Test
    void addNodes() {
        GraphManager graph = new GraphManager();
        List<String> nodeNames = Arrays.asList("A", "B", "C", "D");
        graph.addNodes(nodeNames);
        List<MutableNode> nodes = graph.getNodeSet();

        assertEquals(4, nodes.size());
        assertTrue(nodes.stream().allMatch(node -> nodeNames.contains(node.name().toString())));
    }

    @Test
    void removeNodes() {
        GraphManager graph = new GraphManager();
        List<String> nodeNames = Arrays.asList("A", "B", "C", "D");
        graph.addNodes(nodeNames);
        graph.removeNodes(Arrays.asList("A"));
        List<MutableNode> nodes = graph.getNodeSet();

        assertEquals(3, nodes.size());
        assertTrue(nodes.stream().allMatch(node -> nodeNames.contains(node.name().toString())));
    }

    @Test
    void testOutputToDot() {
        String input = "y";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        GraphManager graph = new GraphManager();
        graph.initGraph();

        try {
            graph.outputToDot();
        }
        catch (IOException e) {
            System.out.println("Something's wrong...");
            e.printStackTrace();
        }
        File file = new File("example/output.dot");
        assertTrue(file.exists());
    }
}