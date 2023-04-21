package javacode.org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.util.*;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.parse.Parser;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GraphManager {
    public static final String DEFAULT_DOT_FILEPATH = "src/main/java/org/example/color.dot";
    public static final String REGEX_UNDIRECTED_CONNECTOR = "--";
    public static final String REGEX_DIRECTED_CONNECTOR = "-->";
    public static final String JPG_STR = "jpg";
    public static final String PNG_STR = "png";
    public static final String JPEG_STR = "jpeg";

    public static final String OUTPUT_DIR = "example/";
    public static final String DEFAULT_PNG_DIRECTED_FILENAME = "ex4-2";
    public static final String DEFAULT_PNG_UNDIRECTED_FILENAME = "ex4-3";
    public static final String DEFAULT_JPEG_FILENAME = "ex4-1";
    public static final String DEFAULT_OUTPUT_DOT_FILEPATH = OUTPUT_DIR + "output.dot";
    public static final String BFS_STR = "bfs";
    public static final String DFS_STR = "dfs";

    private MutableGraph graph = null;
    private String dotFilePath = null;

    public GraphManager() {
        this.graph = mutGraph();
        this.dotFilePath = "";
    }

    public GraphManager(String dotFilePath) {
        this.getGraph(dotFilePath);
        this.dotFilePath = dotFilePath;
    }

    /**
     * Determines whether the current graph is directed or not
     * @return Boolean
     */
    public Boolean isDirected() {
        return (this.graph == null || this.dotFilePath == null ) ? false : this.graph.isDirected();
    }
    /**
     * Creates a graph of the corresponding dot file whose filepath is passed to it
     * @param dotFilePath The file path of the dot file to be parsed
     * @return void
     */
    public void getGraph(String dotFilePath) {
        try {
            this.graph = new Parser().read(new File(dotFilePath));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the graph in case it hasn't already been initialized
     * @return void
     */
    public void initGraph() {

        System.out.println("Default filepath of the dot file is '" + DEFAULT_DOT_FILEPATH + "'");
        System.out.print("Do you want to continue with the default dot file? (Enter y/n): ");
        Scanner sc = new Scanner(System.in);
        String resp = sc.nextLine();

        if(resp.equals("y") || resp.equals("Y")) {
            this.dotFilePath = DEFAULT_DOT_FILEPATH;
        }
        else {
            System.out.println("Please provide the filepath of the dot file that contains the graph config: ");
            this.dotFilePath = sc.nextLine();
        }
        this.getGraph(this.dotFilePath);
    }

    /**
     * Exports the graph to a PNG image
     * @return void
     */
    public void parseGraph(String fileType) {
        try {
            String defaultPngFilename = this.isDirected() ? OUTPUT_DIR + DEFAULT_PNG_DIRECTED_FILENAME + "." + PNG_STR : OUTPUT_DIR + DEFAULT_PNG_UNDIRECTED_FILENAME + "." + PNG_STR;
            String tempPath = fileType.toLowerCase().equals(PNG_STR) ? defaultPngFilename : OUTPUT_DIR + "temp." + PNG_STR;
            Graphviz.fromGraph(this.graph).width(700).render(Format.PNG).toFile(new File(tempPath));
            if(fileType.toLowerCase().equals(JPG_STR) || fileType.toLowerCase().equals(JPEG_STR)) {
                BufferedImage pngImage = ImageIO.read(new File(tempPath));
                BufferedImage newImage = new BufferedImage( pngImage.getWidth(), pngImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newImage.createGraphics().drawImage( pngImage, 0, 0, Color.BLACK, null);
                ImageIO.write(newImage, JPG_STR, new File(OUTPUT_DIR + DEFAULT_JPEG_FILENAME + "." + fileType));
                File tempFile = new File(tempPath);
                tempFile.delete();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints the nodes and edges in the graph
     * @return void
     */
    public void outputGraph() {
        this.checkIfGraphIsNull();

        Set<String> nodeSet = new HashSet<>();

        for (MutableNode currNode : this.graph.nodes()) {
            nodeSet.add(currNode.name().toString());
        }

        System.out.println("Node List: (" + nodeSet.size() + " nodes)");
        for (String nodeName : nodeSet) {
            System.out.println(nodeName);
        }

        this.getEdgeList();
    }

    /**
     * Adds a node to the corresponding graph
     * @param nodeName The name of the node
     * @return void
     */
    public int addNode(String nodeName) {
        this.checkIfGraphIsNull();

        Set<String> nodeSet = new HashSet<>();
        boolean isDuplicate = false;
        for (MutableNode currNode : this.graph.nodes()) {
            nodeSet.add(currNode.name().toString());
            isDuplicate = (!isDuplicate) ? currNode.name().toString().equals(nodeName) : true;
        }

        if(isDuplicate) {
            System.out.println("Node " + nodeName + " is already present in the graph");
            return 1;
        }

        this.graph.add(mutNode(nodeName).asLinkSource());
        return 0;
    }

    /**
     * Adds edges between a list of nodes to the graph
     * @param edgeList The list of nodes between which an edge is needed
     * @return void
     */
    public void addEdges(List<List<String>> edgeList) {
        this.checkIfGraphIsNull();

        Map<String, MutableNode> nameToNodeMap = new HashMap<>();
        for(MutableNode currNode : this.graph.nodes()) {
            if(!nameToNodeMap.containsKey(currNode.name())) {
                nameToNodeMap.put(String.valueOf(currNode.name()), currNode);
            }
        }

        Scanner sc = new Scanner(System.in);

        for (List<String> edgeGroups : edgeList) {
            String nodeName1 = edgeGroups.get(0);
            String nodeName2 = edgeGroups.get(1);

            if(!nameToNodeMap.containsKey(nodeName1)) {
                System.out.println(nodeName1 + " is not present in the graph. \nDo you want to add it? (Enter y or n)");
                String resp = sc.nextLine();
                if(resp.equals("y")) {
                    this.addNode(nodeName1);
                }
                else {
                    System.out.println("Exiting...");
                    return;
                }
            }

            if(!nameToNodeMap.containsKey(nodeName2)) {
                System.out.println(nodeName2 + " is not present in the graph. \nDo you want to add it? (Enter y or n)");
                String resp = sc.nextLine();
                if(resp.equals("y")) {
                    this.addNode(nodeName2);
                }
                else {
                    System.out.println("Exiting...");
                    return;
                }
            }
        }

        nameToNodeMap = new HashMap<>();
        for(MutableNode currNode : this.graph.nodes()) {
            if(!nameToNodeMap.containsKey(currNode.name())) {
                nameToNodeMap.put(String.valueOf(currNode.name()), currNode);
            }
        }

        for(List<String> edgeGroups : edgeList) {
            MutableNode node1 = nameToNodeMap.get(edgeGroups.get(0));
            MutableNode node2 = nameToNodeMap.get(edgeGroups.get(1));

            Boolean isDuplicate = false;
            for(Link link : node1.links()) {
                List<String> linkSplit = List.of(link.name().toString().split(REGEX_UNDIRECTED_CONNECTOR));
                if(linkSplit.get(0).equals(node2.name().toString()) || linkSplit.get(1).equals(node2.name().toString())) {
                    isDuplicate = true;
                }
            }

            if(!isDuplicate) {
                node1.addLink(node2);
            }
            else {
                String connector = (this.graph.isDirected()) ? REGEX_DIRECTED_CONNECTOR : REGEX_UNDIRECTED_CONNECTOR;
                System.out.println(node1.name() + connector + node2.name() + " is already present in the graph.");
            }
        }
    }

    /**
     * Removes node from the graph
     * @param nodeName The name of the node
     * @return void
     */
    public void removeNode(String nodeName) {
        if(this.graph == null || this.dotFilePath == null) {
            System.out.println("this.graph is null somehow");
            this.initGraph();
        }

        MutableGraph tempGraph = mutGraph();
        tempGraph.setDirected(this.graph.isDirected());

        Map<String, MutableNode> nodeMap = new HashMap<>();
        Boolean nodeExists = false;
        for(MutableNode currNode : this.graph.nodes()) {
            if(!nodeMap.containsKey(currNode.name().toString()) && !nodeName.equals(currNode.name().toString())) {
                nodeMap.put(currNode.name().toString(), currNode);
            }

            nodeExists = nodeExists || currNode.name().toString().equals(nodeName);
        }

        if(!nodeExists) {
            return;
        }

        for(String nodeKey : nodeMap.keySet()) {
            tempGraph.add(mutNode(nodeKey).asLinkSource());
        }

        List<List<String>> edgeList = new ArrayList<>();
        for (Link edge : this.graph.edges()) {
            String edgeGroup = edge.name().toString();
            String[] groupSplit = edgeGroup.split(REGEX_UNDIRECTED_CONNECTOR);
            if(!groupSplit[0].equals(nodeName) && !groupSplit[1].equals(nodeName)) {
                edgeList.add(List.of(groupSplit));
            }
        }

        Map<String, MutableNode> tempGraphMap = new HashMap<>();
        for(MutableNode currNode : tempGraph.nodes()) {
            if(!tempGraphMap.containsKey(currNode.name().toString())) {
                tempGraphMap.put(currNode.name().toString(), currNode);
            }
        }

        for(List<String> edgeGroups : edgeList) {
            System.out.println("edgeGroups = " + edgeGroups);
            MutableNode node1 = tempGraphMap.get(edgeGroups.get(0));
            MutableNode node2 = tempGraphMap.get(edgeGroups.get(1));

            node1.addLink(node2);
        }

        this.graph = tempGraph;
        this.parseGraph(PNG_STR);
    }

    /**
     * Adds nodes to the graph
     * @param nodeNames The names of the nodes to be added
     * @return void
     */
    public int addNodes(List<String> nodeNames) {
        Integer invalidCount = 0;
        for(String nodeName : nodeNames) {
            invalidCount = this.addNode(nodeName) == 1 ? invalidCount + 1 : invalidCount;
        }
        return invalidCount;
    }

    /**
     * Removes nodes from the graph
     * @param nodeNames The names of the nodes to be removed
     * @return void
     */
    public void removeNodes(List<String> nodeNames) {
        for(String nodeName : nodeNames) {
            this.removeNode(nodeName);
        }
    }

    /**
     * Deletes the requested edges from the graph
     * @param edgeList The list of edges that need to be removed from the graph
     * @return void
     */
    public void removeEdges(List<String> edgeList) {
        if(this.graph == null || this.dotFilePath == null) {
            System.out.println("this.graph is null somehow");
            this.initGraph();
        }

        MutableGraph tempGraph = mutGraph();
        tempGraph.setDirected(this.graph.isDirected());

        Map<String, MutableNode> nodeMap = new HashMap<>();

        for(MutableNode currNode : this.graph.nodes()) {
            if(!nodeMap.containsKey(currNode.name().toString())) {
                nodeMap.put(currNode.name().toString(), currNode);
            }
        }

        for(String nodeKey : nodeMap.keySet()) {
            tempGraph.add(mutNode(nodeKey).asLinkSource());
        }

        List<List<String>> remainingEdges = new ArrayList<>();
        for (Link edge : this.graph.edges()) {
            String edgeGroup = edge.name().toString();
            String[] groupSplit = edgeGroup.split(REGEX_UNDIRECTED_CONNECTOR);

            if(this.graph.isDirected()) {
                if(!edgeList.contains(edgeGroup)) {
                    remainingEdges.add(List.of(groupSplit));
                }
            }
            else {
                String revEdgeGroup = groupSplit[1] + REGEX_UNDIRECTED_CONNECTOR + groupSplit[0];

                if(!edgeList.contains(edgeGroup) && !edgeList.contains(revEdgeGroup)) {
                    remainingEdges.add(List.of(groupSplit));
                }
            }
        }

        Map<String, MutableNode> tempGraphMap = new HashMap<>();
        for(MutableNode currNode : tempGraph.nodes()) {
            if(!tempGraphMap.containsKey(currNode.name().toString())) {
                tempGraphMap.put(currNode.name().toString(), currNode);
            }
        }

        for(List<String> edgeGroups : remainingEdges) {
            System.out.println("edgeGroups = " + edgeGroups);
            MutableNode node1 = tempGraphMap.get(edgeGroups.get(0));
            MutableNode node2 = tempGraphMap.get(edgeGroups.get(1));

            node1.addLink(node2);
        }

        this.graph = tempGraph;
        this.parseGraph(PNG_STR);
    }

    /**
     * Outputs the graph to a DOT file
     * @return void
     */
    public void outputToDot() throws IOException {
        if(this.graph == null) {
            System.out.println("Graph hasn't been initialized");
        }

        try {
            FileWriter dotWriter = new FileWriter(DEFAULT_OUTPUT_DOT_FILEPATH);
            dotWriter.write(this.graph.toString());
            dotWriter.close();
            System.out.println("Graph has been exported to '" + DEFAULT_OUTPUT_DOT_FILEPATH + "'");
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * BFS: Breadth First Search
     * @param src source node
     * @param dst destination node
     * @return A path instance that shows the path traversed to reach destination node
     */
    public Path GraphSearch(MutableNode src, MutableNode dst, Algorithm algorithm) {
        Path result = algorithm.GraphSearch(src, dst);
        return result;
    }

    public List<MutableNode> getNodeSet() {
        if(this.graph == null) {
            return List.of();
        }

        Map<String, MutableNode> nodeMap = new HashMap<>();
        for (MutableNode currNode : this.graph.nodes()) {
            if(!nodeMap.containsKey(currNode.name().toString())) {
                nodeMap.put(currNode.name().toString(), currNode);
            }
        }

        List<MutableNode> nodeList = new ArrayList<>(nodeMap.values());
        return nodeList;
    }

    public ArrayList<Link> getEdgeList() {
        System.out.println("Edge List: (" + this.graph.edges().size() +" edges)");
        for (Link edge : this.graph.edges()) {
            System.out.println(edge.name());
        }

        return new ArrayList<>(this.graph.edges());
    }

    public String getDotFilePath() {
        return this.dotFilePath;
    }

    public void checkIfGraphIsNull() {
        if(this.graph == null || this.dotFilePath == null) {
            this.initGraph();
        }
    }
}
