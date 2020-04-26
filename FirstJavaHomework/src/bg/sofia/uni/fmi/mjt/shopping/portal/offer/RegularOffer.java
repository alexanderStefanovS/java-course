package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;
import java.util.Objects;

public class RegularOffer implements Offer {

    private String productName;
    private LocalDate date;
    private String description;
    private double price;
    private double shippingPrice;
    private double totalPrice;

    public RegularOffer(String productName, LocalDate date, String description,
                        double price, double shippingPrice) {
        this.productName = productName;
        this.date = date;
        this.description = description;
        this.price = price;
        this.shippingPrice = shippingPrice;
        this.totalPrice = this.price + this.shippingPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getShippingPrice() {
        return shippingPrice;
    }

    @Override
    public double getTotalPrice() {
        double totalPrice = price + shippingPrice;
        return totalPrice;
    }

    @Override
    public String toString() {
        return "[" + productName + ", " + date + ", " + description + ", " + price + ", " + shippingPrice + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegularOffer that = (RegularOffer) o;
        return Double.compare(that.totalPrice, totalPrice) == 0
                && productName.equals(that.productName)
                && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, date, totalPrice);
    }

    @Override
    public int compareTo(Object obj) {
        RegularOffer other = (RegularOffer) obj;
        return Double.compare(totalPrice, other.totalPrice);
    }
}
