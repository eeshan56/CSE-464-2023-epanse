package javacode.org.example;

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

public class GraphManager {
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

        System.out.println("Default filepath of the dot file is 'src/main/java/org/example/color.dot'.");
        System.out.print("Do you want to continue with the default dot file? (Enter y/n): ");
        Scanner sc = new Scanner(System.in);
        String resp = sc.nextLine();

        if(resp.equals("y") || resp.equals("Y")) {
            this.dotFilePath = "src/main/java/org/example/color.dot";
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
            if(fileType.toLowerCase().equals("png")) {
                Graphviz.fromGraph(this.graph).width(700).render(Format.PNG).toFile(new File("example/ex4-2.png"));
            }
            else if(fileType.toLowerCase().equals("jpg") || fileType.toLowerCase().equals("jpeg")) {
                String outputFile = "example/ex4-1." + fileType;
                Graphviz.fromGraph(this.graph).width(700).render(Format.PNG).toFile(new File(outputFile));
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
        if(this.graph == null || this.dotFilePath == null) {
            this.initGraph();
        }

        Set<String> nodeSet = new HashSet<>();

        for (MutableNode node : this.graph.nodes()) {
            nodeSet.add(node.name().toString());
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
    public void addNode(String nodeName) {
        if(this.graph == null || this.dotFilePath == null) {
            this.initGraph();
        }

        Set<String> nodeSet = new HashSet<>();
        boolean isDuplicate = false;
        for (MutableNode node : this.graph.nodes()) {
            nodeSet.add(node.name().toString());
            isDuplicate = node.name().toString().equals(nodeName);
        }

        if(isDuplicate) {
            System.out.println("Node " + nodeName + " is already present in the graph");
            return;
        }

        // MutableNode node1 = (MutableNode) this.graph.add().asLinkSource();
        this.graph.add(mutNode(nodeName).asLinkSource());
    }

    /**
     * Adds edges between a list of nodes to the graph
     * @param edgeList The list of nodes between which an edge is needed
     * @return void
     */
    public void addEdges(List<List<String>> edgeList) {
        if(this.graph == null || this.dotFilePath == null) {
            this.initGraph();
        }

        Map<String, MutableNode> nameToNodeMap = new HashMap<>();
        for(MutableNode node : this.graph.nodes()) {
            if(!nameToNodeMap.containsKey(node.name())) {
                nameToNodeMap.put(String.valueOf(node.name()), node);
            }
        }
        // System.out.println("nodeMap = " + nameToNodeMap);
        Scanner sc = new Scanner(System.in);

        for (List<String> edgeGroups : edgeList) {
            String nodeName1 = edgeGroups.get(0);
            String nodeName2 = edgeGroups.get(1);

            if(!nameToNodeMap.containsKey(nodeName1)) {
                System.out.println(nodeName1 + " is not present in the graph. \nDo you want to add it? (Enter y or n)");
                String resp = sc.nextLine();
                if(resp.equals("y")) {
                    this.addNode(nodeName1);

                    // node1 = mutNode(nodeName1);
                    // nameToNodeMap.put(nodeName1, node1);
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
                    //node2 = mutNode(nodeName2);
                    //nameToNodeMap.put(nodeName2, node2);
                }
                else {
                    System.out.println("Exiting...");
                    return;
                }
            }
        }

        nameToNodeMap = new HashMap<>();
        for(MutableNode node : this.graph.nodes()) {
            if(!nameToNodeMap.containsKey(node.name())) {
                nameToNodeMap.put(String.valueOf(node.name()), node);
            }
        }

        for(List<String> edgeGroups : edgeList) {
            MutableNode node1 = nameToNodeMap.get(edgeGroups.get(0));
            MutableNode node2 = nameToNodeMap.get(edgeGroups.get(1));

            Boolean isDuplicate = false;
            for(Link link : node1.links()) {
                List<String> linkSplit = List.of(link.name().toString().split("--"));
                if(linkSplit.get(0).equals(node2.name().toString()) || linkSplit.get(1).equals(node2.name().toString())) {
                    isDuplicate = true;
                }
            }

            if(!isDuplicate) {
                node1.addLink(node2);
            }
            else {
                String connector = (this.graph.isDirected()) ? "-->" : "--";
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
        for(MutableNode node : this.graph.nodes()) {
            if(!nodeMap.containsKey(node.name().toString()) && !nodeName.equals(node.name().toString())) {
                nodeMap.put(node.name().toString(), node);
            }

            nodeExists = nodeExists || node.name().toString().equals(nodeName);
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
            String[] groupSplit = edgeGroup.split("--");
            if(!groupSplit[0].equals(nodeName) && !groupSplit[1].equals(nodeName)) {
                edgeList.add(List.of(groupSplit));
            }
        }

        Map<String, MutableNode> tempGraphMap = new HashMap<>();
        for(MutableNode node : tempGraph.nodes()) {
            if(!tempGraphMap.containsKey(node.name().toString())) {
                tempGraphMap.put(node.name().toString(), node);
            }
        }

        for(List<String> edgeGroups : edgeList) {
            System.out.println("edgeGroups = " + edgeGroups);
            MutableNode node1 = tempGraphMap.get(edgeGroups.get(0));
            MutableNode node2 = tempGraphMap.get(edgeGroups.get(1));

            node1.addLink(node2);
        }

        this.graph = tempGraph;
        try {
            Graphviz.fromGraph(tempGraph).width(700).render(Format.PNG).toFile(new File("example/ex4-3.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds nodes to the graph
     * @param nodeNames The names of the nodes to be added
     * @return void
     */
    public void addNodes(List<String> nodeNames) {
        for(String nodeName : nodeNames) {
            this.addNode(nodeName);
        }
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

        for(MutableNode node : this.graph.nodes()) {
            if(!nodeMap.containsKey(node.name().toString())) {
                nodeMap.put(node.name().toString(), node);
            }
        }

        for(String nodeKey : nodeMap.keySet()) {
            tempGraph.add(mutNode(nodeKey).asLinkSource());
        }

        List<List<String>> remainingEdges = new ArrayList<>();
        for (Link edge : this.graph.edges()) {
            String edgeGroup = edge.name().toString();
            String[] groupSplit = edgeGroup.split("--");

            if(this.graph.isDirected()) {
                if(!edgeList.contains(edgeGroup)) {
                    remainingEdges.add(List.of(groupSplit));
                }
            }
            else {
                String revEdgeGroup = groupSplit[1] + "--" + groupSplit[0];

                if(!edgeList.contains(edgeGroup) && !edgeList.contains(revEdgeGroup)) {
                    remainingEdges.add(List.of(groupSplit));
                }
            }
        }

        Map<String, MutableNode> tempGraphMap = new HashMap<>();
        for(MutableNode node : tempGraph.nodes()) {
            if(!tempGraphMap.containsKey(node.name().toString())) {
                tempGraphMap.put(node.name().toString(), node);
            }
        }

        for(List<String> edgeGroups : remainingEdges) {
            System.out.println("edgeGroups = " + edgeGroups);
            MutableNode node1 = tempGraphMap.get(edgeGroups.get(0));
            MutableNode node2 = tempGraphMap.get(edgeGroups.get(1));

            node1.addLink(node2);
        }

        this.graph = tempGraph;
        try {
            Graphviz.fromGraph(tempGraph).width(700).render(Format.PNG).toFile(new File("example/ex4-3.png"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            FileWriter dotWriter = new FileWriter("example/output.dot");
            dotWriter.write(this.graph.toString());
            dotWriter.close();
            System.out.println("Graph has been exported to 'example/output.dot'");
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
    public Path GraphSearch(MutableNode src, MutableNode dst) {
        List<MutableNode> nodeList = this.getNodeSet();
        // Map<String, List<String>> neighborMap = createNeighborMap();

        Queue<List<MutableNode>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        List<MutableNode> path = new ArrayList<>();
        path.add(src);
        queue.add(path);

        while (!queue.isEmpty()) {
//            this.displayQueue(queue);
            List<MutableNode> currentPath = queue.remove();
            MutableNode currentNode = currentPath.get(currentPath.size() - 1);
            if (currentNode.name().toString().equals(dst.name().toString())) {
                Path result = new Path();
                for (MutableNode node : currentPath) {
                    result.addToPath(node);
                }
                return result;
            }

            visited.add(currentNode.name().toString());
//            List<String> neighborNodes = neighborMap.get(currentNode.name().toString());
//            List<MutableNode> neighNodes = new ArrayList<>();
//
//            for(MutableNode node : nodeList) {
//                if(!neighborNodes.contains(node.name().toString())) {
//                    System.out.println(node.name().toString());
//                    neighNodes.add(node);
//                }
//            }

            Collection<Link> neighNodes = currentNode.links();

            for (Link neighbor : neighNodes) {

                if (!visited.contains(neighbor.to().name().toString())) {
                    List<MutableNode> newPath = new ArrayList<>(currentPath);

                    LinkTarget toNode = neighbor.to();
                    newPath.add((MutableNode) toNode.asLinkSource());
                    queue.add(newPath);
                }
            }
        }

        return null;
    }

    public void displayQueue(Queue<List<MutableNode>> queue) {
        Queue<List<MutableNode>> queueCopy = new LinkedList<>(queue);
        while(!queueCopy.isEmpty()) {
            System.out.println(queueCopy.remove());
        }
    }

//    private Map<String, List<String>> createNeighborMap() {
//        Map<String, List<String>> neighborMap = new HashMap<>();
//        for (MutableNode currNode : this.graph.nodes()) {
//            for (Link edge : currNode.links()) {
//                List<String> edgeNodes = List.of(edge.toString().split("--"));
//                if (this.graph.isDirected()) {
//                    List<String> corrEdges;
//                    if (!neighborMap.containsKey(currNode.name().toString())) {
//                        corrEdges = new ArrayList<>();
//                    } else {
//                        corrEdges = neighborMap.get(currNode.name().toString());
//                    }
//
//                    if (edgeNodes.get(0).equals(currNode.name().toString())) {
//                        corrEdges.add(edgeNodes.get(1));
//                    } else {
//                        corrEdges.add(edgeNodes.get(0));
//                    }
//
//                    neighborMap.put(currNode.name().toString(), corrEdges);
//                }
//                else {
//                    Integer currInd = 0;
//                    if (!edgeNodes.get(0).equals(currNode.name().toString())) {
//                        currInd = 1;
//                    }
//                    Integer newInd = (currInd == 0) ? 1 : 0;
//
//                    List<String> corrEdges1;
//                    List<String> corrEdges2;
//
//                    if (!neighborMap.containsKey(currNode.name().toString())) {
//                        corrEdges1 = new ArrayList<>();
//                    } else {
//                        corrEdges1 = neighborMap.get(currNode.name().toString());
//                    }
//
//                    if (!neighborMap.containsKey(edgeNodes.get(newInd))) {
//                        corrEdges2 = new ArrayList<>();
//                    } else {
//                        corrEdges2 = neighborMap.get(edgeNodes.get(newInd));
//                    }
//
//                    corrEdges1.add(edgeNodes.get(newInd));
//                    corrEdges2.add(edgeNodes.get(currInd));
//
//                    neighborMap.put(currNode.name().toString(), corrEdges1);
//                    neighborMap.put(edgeNodes.get(newInd), corrEdges2);
//                }
//            }
//        }
//        return neighborMap;
//    }

    public List<MutableNode> getNodeSet() {
        if(this.graph == null) {
            return List.of();
        }

        Map<String, MutableNode> nodeMap = new HashMap<>();
        for (MutableNode node : this.graph.nodes()) {
            if(!nodeMap.containsKey(node.name().toString())) {
                nodeMap.put(node.name().toString(), node);
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
}
