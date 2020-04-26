package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;

public class PremiumOffer extends RegularOffer {

    private double discount;
    private static final double ROUND_NUMBER = 100;

    public PremiumOffer(String productName, LocalDate date, String description,
                        double price, double shippingPrice, double discount) {
        super(productName, date, description, price, shippingPrice);
        this.discount = Math.round(discount * ROUND_NUMBER) / ROUND_NUMBER;
        double totalPrice = getTotalPrice();
        setTotalPrice(totalPrice);
    }

    @Override
    public double getTotalPrice() {
        double totalPrice = super.getTotalPrice();
        totalPrice = totalPrice - totalPrice * (discount / ROUND_NUMBER);
        return totalPrice;
    }

    @Override
    public String toString() {
        return super.toString() + "; " + discount;
    }
}
