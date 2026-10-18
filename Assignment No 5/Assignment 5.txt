import java.util.*;

public class AssignmentNo05 {
    static class Edge {
        int to;
        double baseCost;
        double curCost;

        Edge(int to, double cost) {
            this.to = to;
            this.baseCost = cost;
            this.curCost = cost;
        }
    }

    static int stages, nodes;
    static int[] stageCount, stageStart, nodeStage, nextNode;
    static double[] bestCost;
    static ArrayList<ArrayList<Edge>> adj;
    static final double INF = 1e30;

    static void calculateDP() {
        Arrays.fill(bestCost, INF);
        Arrays.fill(nextNode, -1);

        int lastStage = stages - 1;
        for (int k = 0; k < stageCount[lastStage]; k++) {
            int node = stageStart[lastStage] + k;
            bestCost[node] = 0.0;
        }

        for (int st = stages - 2; st >= 0; st--) {
            for (int k = 0; k < stageCount[st]; k++) {
                int u = stageStart[st] + k;
                double best = INF;
                int bestV = -1;

                for (Edge e : adj.get(u)) {
                    int v = e.to;
                    if (bestCost[v] >= INF / 2) {
                        continue;
                    }

                    double candidate = e.curCost + bestCost[v];
                    if (candidate < best) {
                        best = candidate;
                        bestV = v;
                    }
                }

                bestCost[u] = best;
                nextNode[u] = bestV;
            }
        }
    }

    static void printPath(int src) {
        if (src < 0 || src >= nodes) {
            System.out.println("Error: Invalid source node.");
            return;
        }

        if (nodeStage[src] != 0) {
            System.out.println("Error: Source node " + src + " is not in Stage 0.");
            return;
        }

        if (bestCost[src] >= INF / 2) {
            System.out.println("No route from node " + src + ".");
            return;
        }

        System.out.println("\nOptimal Path from Node " + src + ":");
        int cur = src;
        double total = 0.0;
        System.out.print(cur);

        while (nextNode[cur] != -1) {
            int nxt = nextNode[cur];
            double edgeCost = INF;

            for (Edge e : adj.get(cur)) {
                if (e.to == nxt) {
                    edgeCost = Math.min(edgeCost, e.curCost);
                }
            }

            if (edgeCost >= INF / 2) {
                System.out.println("\nError: Edge not found.");
                return;
            }

            total += edgeCost;
            System.out.print(" -> " + nxt);
            cur = nxt;
        }

        System.out.printf("%n%nTotal Route Cost = %.2f%n", total);
    }

    static void printStageZeroCosts(String heading, String costLabel) {
        System.out.println("\n" + heading);
        for (int k = 0; k < stageCount[0]; k++) {
            int node = stageStart[0] + k;
            if (bestCost[node] >= INF / 2) {
                System.out.println("Node " + node + ": Unreachable");
            } else {
                System.out.printf("Node %d: %s = %.2f%n", node, costLabel, bestCost[node]);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of stages: ");
        stages = sc.nextInt();

        if (stages < 2) {
            System.out.println("Error: Number of stages must be at least 2.");
            return;
        }

        stageCount = new int[stages];
        nodes = 0;

        System.out.print("Enter number of nodes in each stage (" + stages + " values): ");
        for (int i = 0; i < stages; i++) {
            stageCount[i] = sc.nextInt();
            if (stageCount[i] <= 0) {
                System.out.println("Error: Number of nodes in a stage must be positive.");
                return;
            }
            nodes += stageCount[i];
        }

        stageStart = new int[stages];
        int idx = 0;
        for (int i = 0; i < stages; i++) {
            stageStart[i] = idx;
            idx += stageCount[i];
        }

        System.out.print("Enter number of edges: ");
        int m = sc.nextInt();

        if (m < 0) {
            System.out.println("Error: Number of edges cannot be negative.");
            return;
        }

        adj = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            adj.add(new ArrayList<>());
        }

        System.out.println("\nEnter each edge as: u v cost");
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            double cost = sc.nextDouble();

            if (u < 0 || u >= nodes || v < 0 || v >= nodes) {
                System.out.println("Error: Invalid node ID in edge " + u + " -> " + v);
                return;
            }

            if (cost < 0) {
                System.out.println("Error: Edge cost cannot be negative.");
                return;
            }

            adj.get(u).add(new Edge(v, cost));
        }

        nodeStage = new int[nodes];
        for (int st = 0; st < stages; st++) {
            for (int k = 0; k < stageCount[st]; k++) {
                int node = stageStart[st] + k;
                nodeStage[node] = st;
            }
        }

        bestCost = new double[nodes];
        nextNode = new int[nodes];
        calculateDP();

        System.out.println("\n______________________________________________");
        System.out.println(" Best Costs from Stage-0 Nodes");
        System.out.println("______________________________________________");
        for (int k = 0; k < stageCount[0]; k++) {
            int u = stageStart[0] + k;
            if (bestCost[u] >= INF / 2) {
                System.out.println("Node " + u + ": Unreachable");
            } else {
                System.out.printf("Node %d: Cost = %.2f%n", u, bestCost[u]);
            }
        }

        System.out.print("\nEnter a source node ID from Stage 0 to print path (-1 to skip): ");
        int src = sc.nextInt();
        if (src != -1) {
            printPath(src);
        }

        System.out.print("\nEnter number of live updates (0 to finish): ");
        int q = sc.nextInt();

        for (int update = 1; update <= q; update++) {
            System.out.println("\nUpdate " + update + ":");
            System.out.print("Enter edge update (u v multiplier): ");

            int u = sc.nextInt();
            int v = sc.nextInt();
            double multiplier = sc.nextDouble();

            if (u < 0 || u >= nodes || v < 0 || v >= nodes) {
                System.out.println("Invalid node ID. Update skipped.");
                continue;
            }

            if (multiplier < 0) {
                System.out.println("Multiplier cannot be negative. Update skipped.");
                continue;
            }

            boolean edgeFound = false;
            for (Edge e : adj.get(u)) {
                if (e.to == v) {
                    e.curCost = e.baseCost * multiplier;
                    edgeFound = true;

                    System.out.println("\nEdge " + u + " -> " + v + " updated successfully.");
                    System.out.printf("Base Cost    = %.2f%n", e.baseCost);
                    System.out.printf("Multiplier   = %.2f%n", multiplier);
                    System.out.printf("Current Cost = %.2f%n", e.curCost);
                }
            }

            if (!edgeFound) {
                System.out.println("Error: Edge " + u + " -> " + v + " does not exist.");
                continue;
            }

            calculateDP();
            System.out.println("\nAfter Update " + update + ", Best Costs:");
            for (int k = 0; k < stageCount[0]; k++) {
                int node = stageStart[0] + k;
                if (bestCost[node] >= INF / 2) {
                    System.out.println("Node " + node + ": Unreachable");
                } else {
                    System.out.printf("Node %d: Cost = %.2f%n", node, bestCost[node]);
                }
            }
        }

        System.out.println("\n______________________________________________");
        System.out.println(" Final Results After All Updates");
        System.out.println("______________________________________________");
        for (int k = 0; k < stageCount[0]; k++) {
            int node = stageStart[0] + k;
            if (bestCost[node] >= INF / 2) {
                System.out.println("Node " + node + ": Unreachable");
            } else {
                System.out.printf("Node %d: Minimum Cost = %.2f%n", node, bestCost[node]);
            }
        }

        System.out.print("\nEnter Stage-0 source node to print final optimal path (-1 to exit): ");
        src = sc.nextInt();
        if (src != -1) {
            printPath(src);
        }

        sc.close();
    }
}
