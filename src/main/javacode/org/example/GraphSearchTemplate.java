package javacode.org.example;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableNode;
import javacode.org.example.Path;

import java.util.*;

public abstract class GraphSearchTemplate implements Algorithm {
    protected abstract Collection<Link> getNeighbors(MutableNode node);

    public Path GraphSearch(MutableNode src, MutableNode dst) {
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

class BreadthFirstSearch extends GraphSearchTemplate {
    @Override
    protected Collection<Link> getNeighbors(MutableNode node) {
        return node.links();
    }

    @Override
    protected boolean useQueue() {
        return true;
    }
}

class DepthFirstSearch extends GraphSearchTemplate {
    @Override
    protected Collection<Link> getNeighbors(MutableNode node) {
        return new ArrayList<>(node.links());
    }

    @Override
    protected boolean useQueue() {
        return false;
    }
}
