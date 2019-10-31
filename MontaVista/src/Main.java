import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final int REAL_REVIEW_THRESHOLD = 15;

    public static void main(String[] args) {
//ArrayList<AmazonReview> allAmazonReviews = parseDataset("data/AmazonReviewDataset.tsv");
//        for (AmazonReview review:allAmazonReviews) {
//            System.out.print("The review is predicted to be " +testForFakeReview(review,allAmazonReviews));
//            System.out.println("\tStar Rating: "+review.getStarRating()+"\t" +review.getReviewText());
////            System.out.println(review.getSentiment());
////            for (int i = 0; i <review.getAllWords().length ; i++) {
////                System.out.print(review.getAllWords()[i]+" ");
////            }
////            System.out.println();
//        }
       findPercentAccuracy("data/amazon_reviews.txt");
        //getUserInput();
    }
    public static void getUserInput(){
        ArrayList<AmazonReview> reviews = new ArrayList<>();
        AmazonReview review = new AmazonReview();
        Scanner sc = new Scanner(System.in);
        System.out.println("What is the name of the product you are writing a review for?");
        String productName = sc.nextLine();
        review.setProductTitle(productName);
        System.out.println("What is the product category your product falls under?\nEx. Toys, Electronics, Furniture, etc.");
        String productCategory = sc.nextLine();
        review.setProductCategory(productCategory);
        System.out.println("What star rating would you like to give the product? \nPlease enter a number from 1 to 5");
        int starRating = sc.nextInt();
        review.setStarRating(starRating);
        System.out.println("Please type your review.\nPressing enter will submit your review.");
        String r = sc.next();
        review.setReviewText(r);
        reviews.add(review);
        System.out.println("Your review is predicted to be "+testForFakeReview(review,reviews));
    }
    public static void findPercentAccuracy(String filename){
        ArrayList<AmazonReview> allAmazonReviews2 = parseDataset(filename);
        double totalReviews =0;
        double numberCorrect = 0;
        double totalRealReviews = 0;
        double totalFakeReviews = 0;
        double correctRealReviews = 0;
        double correctFakeReviews = 0;
        for (AmazonReview review : allAmazonReviews2) {
            String predictedTruth = testForFakeReview(review, allAmazonReviews2);
            // System.out.print("The review is predicted to be " + predictedTruth+"\tIt is actually "+review.getTruthOfReview());
            //System.out.println("\tStar Rating: " + review.getStarRating() + "\t" + review.getReviewText());
            if (predictedTruth.equals(review.getTruthOfReview())){
                numberCorrect++;
                if (predictedTruth.equals("real")){
                    correctRealReviews++;
                }else{
                    correctFakeReviews++;
                }
            }else{//prints the reviews that are predicted wrong
                  //System.out.println("The review is predicted to be " + predictedTruth+"\tIt is actually "+review.getTruthOfReview()+"\t"+review.getProductTitle()+"\t"+review.getProductCategory()+"\t "+review.getReviewText());
            }
            //System.out.println("The review is predicted to be " + predictedTruth+"\tIt is actually "+review.getTruthOfReview()+"\t "+review.getReviewText());
            totalReviews++;
            if (review.getTruthOfReview().equals("real")){
                totalRealReviews++;
            }else{
                totalFakeReviews++;
            }
//            System.out.println(review.getSentiment());
//            for (int i = 0; i <review.getAllWords().length ; i++) {
//                System.out.print(review.getAllWords()[i]+" ");
//            }
//            System.out.println();
        }
        System.out.println("The overall percent accuracy is "+ (numberCorrect/totalReviews)*100);
        System.out.println("The percent accuracy for real reviews is "+(correctRealReviews/totalRealReviews)*100);
        System.out.println("The percent accuracy for fake reviews is "+(correctFakeReviews/totalFakeReviews)*100);
        System.out.println("Number of fake reviews guessed correctly: "+correctFakeReviews);
        System.out.println("Number of fake reviews: "+totalFakeReviews);
        System.out.println("Number of real reviews guessed correctly: "+correctRealReviews);
        System.out.println("Number of real reviews: "+totalRealReviews);
    }

    public static ArrayList<AmazonReview> parseDataset(String filename) {
        Scanner scanner;
        ArrayList<AmazonReview> amazonReviews = new ArrayList<>();
        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            scanner.nextLine();
            int index = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] amazonReview = line.split("\t");
                amazonReviews.add(new AmazonReview());
                if (filename.contains("reviews")) {
                    addVariablesFromLabeledDataset(amazonReview, amazonReviews, index);
                } else {
                    addVariablesFromUnlabeledDataset(amazonReview, amazonReviews, index);
                }
                index++;
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
            return null;
        }

        return amazonReviews;
    }

    public static void addVariablesFromLabeledDataset(String[] amazonReview, ArrayList<AmazonReview> amazonReviews, int index) {
        boolean verifiedPurchase = amazonReview[3].equals("Y");
        boolean isReal = amazonReview[1].equals("__label2__");
        amazonReviews.get(index).setTruth(isReal);
        amazonReviews.get(index).setReviewText(amazonReview[8]);
        amazonReviews.get(index).setReviewHeadline(amazonReview[7]);
        amazonReviews.get(index).setProductTitle(amazonReview[6]);
        amazonReviews.get(index).setProductCategory(amazonReview[4]);
        amazonReviews.get(index).setProductID(amazonReview[5]);
        amazonReviews.get(index).setStarRating(Long.parseLong(amazonReview[2]));
        amazonReviews.get(index).setVerifiedPurchased(verifiedPurchase);
    }

    public static void addVariablesFromUnlabeledDataset(String[] amazonReview, ArrayList<AmazonReview> amazonReviews, int index) {
        boolean verifiedPurchase = amazonReview[11].equals("Y");
        // amazonReviews.add(new AmazonReview(amazonReview[13],amazonReview[12],amazonReview[5],amazonReview[6], amazonReview[3],amazonReview[1], amazonReview[14], Long.parseLong(amazonReview[8]),Long.parseLong(amazonReview[7]), Long.parseLong(amazonReview[9]), verifiedPurchase,amazonReview[2]));

        amazonReviews.get(index).setReviewText(amazonReview[13]);
        amazonReviews.get(index).setReviewHeadline(amazonReview[12]);
        amazonReviews.get(index).setProductTitle(amazonReview[5]);
        amazonReviews.get(index).setProductCategory(amazonReview[6]);
        amazonReviews.get(index).setProductID(amazonReview[3]);
        amazonReviews.get(index).setCustomerID(amazonReview[1]);
        amazonReviews.get(index).setReviewDate(amazonReview[14]);
        amazonReviews.get(index).setHelpfulVotes(Long.parseLong(amazonReview[8]));
        amazonReviews.get(index).setStarRating(Long.parseLong(amazonReview[7]));
        amazonReviews.get(index).setTotalVotes(Long.parseLong(amazonReview[9]));
        amazonReviews.get(index).setVerifiedPurchased(verifiedPurchase);
        amazonReviews.get(index).setReviewID(amazonReview[2]);
    }

    public static String testForFakeReview(AmazonReview review, ArrayList<AmazonReview> allAmazonReviews) {
        int counter = 0;
        counter += 12 * review.hasProductSpecificWordsNTimes();  // detect if review uses words from the product name or product category
        if (!allAmazonReviews.get(0).getReviewID().equals("null")) {  //if the review has review id's then check if they match, if there is no review id, just don't check for duplicate reviews as the review will be checked against itself
            for (AmazonReview review1 : allAmazonReviews) { // detect if the review is copy pasted from another review

                if (review1.getReviewText().equals(review.getReviewText()) && !review1.getReviewID().equals(review.getReviewID())) {
                    return "true";
                }
            }
        }
        counter += 3*review.getHelpfulVotes();

        if (review.differenceBetweenSentimentAndStarRating() >= 2) { // detect if the sentiment of the review matches the star rating assigned
            counter -= 6;
        } else {
            counter+=4;
        }
        counter += review.getAllWords().length / 8; //Increase score proportional to the length of the review

        //System.out.print(counter + " ");
        return counter < REAL_REVIEW_THRESHOLD ? "fake" : "real";

    }

}

