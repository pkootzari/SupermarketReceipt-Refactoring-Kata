package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class Receipt {
    private List<ReceiptItem> items = new ArrayList<>();
    private List<Discount> discounts = new ArrayList<>();

    public Double getTotalPrice() {
        double total = 0.0;
        for (ReceiptItem item : this.items) {
            total += item.getTotalPrice();
        }
        for (Discount discount : this.discounts) {
            total += discount.getDiscountAmount();
        }
        return total;
    }

    public void addProduct(Product p, double quantity, double price, double totalPrice) {
        this.items.add(new ReceiptItem(p, quantity, price, totalPrice));
    }

    public List<ReceiptItem> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addDiscount(Discount discount) {
        this.discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    private double get_product_quantity(Product p) {
        double quantity = 0;
        for (ReceiptItem item : items)
            if (p == item.getProduct())
                quantity += item.getQuantity();
        return quantity;
    }

    private Map<Product, Double> get_prodcut_quantity_map() {
        Map<Product, Double> productQuantities = new HashMap<>();
        for (ReceiptItem item: items) {
            Product product = item.getProduct();
            if (productQuantities.containsKey(product)) {
                productQuantities.put(product, productQuantities.get(product) + item.getQuantity());
            } else {
                productQuantities.put(product, item.getQuantity());
            }
        }
        return productQuantities;
    }

    void handleOffers(Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product product : get_prodcut_quantity_map().keySet()) {
            double quantity = get_product_quantity(product);
            if (offers.containsKey(product)) {
                Offer offer = offers.get(product);
                double unitPrice = catalog.getUnitPrice(product);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int x = 1;
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    x = 3;

                } else if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    x = 2;
                    if (quantityAsInt >= 2) {
                        int intDivision = quantityAsInt / x;
                        double pricePerUnit = offer.argument * intDivision;
                        double theTotal = (quantityAsInt % 2) * unitPrice;
                        double total = pricePerUnit + theTotal;
                        double discountN = unitPrice * quantity - total;
                        discount = new Discount(product, "2 for " + offer.argument, -discountN);
                    }

                }
                if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    x = 5;
                }
                int numberOfXs = quantityAsInt / x;
                if (offer.offerType == SpecialOfferType.ThreeForTwo && quantityAsInt > 2) {
                    double discountAmount = quantity * unitPrice
                            - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                    discount = new Discount(product, "3 for 2", -discountAmount);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(product, offer.argument + "% off",
                            -quantity * unitPrice * offer.argument / 100.0);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
                    double discountTotal = unitPrice * quantity
                            - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                    discount = new Discount(product, x + " for " + offer.argument, -discountTotal);
                }
                if (discount != null)
                    addDiscount(discount);
            }

        }
    }
}
