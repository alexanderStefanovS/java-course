package bg.sofia.uni.fmi.mjt.shopping.portal;

import java.time.LocalDate;

public class PriceStatistic {

    private LocalDate date;
    private double lowestPrice;
    private String productName;
    private int numberOfOffers;
    private double averagePrice;

    public PriceStatistic(LocalDate date, String productName) {
        this.date = date;
        this.productName = productName;
        lowestPrice = 0;
        numberOfOffers = 0;
        averagePrice = 0;
    }

    /**
     * Calculates the average price
     *
     * @param price - the new price
     */
    private void calculateAveragePrice(double price) {
        double lastSum = averagePrice * numberOfOffers;
        numberOfOffers++;
        averagePrice = (lastSum + price) / numberOfOffers;
    }

    /**
     * Adds price with setting the lowest price
     * and calculating the averages price
     *
     * @param price - the new price
     */
    public void addPrice(double price) {
        if (numberOfOffers == 0) {
            lowestPrice = price;
        } else {
            if (price < lowestPrice) {
                lowestPrice = price;
            }
        }
        calculateAveragePrice(price);
    }

    /**
     * Returns the date for which the statistic is
     * collected.
     */
    public LocalDate getDate() {
        if (date == null) {
            throw new UnsupportedOperationException();
        }
        return date;
    }

    /**
     * Returns the lowest total price from the offers
     * for this product for the specific date.
     */
    public double getLowestPrice() {
        if (lowestPrice == 0) {
            throw new UnsupportedOperationException();
        }
        return lowestPrice;
    }

    /**
     * Return the average total price from the offers
     * for this product for the specific date.
     */
    public double getAveragePrice() {
        if (averagePrice == 0) {
            throw new UnsupportedOperationException();
        }
        return averagePrice;
    }

    @Override
    public String toString() {
        String stat = "[" + date + "; " + productName + "; " + averagePrice + "; " + lowestPrice + "; " + numberOfOffers + "]";
        return stat;
    }
}
