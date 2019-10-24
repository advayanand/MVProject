import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final int REAL_REVIEW_THRESHOLD = 15;

    public static void main(String[] args) {
ArrayList<AmazonReview> allAmazonReviews = parseDataset("data/AmazonReviewDataset.tsv");
        for (AmazonReview review:allAmazonReviews) {
            System.out.print(testForFakeReview(review,allAmazonReviews));
            System.out.println("\t"+review.getStarRating()+"\t" +review.getReviewText());
//            System.out.println(review.getSentiment());
//            for (int i = 0; i <review.getAllWords().length ; i++) {
//                System.out.print(review.getAllWords()[i]+" ");
//            }
//            System.out.println();
        }

    }
    public static ArrayList<AmazonReview> parseDataset(String filename){
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
               amazonReviews.add(new AmazonReview(amazonReview[13],amazonReview[12],amazonReview[5],amazonReview[6], amazonReview[3],amazonReview[1], amazonReview[14], Long.parseLong(amazonReview[8]),Long.parseLong(amazonReview[7]), Long.parseLong(amazonReview[9]), verifiedPurchase,amazonReview[2]));
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
            return null;
        }

        return amazonReviews;
    }
    public static String testForFakeReview(AmazonReview review, ArrayList<AmazonReview> allAmazonReviews){
        int counter = 0;

        counter += 10*review.hasProductSpecificWordsNTimes();  // detect if review uses words from the product name or product category

        for (AmazonReview review1 : allAmazonReviews) { // detect if the review is copy pasted from another review
            if (review1.getReviewText().equals(review.getReviewText())&& !review1.getReviewID().equals(review.getReviewID())){
                return "true";
            }
        }

        counter += review.getHelpfulVotes();

        if (review.differenceBetweenSentimentAndStarRating()>=2){ // detect if the sentiment of the review matches the star rating assigned
            counter-=5;
        } else{
            counter ++;
        }
        counter += review.getAllWords().length/10; //Increase score proportional to the length of the review

        System.out.print(counter+" ");
        return counter < REAL_REVIEW_THRESHOLD ? "fake" : "true";

    }

}

