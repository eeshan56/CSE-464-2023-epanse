package org.example;

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
        System.out.println("\t1. Parse Graph from DOT file to image (PNG)");
        System.out.println("\t2. Print nodes and edges of the graph");
        System.out.println("\t3. Add node(s) to the graph");
        System.out.println("\t4. Add edge(s) to the graph");
        System.out.println("\t5. Remove edge(s) from the graph");
        System.out.println("\t6. Remove node(s) from the graph");
        System.out.println("\t7. Exit");
    }
    public static void main(String[] args) {
        String resp = "";
        GraphManager graphManager = new GraphManager();

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
                    graphManager.parseGraph();
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

                    graphManager.addNodes(nodeNames);
                    graphManager.parseGraph();
                    System.out.println("The requested nodes have been added. Check out the example/ex4-2.png file");
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

                        String connector = (graphManager.isDirected()) ? "-->" : "--";
                        System.out.println("Entered edge: " + node1 + connector + node2);
                        List<String> edgeGroups = new ArrayList<>();
                        edgeGroups.add(node1);
                        edgeGroups.add(node2);

                        edgeList.add(edgeGroups);
                    }

                    graphManager.addEdges(edgeList);
                    graphManager.parseGraph();
                    System.out.println("The requested edge(s) have been added. Check out the example/ex4-2.png file");
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

                        String dotEdgeStr = node1 + "--" + node2;
                        System.out.println("Entered edge: " + dotEdgeStr);

                        strEdgeList.add(dotEdgeStr);
                    }

                    graphManager.removeEdges(strEdgeList);
                    graphManager.parseGraph();
                    System.out.println("The requested edge(s) have been deleted. Check out the example/ex4-2.png file");
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
                    graphManager.parseGraph();
                    System.out.println("The requested node(s) have been deleted. Check out the example/ex4-2.png file");
                    break;
                }
                default:
                    break;
            }
        }while(!resp.equals("7"));
    }
}