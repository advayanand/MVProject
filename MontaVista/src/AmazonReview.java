public class AmazonReview {
    private String reviewText, reviewHeadline, productTitle, productCategory, productID, customerID, reviewDate;
    private long helpfulVotes, starRating, totalVotes;
    private boolean isVerifiedPurchased;

    public AmazonReview(String reviewText, String reviewHeadline, String productTitle, String productCategory, String productID, String customerID, String reviewDate, long helpfulVotes, long starRating, long totalVotes, boolean isVerifiedPurchased) {
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

    public boolean getVerifiedPurcahse() {
        return isVerifiedPurchased;
    }
}
