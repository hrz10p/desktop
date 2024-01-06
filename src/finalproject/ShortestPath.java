package finalproject;

import finalproject.system.Tile;
import finalproject.tiles.MetroTile;


public class ShortestPath extends PathFindingService {
    public ShortestPath(Tile start) {
        super(start);
        generateGraph();
    }

    @Override
    public void generateGraph() {
        g = new Graph(GraphTraversal.DFS(source));

        for (Tile v : GraphTraversal.DFS(source)) {
            Tile[] neighbors = v.neighbors.toArray(new Tile[0]);
            for (Tile u : neighbors) {
                boolean metro = v instanceof MetroTile && u instanceof MetroTile;
                if (metro) ((MetroTile) u).fixMetro(v);
                if (u.isWalkable()) {
                    double weight = metro ? ((MetroTile) u).metroDistanceCost : u.distanceCost;
                    g.addEdge(v, u, weight);
                    edges.put(v, g.getAllEdges());
                }
            }
        }
    }
}
