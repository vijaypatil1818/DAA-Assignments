import java.util.*;

public class TSP {
    private static final int INF = 1_000_000_000;

    static class Node {
        int[][] mat;
        ArrayList<Integer> path;
        int bound;

        Node(int[][] mat, ArrayList<Integer> path, int bound) {
            this.mat = mat;
            this.path = path;
            this.bound = bound;
        }
    }

    // Reduce each row and column; the reduction contributes to the lower bound.
    private static int reduceMatrix(int[][] a) {
        int n = a.length;
        int reduction = 0;

        for (int i = 0; i < n; i++) {
            int min = INF;
            for (int j = 0; j < n; j++) {
                min = Math.min(min, a[i][j]);
            }
            if (min == INF) {
                continue;
            }
            reduction += min;
            for (int j = 0; j < n; j++) {
                if (a[i][j] != INF) {
                    a[i][j] -= min;
                }
            }
        }

        for (int j = 0; j < n; j++) {
            int min = INF;
            for (int i = 0; i < n; i++) {
                min = Math.min(min, a[i][j]);
            }
            if (min == INF) {
                continue;
            }
            reduction += min;
            for (int i = 0; i < n; i++) {
                if (a[i][j] != INF) {
                    a[i][j] -= min;
                }
            }
        }

        return reduction;
    }

    private static int[][] copyMatrix(int[][] matrix) {
        int n = matrix.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }

    private static void solveTSP(int[][] cost) {
        int n = cost.length;

        int[][] rootMatrix = copyMatrix(cost);
        ArrayList<Integer> rootPath = new ArrayList<>();
        rootPath.add(0);

        Node root = new Node(rootMatrix, rootPath, 0);
        root.bound = reduceMatrix(root.mat);

        PriorityQueue<Node> pq =
                new PriorityQueue<>(Comparator.comparingInt(node -> node.bound));
        pq.offer(root);

        int best = INF;
        ArrayList<Integer> answer = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.bound >= best) {
                continue;
            }

            int u = cur.path.get(cur.path.size() - 1);

            if (cur.path.size() == n) {
                if (cost[u][0] == INF) {
                    continue;
                }

                int total = cost[u][0];
                for (int k = 1; k < n; k++) {
                    total += cost[cur.path.get(k - 1)][cur.path.get(k)];
                }

                if (total < best) {
                    best = total;
                    answer = new ArrayList<>(cur.path);
                }
                continue;
            }

            for (int v = 1; v < n; v++) {
                if (cur.path.contains(v) || cur.mat[u][v] == INF) {
                    continue;
                }

                Node child = new Node(
                        copyMatrix(cur.mat),
                        new ArrayList<>(cur.path),
                        cur.bound
                );
                child.path.add(v);
                child.bound += cur.mat[u][v];

                for (int j = 0; j < n; j++) {
                    child.mat[u][j] = INF;
                }
                for (int i = 0; i < n; i++) {
                    child.mat[i][v] = INF;
                }
                if (child.path.size() < n) {
                    child.mat[v][0] = INF;
                }

                child.bound += reduceMatrix(child.mat);

                if (child.bound < best) {
                    pq.offer(child);
                }
            }
        }

        if (best == INF) {
            System.out.println("No possible tour");
        } else {
            System.out.println();
            System.out.println("Minimum cost: " + best);
            System.out.print("Route: ");
            for (int city : answer) {
                System.out.print(city + " -> ");
            }
            System.out.println("0");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of cities: ");
        int n = scanner.nextInt();

        if (n < 2) {
            System.out.println("No possible tour");
            scanner.close();
            return;
        }

        int[][] cost = new int[n][n];
        System.out.println("Enter cost matrix (0 for no road):");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = scanner.nextInt();
                if (i == j || value <= 0) {
                    cost[i][j] = INF;
                } else {
                    cost[i][j] = value;
                }
            }
        }

        solveTSP(cost);
        scanner.close();
    }
}
