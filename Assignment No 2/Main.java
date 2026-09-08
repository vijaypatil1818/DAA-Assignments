import java.io.*;
import java.util.*;

class Movie {
    String title;
    int year;
    double rating;
    int voteCount;
}

public class Main {

    static final int MAX = 10000;
    static Movie[] movies = new Movie[MAX];
    static int count = 0;

    static {
        for (int i = 0; i < MAX; i++)
            movies[i] = new Movie();
    }

    static void swapMovies(int i, int j) {
        Movie temp = movies[i];
        movies[i] = movies[j];
        movies[j] = temp;
    }

    static int partition(int low, int high, int sortBy) {

        double pivot;

        if (sortBy == 1)
            pivot = movies[high].rating;
        else if (sortBy == 2)
            pivot = movies[high].year;
        else
            pivot = movies[high].voteCount;

        int i = low - 1;

        for (int j = low; j < high; j++) {

            double value;

            if (sortBy == 1)
                value = movies[j].rating;
            else if (sortBy == 2)
                value = movies[j].year;
            else
                value = movies[j].voteCount;

            if (value >= pivot) {
                i++;
                swapMovies(i, j);
            }
        }

        swapMovies(i + 1, high);

        return i + 1;
    }

    static void quicksort(int low, int high, int sortBy) {

        if (low < high) {

            int p = partition(low, high, sortBy);

            quicksort(low, p - 1, sortBy);
            quicksort(p + 1, high, sortBy);
        }
    }

    static String[] splitLine(String line, int maxFields) {

        String[] fields = new String[maxFields];

        for (int i = 0; i < maxFields; i++)
            fields[i] = "";

        int idx = 0;
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            }
            else if (c == ',' && !inQuotes) {

                if (idx < maxFields)
                    fields[idx++] = field.toString();

                field.setLength(0);
            }
            else {
                field.append(c);
            }
        }

        if (idx < maxFields)
            fields[idx++] = field.toString();

        return fields;
    }

    public static void main(String[] args) {

        String filePath = "E:\\Vijay\\sem 5\\DAA\\movies.csv";

        try {

            BufferedReader file =
                    new BufferedReader(new FileReader(filePath));

            String line;

            // Skip header
            file.readLine();

            final int NUM_COLS = 8;

            while ((line = file.readLine()) != null && count < MAX) {

                String[] fields = splitLine(line, NUM_COLS);

                try {

                    String title = fields[2];
                    String releaseDate = fields[4];
                    String ratingStr = fields[6];
                    String voteCountStr = fields[7];

                    if (title.isEmpty() || releaseDate.length() < 4)
                        continue;

                    int year = Integer.parseInt(
                            releaseDate.substring(0, 4)
                    );

                    double rating = Double.parseDouble(ratingStr);

                    int voteCount = voteCountStr.isEmpty()
                            ? 0
                            : (int) Double.parseDouble(voteCountStr);

                    movies[count].title = title;
                    movies[count].year = year;
                    movies[count].rating = rating;
                    movies[count].voteCount = voteCount;

                    count++;

                } catch (Exception e) {
                    continue;
                }
            }

            file.close();

        } catch (IOException e) {

            System.out.println(
                    "Could not open file. Put the CSV in the same folder."
            );

            return;
        }

        System.out.println("Loaded " + count + " movies.");

        Scanner sc = new Scanner(System.in);

        int choice;

        System.out.print(
                "Sort by: 1-Rating  2-Year  3-Vote Count\nEnter choice: "
        );

        choice = sc.nextInt();

        quicksort(0, count - 1, choice);

        System.out.println("\nTop 10 Movies:");

        for (int i = 0; i < 10 && i < count; i++) {

            System.out.println(
                    movies[i].title + " (" + movies[i].year + ") | "
                    + "Rating: " + movies[i].rating + " | "
                    + "Vote Count: " + movies[i].voteCount
            );
        }

        sc.close();
    }
}
