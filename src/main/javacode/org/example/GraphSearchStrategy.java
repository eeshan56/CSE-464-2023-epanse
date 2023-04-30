package javacode.org.example;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableNode;
import javacode.org.example.Path;

import java.util.*;

public abstract class GraphSearchStrategy implements Algorithm {
    protected abstract Collection<Link> getNeighbors(MutableNode node);

    public Path GraphSearch(MutableNode src, MutableNode dst) {
        String algo = useQueue() ? "Breadth First Search" : "Depth First Search";
        System.out.println(algo + " via Strategy Pattern");

        Queue<List<MutableNode>> queue = new LinkedList<>();
        Stack<List<MutableNode>> stack = new Stack<>();

        Set<String> visited = new HashSet<>();

        List<MutableNode> path = new ArrayList<>();
        path.add(src);

        if(useQueue()) {
            queue.add(path);
        }
        else {
            stack.push(path);
        }

        Boolean datastructureEmpty = false;
        while (!datastructureEmpty) {
            List<MutableNode> currentPath = new ArrayList<>();

            if(useQueue()) {
                currentPath = queue.remove();
            }
            else {
                currentPath = stack.pop();
            }

            MutableNode currentNode = currentPath.get(currentPath.size() - 1);
            if (currentNode.name().toString().equals(dst.name().toString())) {
                Path result = new Path();
                for (MutableNode currPathNode : currentPath) {
                    result.addToPath(currPathNode);
                }
                return result;
            }

            visited.add(currentNode.name().toString());

            Collection<Link> neighNodes = getNeighbors(currentNode);

            for (Link neighbor : neighNodes) {

                if (!visited.contains(neighbor.to().name().toString())) {
                    List<MutableNode> newPath = new ArrayList<>(currentPath);

                    LinkTarget toNode = neighbor.to();
                    newPath.add((MutableNode) toNode.asLinkSource());
                    if(useQueue()) {
                        queue.add(newPath);
                    }
                    else {
                        stack.push(newPath);
                    }
                }
            }
            datastructureEmpty = useQueue() ? queue.isEmpty() : stack.isEmpty();
        }

        return null;
    }

    protected abstract boolean useQueue();
}

class BreadthFirstSearchStrategy extends GraphSearchStrategy {
    @Override
    protected Collection<Link> getNeighbors(MutableNode node) {
        return node.links();
    }

    @Override
    protected boolean useQueue() {
        return true;
    }
}

class DepthFirstSearchStrategy extends GraphSearchStrategy {
    @Override
    protected Collection<Link> getNeighbors(MutableNode node) {
        return new ArrayList<>(node.links());
    }

    @Override
    protected boolean useQueue() {
        return false;
    }
}

class RandomWalkSearchStrategy extends GraphSearchStrategy {

    @Override
    protected Collection<Link> getNeighbors(MutableNode node) {
        return new ArrayList<>(node.links());
    }

    @Override
    public Path GraphSearch(MutableNode src, MutableNode dst) {
        System.out.println("Random Walk Search via Strategy Pattern");
        MutableNode current = (MutableNode) src;
        Set<String> visited = new HashSet<>();
        visited.add(current.name().toString());
        Path result = new Path();
        result.addToPath(current);

        String pathTraversed = current.name().toString();

        while (!current.name().toString().equals(dst.name().toString())) {
            Collection<Link> neighbors = getNeighbors(current);
            List<MutableNode> validNeighbors = new ArrayList<>();
            for (Link neighbor : neighbors) {
                LinkTarget toNode = neighbor.to();
                MutableNode toMutableNode = (MutableNode) toNode.asLinkSource();
                if (!visited.contains(toMutableNode.name().toString())) {
                    validNeighbors.add(toMutableNode);
                }
            }

            if (validNeighbors.isEmpty()) {
                System.out.println("No more unvisited nodes to traverse were found.");
                System.out.println("Starting from source again...");
                pathTraversed = src.name().toString();
                System.out.println("Path traversed: " + pathTraversed);

                current = (MutableNode) src;

                result = new Path();
                result.addToPath(current);

                continue;
            }

            int randomIndex = (int) Math.round(Math.random() * validNeighbors.size()) % validNeighbors.size();
            MutableNode next = validNeighbors.get(randomIndex);
            visited.add(next.name().toString());
            result.addToPath(next);
            pathTraversed += " --> " + next.name().toString();
            System.out.println("Path traversed: " + pathTraversed);
            current = next;
        }

        return result;
    }

    @Override
    protected boolean useQueue() {
        return false;
    }
}