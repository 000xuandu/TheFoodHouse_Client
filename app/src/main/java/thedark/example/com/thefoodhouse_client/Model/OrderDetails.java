package thedark.example.com.thefoodhouse_client.Model;

/**
 * Created by The Dark on 20-Dec-2017.
 */

public class OrderDetails {
    private String ProductIdDetails, ProductNameDetails, QuantityDetails, PriceDetails, DiscountDetails, ImgDetails;

    public OrderDetails() {

    }

    public OrderDetails(String productIdDetails, String productNameDetails, String quantityDetails, String priceDetails, String discountDetails, String imgDetails) {
        ProductIdDetails = productIdDetails;
        ProductNameDetails = productNameDetails;
        QuantityDetails = quantityDetails;
        PriceDetails = priceDetails;
        DiscountDetails = discountDetails;
        ImgDetails = imgDetails;
    }

    public String getProductIdDetails() {
        return ProductIdDetails;
    }

    public void setProductIdDetails(String productIdDetails) {
        ProductIdDetails = productIdDetails;
    }

    public String getProductNameDetails() {
        return ProductNameDetails;
    }

    public void setProductNameDetails(String productNameDetails) {
        ProductNameDetails = productNameDetails;
    }

    public String getQuantityDetails() {
        return QuantityDetails;
    }

    public void setQuantityDetails(String quantityDetails) {
        QuantityDetails = quantityDetails;
    }

    public String getPriceDetails() {
        return PriceDetails;
    }

    public void setPriceDetails(String priceDetails) {
        PriceDetails = priceDetails;
    }

    public String getDiscountDetails() {
        return DiscountDetails;
    }

    public void setDiscountDetails(String discountDetails) {
        DiscountDetails = discountDetails;
    }

    public String getImgDetails() {
        return ImgDetails;
    }

    public void setImgDetails(String imgDetails) {
        ImgDetails = imgDetails;
    }
}
