import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AmazonReview {
    private String reviewText, reviewHeadline, productTitle, productCategory, productID, customerID, reviewDate, reviewID;
    private long helpfulVotes, starRating, totalVotes;
    private boolean isVerifiedPurchased;
    private String[] words;
    private ArrayList<String> wordsSpecificToProduct;
    public static ArrayList<String> positiveWords= new ArrayList<>();
    public static ArrayList<String> negativeWords = new ArrayList<>();

    public AmazonReview(String reviewText, String reviewHeadline, String productTitle, String productCategory, String productID, String customerID, String reviewDate, long helpfulVotes, long starRating, long totalVotes, boolean isVerifiedPurchased, String reviewID) {
        this.customerID = customerID;
        this.reviewDate = reviewDate;
        this.reviewHeadline = reviewHeadline;
        this.reviewText = reviewText;
        this.productTitle = productTitle;
        this.productCategory = productCategory;
        this.productID = productID;
        this.helpfulVotes = helpfulVotes;
        this.starRating = starRating;
        this.totalVotes = totalVotes;
        this.isVerifiedPurchased = isVerifiedPurchased;
        this.reviewID = reviewID;
        this.words = getReviewText().split(" ");
        this.wordsSpecificToProduct = getProductSpecificWords();
        loadSentimentFiles(positiveWords,"data/positive-words.txt");
        loadSentimentFiles(negativeWords,"data/negative-words.txt");
    }

    private void loadSentimentFiles(ArrayList<String> wordlist, String filename) {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            for (int i = 0; i < 35; i++) {
             scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                wordlist.add(line.trim());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }
    }

    private String getSentiment() {
        int positiveWordCount = 0;
        int negativeWordCount = 0;
        for (String word : positiveWords) {
            positiveWordCount += countWord(word);
        }
        for (String word : negativeWords) {
            positiveWordCount += countWord(word);
        }
        double percent = (double)positiveWordCount/(double)negativeWordCount;
        if (0.9<percent && percent<1.1){
            return "neutral";
        }else if (percent >2){
            return "extremely positive";
        }else if (percent <0.5){
            return "extremely negative";
        }else if (percent> 1.1){
            return "positive";
        }else if (percent<0.9){
            return "negative";
        }
        return "null";
    }

    private ArrayList<String> getProductSpecificWords() {
        String[] productName = getProductTitle().split(" ");
        String[] productCategory = getProductCategory().split(" ");
        ArrayList<String> output = new ArrayList<>();
        for (String word : productName) {
            output.add(word);
        }
        for (String word : productCategory) {
            output.add(word);
        }
        return output;
    }

    public String toString() {
        return "The review text is " + getReviewText() + "\nThe star rating is " + getStarRating();
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getReviewHeadline() {
        return reviewHeadline;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductID() {
        return productID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public long getHelpfulVotes() {
        return helpfulVotes;
    }

    public long getStarRating() {
        return starRating;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public String getReviewID() {
        return reviewID;
    }

    public boolean getVerifiedPurcahse() {
        return isVerifiedPurchased;
    }

    public int countWord(String word) {
        int count = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word)) {
                count++;
            }
        }
        return count;
    }

    public int hasProductSpecificWordsNTimes() {
        int count = 0;
        for (String word : getProductSpecificWords()) {
            count += countWord(word);
        }
        return count;
    }

}
