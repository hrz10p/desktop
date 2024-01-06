package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class PathFindingService {
    HashMap<Tile, ArrayList<Graph.Edge>> edges;
    Tile source;
    Graph g;

    public PathFindingService(Tile start) {
        this.source = start;
        this.edges = new HashMap<>();
    }

    public abstract void generateGraph();

    public ArrayList<Tile> findPath(Tile startNode) {
        TilePriorityQ queue = new TilePriorityQ();

        for (Tile t : getAllVertices()) {
            t.costEstimate = t == startNode ? 0 : Double.MAX_VALUE;
            t.predecessor = null;
            queue.insert(t);
        }

        Tile endNode = null;

        while (!queue.isEmpty()) {
            Tile u = queue.removeMin();

            for (Tile v : g.getNeighbors(u)) {
                if (v.isDestination) endNode = v;

                if (u.costEstimate + getWeight(u, v, g.getAllEdges()) < v.costEstimate)
                    queue.updateKeys(v, u, u.costEstimate + getWeight(u, v, g.getAllEdges()));
            }
        }

        ArrayList<Tile> path = new ArrayList<>();

        if (endNode.predecessor != null || endNode == startNode) {
            while (endNode != null) {
                path.add(0, endNode);
                endNode = endNode.predecessor;
            }
        }

        return path;
    }

    public ArrayList<Tile> findPath(Tile start, Tile end) {
        TilePriorityQ queue = new TilePriorityQ();

        for (Tile t : getAllVertices()) {
            t.costEstimate = t == start ? 0 : Double.MAX_VALUE;
            t.predecessor = null;
            queue.insert(t);
        }

        while (!queue.isEmpty()) {
            Tile u = queue.removeMin();

            for (Tile v : g.getNeighbors(u))
                if (u.costEstimate + getWeight(u, v, g.getAllEdges()) < v.costEstimate)
                    queue.updateKeys(v, u, u.costEstimate + getWeight(u, v, g.getAllEdges()));
        }

        ArrayList<Tile> path = new ArrayList<>();

        if (end.predecessor != null || end == start) {
            while (end != null) {
                path.add(0, end);
                end = end.predecessor;
            }
        }

        return path;
    }

    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {
        ArrayList<Tile> all = new ArrayList<>();

        all.add(start);

        for (Tile w : waypoints) all.add(w);

        for (Tile t : getAllVertices()) if (t.isDestination) all.add(t);

        ArrayList<Tile> path = new ArrayList<>();

        for (int i = 0; i < all.size() - 1; ++i) {
            ArrayList<Tile> curr = findPath(all.get(i), all.get(i + 1));
            for (int j = i > 0 ? 1 : 0; j < curr.size(); ++j) path.add(curr.get(j));
        }

        return path;
    }

    public static double getWeight(Tile u, Tile v, ArrayList<Graph.Edge> edges) {
        for (Graph.Edge x : edges) {
            if (x.destination == v) {
                return x.weight;
            }
        }
        return 0;
    }

    public ArrayList<Tile> getAllVertices() {
        ArrayList<Tile> ret = new ArrayList<>();
        for (Tile t : edges.keySet()) ret.add(t);
        return ret;
    }

}
