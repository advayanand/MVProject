import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

    }
    public ArrayList<AmazonReview> parseDatatset(String filename){
        Scanner scanner;
        ArrayList<AmazonReview> amazonReviews = new ArrayList<>();
        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
               String [] amazonReview = line.split("\t");
               boolean verifiedPurchase = false;
               if (amazonReview[11].equals("Y")) verifiedPurchase = true;
               amazonReviews.add(new AmazonReview(amazonReview[13],amazonReview[12],amazonReview[5],amazonReview[6], amazonReview[3],amazonReview[1], amazonReview[14], Long.parseLong(amazonReview[8]),Long.parseLong(amazonReview[7]), Long.parseLong(amazonReview[9]), verifiedPurchase));
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
            return null;
        }

        return amazonReviews;
    }
}

