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

    public void removeNodes (Node node) {
        ArrayList <Arc> arcsToRemove = new ArrayList<>();
         for (Arc arc : arcs) {
            if (arc.getNode1() == node || arc.getNode2() == node) {
                arcsToRemove.add(arc);
            }
        }
        this.removeArcs(arcsToRemove);
        this.nodes.remove(node);
    }

    public void removeArcs (List<Arc> arcs) {
        for (Arc arc : arcs) {
            this.arcs.remove(arc);
        }
    }

}
