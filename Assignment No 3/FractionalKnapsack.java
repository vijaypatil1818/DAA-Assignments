import java.util.Scanner;

public class FractionalKnapsack
{
    // Sorting according to ratio (descending order)
    static void sortItems(int[] profit, int[] weight, float[] ratio, int n)
    {
        for(int i = 0; i < n - 1; i++)
        {
            for(int j = i + 1; j < n; j++)
            {
                if(ratio[i] < ratio[j])
                {
                    // Swap ratio
                    float temp = ratio[i];
                    ratio[i] = ratio[j];
                    ratio[j] = temp;

                    // Swap profit
                    int p = profit[i];
                    profit[i] = profit[j];
                    profit[j] = p;

                    // Swap weight
                    int w = weight[i];
                    weight[i] = weight[j];
                    weight[j] = w;
                }
            }
        }
    }

    static float fractionalKnapsack(int capacity, int[] profit, int[] weight,
                                    float[] ratio, int n)
    {
        float totalProfit = 0;

        System.out.println("\nSelection Process:");

        for(int i = 0; i < n; i++)
        {
            if(capacity >= weight[i])
            {
                // Take complete item
                capacity = capacity - weight[i];
                totalProfit = totalProfit + profit[i];

                System.out.println("Take full item "
                        + (i + 1)
                        + " Profit = "
                        + profit[i]
                        + " Weight = "
                        + weight[i]);
            }
            else
            {
                // Take fraction of item
                float fraction = (float) capacity / weight[i];

                float addedProfit = profit[i] * fraction;

                totalProfit = totalProfit + addedProfit;

                System.out.println("Take "
                        + (fraction * 100)
                        + "% of item "
                        + (i + 1)
                        + " Profit added = "
                        + addedProfit);

                break;
            }
        }

        return totalProfit;
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        int n;
        int capacity;

        System.out.print("Enter number of items: ");
        n = sc.nextInt();

        int[] profit = new int[n];
        int[] weight = new int[n];
        float[] ratio = new float[n];

        System.out.println("\nEnter profit and weight:");

        for(int i = 0; i < n; i++)
        {
            System.out.print("Item " + (i + 1) + " Profit: ");
            profit[i] = sc.nextInt();

            System.out.print("Item " + (i + 1) + " Weight: ");
            weight[i] = sc.nextInt();

            ratio[i] = (float) profit[i] / weight[i];
        }

        System.out.print("\nEnter knapsack capacity: ");
        capacity = sc.nextInt();

        System.out.println("\nBefore Sorting:");

        for(int i = 0; i < n; i++)
        {
            System.out.println("Item " + (i + 1)
                    + " Profit = " + profit[i]
                    + " Weight = " + weight[i]
                    + " Ratio = " + ratio[i]);
        }

        sortItems(profit, weight, ratio, n);

        System.out.println("\nAfter Sorting (by Ratio):");

        for(int i = 0; i < n; i++)
        {
            System.out.println("Item " + (i + 1)
                    + " Profit = " + profit[i]
                    + " Weight = " + weight[i]
                    + " Ratio = " + ratio[i]);
        }

        float answer = fractionalKnapsack(
                capacity,
                profit,
                weight,
                ratio,
                n
        );

        System.out.println("\nMaximum Profit = " + answer);

        sc.close();
    }
}
