import java.io.*;
import java.util.*;

class Transaction {
    int customer_id;
    String orderId;
    String ProductInfo;
    double amount;
    String purDate;
    String location;

    Transaction(int customer_id, String orderId, String ProductInfo,
                double amount, String purDate, String location) {
        this.customer_id = customer_id;
        this.orderId = orderId;
        this.ProductInfo = ProductInfo;
        this.amount = amount;
        this.purDate = purDate;
        this.location = location;
    }
}

class Solution {

    void merge(ArrayList<Transaction> arr, int low, int mid, int high) {

        ArrayList<Transaction> temp = new ArrayList<>();

        int left = low;
        int right = mid + 1;

        while (left <= mid && right <= high) {

            if (arr.get(left).customer_id <= arr.get(right).customer_id)
                temp.add(arr.get(left++));
            else
                temp.add(arr.get(right++));
        }

        while (left <= mid)
            temp.add(arr.get(left++));

        while (right <= high)
            temp.add(arr.get(right++));

        for (int i = low; i <= high; i++)
            arr.set(i, temp.get(i - low));
    }

    void mergeSort(ArrayList<Transaction> arr, int low, int high) {

        if (low >= high)
            return;

        int mid = (low + high) / 2;

        mergeSort(arr, low, mid);
        mergeSort(arr, mid + 1, high);
        merge(arr, low, mid, high);
    }
}

public class Main {

    public static void main(String[] args) {

        ArrayList<Transaction> arr = new ArrayList<>();

        String filePath = "D:\\COLLEGE\\DAA Assignments\\customer_data.csv";

        try {
            BufferedReader file =
                    new BufferedReader(new FileReader(filePath));

            file.readLine();

            String line;

            while ((line = file.readLine()) != null) {

                String[] data = line.split(",", -1);

                Transaction t = new Transaction(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        Double.parseDouble(data[3]),
                        data[4],
                        data[5]
                );

                arr.add(t);
            }

            file.close();

        } catch (IOException e) {

            System.out.println("Error Opening File");
            return;

        } catch (NumberFormatException e) {

            System.out.println("Error parsing CSV data");
            return;
        }

        if (arr.isEmpty()) {

            System.out.println("No data was Parsed... Check your csv File");
            return;
        }

        System.out.println("Loaded.." + arr.size() + " Transactions. Sorting...");

        Solution sol = new Solution();
        sol.mergeSort(arr, 0, arr.size() - 1);

        System.out.println("Sorting Complete..");

        int dispLimit = Math.min(10, arr.size());

        for (int i = 0; i < dispLimit; i++) {

            System.out.println(
                    "Customer Id : " + arr.get(i).customer_id
                    + "| Order Id : " + arr.get(i).orderId
                    + "| Product Info : " + arr.get(i).ProductInfo
                    + "| Amount : " + arr.get(i).amount
            );
        }

        String outputFile = "sorted_customer_data.csv";

        try {

            BufferedWriter outFile =
                    new BufferedWriter(new FileWriter(outputFile));

            outFile.write(
                    "customer_id,orderId,ProductInfo,amount,purDate,location\n"
            );

            for (Transaction t : arr) {

                outFile.write(
                        t.customer_id + ","
                        + t.orderId + ","
                        + t.ProductInfo + ","
                        + t.amount + ","
                        + t.purDate + ","
                        + t.location + "\n"
                );
            }

            outFile.close();

        } catch (IOException e) {

            System.out.println("Error opening output file!");
            return;
        }

        System.out.println(
                "Sorted data successfully written to sorted_customer_data.csv"
        );
    }
}
