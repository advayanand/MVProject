import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AmazonReview {
    private String reviewText, reviewHeadline, productTitle, productCategory, productID, customerID, reviewDate, reviewID;
    private long helpfulVotes, starRating, totalVotes;
    private boolean isVerifiedPurchased;
    private boolean isReal;
    private String[] words;
    private ArrayList<String> wordsSpecificToProduct;
    public static ArrayList<String> positiveWords = new ArrayList<>();
    public static ArrayList<String> negativeWords = new ArrayList<>();
    private boolean reviewTextSet = false;
    private boolean productNameSet = false;
    private boolean productCategorySet = false;
    private boolean reviewIDset = false;

    public AmazonReview() {       //String reviewText, String reviewHeadline, String productTitle, String productCategory, String productID, String customerID, String reviewDate, long helpfulVotes, long starRating, long totalVotes, boolean isVerifiedPurchased, String reviewID) {
//        this.customerID = customerID;
//        this.reviewDate = reviewDate;
//        this.reviewHeadline = reviewHeadline;
//        this.reviewText = reviewText;
//        this.productTitle = productTitle;
//        this.productCategory = productCategory;
//        this.productID = productID;
//        this.helpfulVotes = helpfulVotes;
//        this.starRating = starRating;
//        this.totalVotes = totalVotes;
//        this.isVerifiedPurchased = isVerifiedPurchased;
//        this.reviewID = reviewID;
//
        if (reviewTextSet) {
            this.words = getWords();
        }
        if (productNameSet && productCategorySet) {
            this.wordsSpecificToProduct = getProductSpecificWords();
        }

        if (positiveWords.size() == 0) {
            loadSentimentFiles(positiveWords, "data/positive-words.txt");
        }
        if (negativeWords.size() == 0) {
            loadSentimentFiles(negativeWords, "data/negative-words.txt");
        }
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

    public void setTruth(boolean isReal) {
        this.isReal = isReal;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
        reviewTextSet = true;
        if (reviewTextSet) {
            this.words = getWords();
        }
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setHelpfulVotes(long helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
        productCategorySet = true;
        if (productNameSet && productCategorySet) {
            this.wordsSpecificToProduct = getProductSpecificWords();
        }
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
        productNameSet = true;
        if (productNameSet && productCategorySet) {
            this.wordsSpecificToProduct = getProductSpecificWords();
        }
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
        reviewIDset = true;
    }

    public void setReviewHeadline(String reviewHeadline) {
        this.reviewHeadline = reviewHeadline;
    }

    public void setStarRating(long starRating) {
        this.starRating = starRating;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public void setVerifiedPurchased(boolean verifiedPurchased) {
        isVerifiedPurchased = verifiedPurchased;
    }


    private String[] getWords() {
        String[] words = getReviewText().split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].toLowerCase();
            words[i] = stripPunctuation(words[i]);
        }
        return words;
    }

    public String[] getAllWords() {
        return words;
    }

    private String stripPunctuation(String word) {
        String output = "";
        for (int i = 0; i < word.length(); i++) {
            if ("abcdefghijklmnopqrstuvwxyz'-".contains(word.substring(i, i + 1))) {
                output += word.substring(i, i + 1);
            }
        }
        return output;
    }


    private String getSentiment() {
        int positiveWordCount = 0;
        int negativeWordCount = 0;
        for (String word : positiveWords) {
            positiveWordCount += countWord(word);
        }
        for (String word : negativeWords) {
            negativeWordCount += countWord(word);
        }
        // System.out.println("Positive word count is "+positiveWordCount);
        //System.out.println("negative word count is "+ negativeWordCount);
        if (positiveWordCount == 0 && negativeWordCount == 0) return "neutral";
        if (positiveWordCount == 0) return "extremely negative";
        if (negativeWordCount == 0) return "extremely positive";
        double percent = (double) positiveWordCount / (double) negativeWordCount;
        if (0.8 < percent && percent < 1.25) {
            return "neutral";
        } else if (percent > 2) {
            return "extremely positive";
        } else if (percent < 0.5) {
            return "extremely negative";
        } else if (percent > 1.25) {
            return "positive";
        } else if (percent < 0.8) {
            return "negative";
        }
        return "null";
    }

    public long differenceBetweenSentimentAndStarRating() {
        String sentiment = getSentiment();
        long predictedRating = 0;
        if (sentiment.equals("extremely positive")) {
            predictedRating = 5;
        } else if (sentiment.equals("positive")) {
            predictedRating = 4;
        } else if (sentiment.equals("neutral")) {
            predictedRating = 3;
        } else if (sentiment.equals("negative")) {
            predictedRating = 2;
        } else if (sentiment.equals("extremely negative")) {
            predictedRating = 1;
        }
        return Math.abs(predictedRating - getStarRating());
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
        if (reviewTextSet) {
            return reviewText;
        }
        return "";
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
        if (reviewIDset) {
            return reviewID;
        } else {
            return "null";
        }
    }

    public String getTruthOfReview() {
        return isReal ? "real" : "fake";
    }

    public boolean getVerifiedPurcahse() {
        return isVerifiedPurchased;
    }

    public int countWord(String word) {
        if (!reviewTextSet) return 0;
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
