import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FractionalKnapsack {

    static class Item {
        String name;
        double weight;
        double value;
        boolean divisible;
        int priority;

        Item(String name, double weight, double value, boolean divisible, int priority) {
            this.name = name;
            this.weight = weight;
            this.value = value;
            this.divisible = divisible;
            this.priority = priority;
        }

        double valuePerWeight() {
            return value / weight;
        }
    }

    static Comparator<Item> itemComparator = (a, b) -> {
        if (a.priority == b.priority) {
            return Double.compare(b.valuePerWeight(), a.valuePerWeight());
        }
        return Integer.compare(a.priority, b.priority);
    };

    static double fractionalKnapsack(List<Item> items, double capacity, double[] totalWeightCarried) {
        items.sort(itemComparator);

        System.out.println("\nSorted Items (by Priority, then Value/Weight):");
        System.out.printf("%-20s %-10s %-10s %-12s %-15s %-15s%n",
                "Item", "Weight", "Value", "Priority", "Value/Weight", "Type");

        for (Item item : items) {
            System.out.printf("%-20s %-10.2f %-10.2f %-12d %-15.2f %-15s%n",
                    item.name, item.weight, item.value, item.priority,
                    item.valuePerWeight(), item.divisible ? "Divisible" : "Indivisible");
        }

        double totalValue = 0.0;
        totalWeightCarried[0] = 0.0;

        System.out.println("\nItems selected for transport:");

        for (Item item : items) {
            if (capacity <= 0) {
                break;
            }

            if (item.divisible) {
                double takenWeight = Math.min(item.weight, capacity);
                double takenValue = item.valuePerWeight() * takenWeight;
                totalValue += takenValue;
                capacity -= takenWeight;
                totalWeightCarried[0] += takenWeight;

                System.out.printf(" - %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Divisible%n",
                        item.name, takenWeight, takenValue, item.priority);
            } else if (item.weight <= capacity) {
                totalValue += item.value;
                capacity -= item.weight;
                totalWeightCarried[0] += item.weight;

                System.out.printf(" - %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Indivisible%n",
                        item.name, item.weight, item.value, item.priority);
            }
        }

        return totalValue;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of relief items: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        List<Item> items = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nItem #" + (i + 1) + ":");

            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Weight (kg): ");
            double weight = scanner.nextDouble();

            System.out.print("Utility Value: ");
            double value = scanner.nextDouble();

            System.out.print("Is it divisible? (1 = Yes, 0 = No): ");
            int divisibleInt = scanner.nextInt();

            System.out.print("Priority (1 = High, 2 = Medium, 3 = Low): ");
            int priority = scanner.nextInt();
            scanner.nextLine();

            items.add(new Item(name, weight, value, divisibleInt == 1, priority));
        }

        System.out.print("\nEnter maximum weight capacity of the boat (in kg): ");
        double capacity = scanner.nextDouble();

        double[] totalWeightCarried = new double[1];
        double maxValue = fractionalKnapsack(items, capacity, totalWeightCarried);

        System.out.println("\n===== Final Report =====");
        System.out.printf("Total weight carried: %.2f kg%n", totalWeightCarried[0]);
        System.out.printf("Total utility value carried: %.2f units%n", maxValue);

        scanner.close();
    }
}
