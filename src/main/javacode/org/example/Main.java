package javacode.org.example;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Factory.node;

public class Main {

    public static void printMenu() {
        String stars = "*";
        for(Integer i = 0; i < 99; i ++) {
            stars += "*";
        }

        System.out.println(stars);
        System.out.println("Welcome!!!\nSelect 1 of the following options:");
        System.out.println("\t1. Parse Graph from DOT file");
        System.out.println("\t2. Print nodes and edges of the graph");
        System.out.println("\t3. Add node(s) to the graph");
        System.out.println("\t4. Add edge(s) to the graph");
        System.out.println("\t5. Remove edge(s) from the graph");
        System.out.println("\t6. Remove node(s) from the graph");
        System.out.println("\t7. Output graph to DOT file");
        System.out.println("\t8. Output graph to Image");
        System.out.println("\t9. BFS/DFS");
        System.out.println("\t10. Exit");
    }
    public static void main(String[] args) {

        String resp = "";
        GraphManager graphManager = new GraphManager();

        String defaultPngFilename = graphManager.isDirected() ? GraphManager.OUTPUT_DIR + GraphManager.DEFAULT_PNG_DIRECTED_FILENAME + "." + GraphManager.PNG_STR : GraphManager.OUTPUT_DIR + GraphManager.DEFAULT_PNG_UNDIRECTED_FILENAME + "." + GraphManager.PNG_STR;

        List<String> nodeNames;
        List<List<String>> edgeList;

        Integer nodeLen = 0;
        Integer edgeLen = 0;

        do {
            printMenu();
            Scanner sc = new Scanner(System.in);
            resp = sc.nextLine();
            Integer respInt = 0;
            try {
                respInt = Integer.parseInt(resp);
            }
            catch (Exception exc) {
                exc.printStackTrace();
                break;
            }
            switch (respInt) {
                case 1: {
                    graphManager.initGraph();
                    break;
                }
                case 2: {
                    graphManager.outputGraph();

                    break;
                }
                case 3: {
                    nodeNames = new ArrayList<>();
                    System.out.println("Enter the number of nodes to be added in the graph:");
                    nodeLen = Integer.parseInt(sc.nextLine());

                    for (Integer i = 0; i < nodeLen; i++) {
                        System.out.println("Enter the name of a node to be added:");
                        String nodeName = sc.nextLine();
                        nodeNames.add(nodeName);
                    }

                    int invalidCount = graphManager.addNodes(nodeNames);

                    if(nodeLen > invalidCount) {
                        graphManager.parseGraph(GraphManager.PNG_STR);
                        System.out.println("The requested node(s) have been deleted. Check out the '" + defaultPngFilename + "' file");
                    }
                    break;
                }
                case 4: {
                    edgeList = new ArrayList<>();
                    System.out.print("Enter the number of edge(s) to be added in the graph: ");
                    edgeLen = Integer.parseInt(sc.nextLine());

                    for (Integer i = 0; i < edgeLen; i++) {
                        System.out.println("Enter the names of a nodes between which an edge needs to be added:");
                        System.out.print("Node 1: ");
                        String node1 = sc.nextLine();
                        System.out.print("Node 2: ");
                        String node2 = sc.nextLine();

                        String connector = (graphManager.isDirected()) ? GraphManager.REGEX_DIRECTED_CONNECTOR : GraphManager.REGEX_UNDIRECTED_CONNECTOR;
                        System.out.println("Entered edge: " + node1 + connector + node2);
                        List<String> edgeGroups = new ArrayList<>();
                        edgeGroups.add(node1);
                        edgeGroups.add(node2);

                        edgeList.add(edgeGroups);
                    }

                    graphManager.addEdges(edgeList);
                    graphManager.parseGraph(GraphManager.PNG_STR);
                    System.out.println("The requested node(s) have been deleted. Check out the '" + defaultPngFilename + "' file");
                    break;
                }
                case 5: {
                    List<String> strEdgeList = new ArrayList<>();
                    System.out.print("Enter the number of edge(s) to be removed from the graph: ");
                    edgeLen = Integer.parseInt(sc.nextLine());

                    for (Integer i = 0; i < edgeLen; i++) {
                        System.out.println("Enter the names of a nodes between which an edge needs to be added:");
                        System.out.print("Node 1: ");
                        String node1 = sc.nextLine();
                        System.out.print("Node 2: ");
                        String node2 = sc.nextLine();

                        String dotEdgeStr = node1 + GraphManager.REGEX_UNDIRECTED_CONNECTOR + node2;
                        System.out.println("Entered edge: " + dotEdgeStr);

                        strEdgeList.add(dotEdgeStr);
                    }

                    graphManager.removeEdges(strEdgeList);
                    graphManager.parseGraph(GraphManager.PNG_STR);
                    System.out.println("The requested node(s) have been deleted. Check out the '" + defaultPngFilename + "' file");
                    break;
                }
                case 6: {
                    nodeNames = new ArrayList<>();
                    System.out.print("Enter the number of nodes to be deleted from the graph: ");
                    nodeLen = Integer.parseInt(sc.nextLine());

                    for (Integer i = 0; i < nodeLen; i++) {
                        System.out.print("Enter the name of a node to be deleted: ");
                        String nodeName = sc.nextLine();
                        nodeNames.add(nodeName);
                    }

                    graphManager.removeNodes(nodeNames);
                    graphManager.parseGraph(GraphManager.PNG_STR);

                    System.out.println("The requested node(s) have been deleted. Check out the '" + defaultPngFilename + "' file");
                    break;
                }
                case 7: {
                    try {
                        graphManager.outputToDot();
                    }
                    catch (IOException exc) {
                        System.out.println("Please retry operation...");
                    }
                    break;
                }
                case 8: {
                    System.out.print("Enter the intended image format (png/jpg): ");
                    String fileType = sc.nextLine();
                    graphManager.parseGraph(fileType);
                    break;
                }
                case 9: {
                    String algo = "";
                    Boolean valid = false;
                    while(!valid) {
                        try {
                            System.out.print("Enter the search algorithm(BFS/DFS): ");
                            algo = sc.nextLine();
                            valid = (algo.toLowerCase().equals(GraphManager.BFS_STR) || algo.toLowerCase().equals(GraphManager.DFS_STR));
                        }
                        catch (Exception e) {
                            valid = false;
                        }
                    }

                    System.out.print("Enter the source node name: ");
                    String src = sc.nextLine();

                    System.out.print("Enter the destination node name: ");
                    String dst = sc.nextLine();

                    List<MutableNode> nodeList = graphManager.getNodeSet();
                    Boolean srcExists = false;

                    MutableNode srcNode = mutNode(src);
                    MutableNode dstNode = mutNode(dst);

                    for(MutableNode node : nodeList) {
                        if(node.name().toString().equals(src)) {
                            srcExists = true;
                            srcNode = node;
                        }
                        if(node.name().toString().equals(dst)) {
                            dstNode = node;
                        }
                    }

                    if(!srcExists) {
                        System.out.println("The entered source node is not present in the graph");
                        break;
                    }

                    Algorithm algorithm = algo.toLowerCase().equals(GraphManager.BFS_STR) ? new BreadthFirstSearch() : new DepthFirstSearch();
                    Path path = graphManager.GraphSearch(srcNode, dstNode, algorithm);
                    if(path != null) {
                        System.out.println("path = " + path.getPath());
                    }
                    else {
                        System.out.println("Path not found...");
                    }

                    break;
                }
                default:
                    break;
            }
        }while(!resp.equals("10"));
    }
}
