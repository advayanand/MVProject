public class UserInput {
   private String reviewText, productCategory, productName;
   private int starRating;
    public UserInput(){}
    public void setReviewText(String review){
        reviewText=review;
    }
    public void setProductCategory(String productCat){
        productCategory = productCat;
    }
    public void setProductName(String product){
        productName = product;
    }
    public void setStarRating(int star){
        starRating = star;
    }

    public int getStarRating() {
        return starRating;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public String getReviewText() {
        return reviewText;
    }
}
