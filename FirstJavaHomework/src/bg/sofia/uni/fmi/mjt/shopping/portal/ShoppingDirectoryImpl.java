package bg.sofia.uni.fmi.mjt.shopping.portal;

import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.NoOfferFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;

import java.time.LocalDate;
import java.util.*;

public class ShoppingDirectoryImpl implements ShoppingDirectory {

    private HashMap<String, TreeSet<Offer>> productsOffersMap;
    private HashMap<String, TreeMap<LocalDate, PriceStatistic>> productsPriceStatisticMap;

    public ShoppingDirectoryImpl() {
        productsOffersMap = new HashMap<>();
        productsPriceStatisticMap = new HashMap<>();
    }

    @Override
    public Collection<Offer> findAllOffers(String productName) throws ProductNotFoundException {
        if (productName == null) {
            throw new IllegalArgumentException();
        }
        if (productsOffersMap.containsKey(productName)) {
            TreeSet<Offer> sortedOffers = productsOffersMap.get(productName);
            return sortedOffers;
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public Offer findBestOffer(String productName) throws ProductNotFoundException, NoOfferFoundException {
        if (productName == null) {
            throw new IllegalArgumentException();
        }
        if (productsOffersMap.containsKey(productName)) {
            TreeSet<Offer> sortedOffers = productsOffersMap.get(productName);
            if (sortedOffers == null || sortedOffers.isEmpty()) {
                throw new NoOfferFoundException();
            }
            Offer bestOffer;
            bestOffer = sortedOffers.iterator().next();
            if (bestOffer == null) {
                throw new NoOfferFoundException();
            }
            return bestOffer;
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public Collection<PriceStatistic> collectProductStatistics(String productName) throws ProductNotFoundException {
        if (productName == null) {
            throw new IllegalArgumentException();
        }
        boolean containsProduct = productsPriceStatisticMap.containsKey(productName);
        if (containsProduct) {
            Collection<PriceStatistic> statistics = productsPriceStatisticMap.get(productName).values();
            return statistics;
        } else {
            throw new ProductNotFoundException();
        }
    }

    private void addToPriceStatistics(Offer offer) {
        String productName = offer.getProductName();
        double price = offer.getTotalPrice();
        LocalDate offerDate = offer.getDate();
        if (!productsPriceStatisticMap.containsKey(productName)) {
            productsPriceStatisticMap.put(productName, new TreeMap<>(Comparator.reverseOrder()));
        }
        if (!productsPriceStatisticMap.get(productName).containsKey(offerDate)) {
            PriceStatistic stat = new PriceStatistic(offerDate, productName);
            stat.addPrice(price);
            productsPriceStatisticMap.get(productName).put(offerDate, stat);
        } else {
            PriceStatistic stat = productsPriceStatisticMap.get(productName).get(offerDate);
            stat.addPrice(price);
            productsPriceStatisticMap.get(productName).put(offerDate, stat);
        }
    }

    @Override
    public void submitOffer(Offer offer) throws OfferAlreadySubmittedException {
        if (offer == null) {
            throw new IllegalArgumentException();
        }
        String productName = offer.getProductName();
        LocalDate offerDate = offer.getDate();
        boolean containsProduct = productsOffersMap.containsKey(productName);
        if (!containsProduct) {
            productsOffersMap.put(productName, new TreeSet<>());
        }
        if (validateDate(offerDate)) {
            if (!productsOffersMap.get(productName).add(offer)) {
                throw new OfferAlreadySubmittedException();
            }
        }
        addToPriceStatistics(offer);
    }

    boolean validateDate(LocalDate date) {
        LocalDate todayDate = LocalDate.now();
        LocalDate before30DaysDate = todayDate.minusDays(30);
        boolean isValid = date.isAfter(before30DaysDate);
        return isValid;
    }
}
