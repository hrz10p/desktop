package finalproject;


import finalproject.system.Tile;
import finalproject.tiles.MetroTile;

import java.util.ArrayList;
import java.util.LinkedList;

public class SafestShortestPath extends ShortestPath {
	public int health;
	public Graph costGraph;
	public Graph damageGraph;
	public Graph aggregatedGraph;

	public SafestShortestPath(Tile start, int health) {
		super(start);
		this.health = health;
		generateGraph();
	}

	public void generateGraph() {
		ArrayList<Tile> nodes = GraphTraversal.DFS(source);
		costGraph = new Graph(nodes);
		damageGraph = new Graph(nodes);
		aggregatedGraph = new Graph(nodes);

		for (Tile v : nodes) {
			edges.putIfAbsent(v, new ArrayList<>());
			for (Tile u : v.neighbors) {
				boolean metro = v instanceof MetroTile && u instanceof MetroTile;
				if (metro) ((MetroTile) u).fixMetro(v);
				if (u.isWalkable()) {
					double distanceWeight = metro ? ((MetroTile) u).metroDistanceCost : u.distanceCost;
					double damageWeight = u.damageCost;

					costGraph.addEdge(v, u, distanceWeight);
					damageGraph.addEdge(v, u, damageWeight);
					aggregatedGraph.addEdge(v, u, damageWeight);

					edges.get(v).add(new Graph.Edge(v, u, damageWeight));
				}
			}
		}
	}


	public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {
		g = costGraph;

		ArrayList<Tile> costGraphPath = super.findPath(source, waypoints);

		if (damageGraph.computePathCost(costGraphPath) < health) return costGraphPath;

		g = damageGraph;

		ArrayList<Tile> damageGraphPath = super.findPath(source, waypoints);

		if (damageGraph.computePathCost(damageGraphPath) > health) return null;

		g = aggregatedGraph;

		while (true) {
			double lambda = (costGraph.computePathCost(costGraphPath) - costGraph.computePathCost(damageGraphPath))
					/ (damageGraph.computePathCost(damageGraphPath) - damageGraph.computePathCost(costGraphPath));

			for (Graph.Edge e : aggregatedGraph.getAllEdges()) {
				e.weight = getCost(e, "distance") + lambda * getCost(e, "damage");
			}

			ArrayList<Tile> aggregatedGraphPath = super.findPath(source, waypoints);

			if (aggregatedGraph.computePathCost(aggregatedGraphPath) == aggregatedGraph.computePathCost(costGraphPath)) {
				return damageGraphPath;
			} else if (damageGraph.computePathCost(aggregatedGraphPath) <= health) {
				damageGraphPath = aggregatedGraphPath;
			} else {
				costGraphPath = aggregatedGraphPath;
			}
		}
	}

	public double getCost(Graph.Edge e, String type) {
		boolean metro = e.origin instanceof MetroTile && e.destination instanceof MetroTile;

		switch (type) {
			case "damage":
				return e.destination.damageCost;
			case "distance":
				return metro ? ((MetroTile) e.destination).metroDistanceCost : e.destination.distanceCost;
			case "time":
				return metro ? ((MetroTile) e.destination).metroTimeCost : e.destination.timeCost;
			default:
				throw new IllegalArgumentException();
		}
	}
}
