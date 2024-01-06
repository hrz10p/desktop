package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
    private HashSet<Tile> vertices;
    private HashSet<Edge> edges;

    public Graph(ArrayList<Tile> vertices) {
        this.vertices = new HashSet<>(vertices);
        this.edges = new HashSet<>();
    }

    public void addEdge(Tile origin, Tile destination, double weight) {
        Edge newEdge = new Edge(origin, destination, weight);
        edges.add(newEdge);
    }

    public ArrayList<Edge> getAllEdges() {
        return new ArrayList<>(edges);
    }

    public ArrayList<Tile> getNeighbors(Tile t) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getStart().equals(t)) {
                neighbors.add(edge.getEnd());
            }
        }
        return neighbors;
    }

    public double computePathCost(ArrayList<Tile> path) {
        double totalCost = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Tile start = path.get(i);
            Tile end = path.get(i + 1);
            for (Edge edge : edges) {
                if (edge.getStart().equals(start) && edge.getEnd().equals(end)) {
                    totalCost += edge.weight;
                    break;
                }
            }
        }
        return totalCost;
    }

    public static class Edge {
        Tile origin;
        Tile destination;
        double weight;

        public Edge(Tile s, Tile d, double cost) {
            this.origin = s;
            this.destination = d;
            this.weight = cost;
        }

        public Tile getStart() {
            return origin;
        }

        public Tile getEnd() {
            return destination;
        }

        // Override equals and hashCode to properly manage Edge uniqueness in HashSet
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return origin.equals(edge.origin) && destination.equals(edge.destination);
        }

        @Override
        public int hashCode() {
            return 31 * origin.hashCode() + destination.hashCode();
        }
    }
}
