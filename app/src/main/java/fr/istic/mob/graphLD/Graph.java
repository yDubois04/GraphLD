package fr.istic.mob.graphLD;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Node> nodes;
    private List<Arc> arcs;

    public Graph () {
        nodes = new ArrayList <Node>();
        arcs = new ArrayList <Arc>();
    }

    public void addNode (Node node) {
        this.nodes.add(node);
    }

    public void addArc (Arc arc) {
        this.arcs.add(arc);
    }

    public List<Node> getNodes () {
        return nodes;
    }

    public List<Arc> getArcs () {
        return arcs;
    }

    public void removeArc (Arc arc) {
        arcs.remove(arc);
    }

}
