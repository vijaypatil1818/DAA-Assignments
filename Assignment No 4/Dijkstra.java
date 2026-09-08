import java.util.*;

public class Dijkstra {

    static void dijkstra(int source, ArrayList<ArrayList<Edge>> graph, int[] dist) {
        int V = graph.size();

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(
            Comparator.comparingInt(a -> a.distance)
        );

        pq.add(new Node(0, source));

        while (!pq.isEmpty()) {
            int u = pq.peek().node;
            int d = pq.peek().distance;
            pq.poll();

            if (d > dist[u]) {
                continue;
            }

            for (Edge edge : graph.get(u)) {
                int v = edge.destination;
                int w = edge.weight;

                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    pq.add(new Node(dist[v], v));
                }
            }
        }
    }

    // Class to represent an edge
    static class Edge {
        int destination;
        int weight;

        Edge(int destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Class to represent a node in the priority queue
    static class Node {
        int distance;
        int node;

        Node(int distance, int node) {
            this.distance = distance;
            this.node = node;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int V, E;

        System.out.print("Enter number of intersections (vertices): ");
        V = sc.nextInt();

        System.out.print("Enter number of roads (edges): ");
        E = sc.nextInt();

        ArrayList<ArrayList<Edge>> graph = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        System.out.println("Enter edges (u v w):");

        for (int i = 0; i < E; i++) {
            int u, v, w;

            u = sc.nextInt();
            v = sc.nextInt();
            w = sc.nextInt();

            graph.get(u).add(new Edge(v, w));
            graph.get(v).add(new Edge(u, w)); // undirected road
        }

        int source;

        System.out.print("Enter ambulance start location (source): ");
        source = sc.nextInt();

        int H;

        System.out.print("Enter number of hospitals: ");
        H = sc.nextInt();

        int[] hospitals = new int[H];

        System.out.print("Enter hospital nodes: ");

        for (int i = 0; i < H; i++) {
            hospitals[i] = sc.nextInt();
        }

        int[] dist = new int[V];

        dijkstra(source, graph, dist);

        int minTime = Integer.MAX_VALUE;
        int nearestHospital = -1;

        for (int h : hospitals) {
            if (dist[h] < minTime) {
                minTime = dist[h];
                nearestHospital = h;
            }
        }

        if (nearestHospital == -1) {
            System.out.println("No hospital reachable.");
        } else {
            System.out.println(
                "Nearest hospital is at node " + nearestHospital
                + " with travel time " + minTime + " minutes."
            );
        }

        sc.close();
    }
}
