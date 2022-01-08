package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import dojo.supermarket.*;

public class Receipt {
    private List<ReceiptItem> items = new ArrayList<>();
    private List<Discount> discounts = new ArrayList<>();

    public Double getTotalPrice() {
        double total = this.items.stream().mapToDouble(item -> item.getTotalPrice()).sum();
        total -= this.discounts.stream().mapToDouble(item -> item.getDiscountAmount()).sum();
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

    private double getProductQuantity(Product p) {
        double quantity = 0;
        for (ReceiptItem item : items)
            if (p == item.getProduct())
                quantity += item.getQuantity();
        return quantity;
    }

    private Map<Product, Double> getProdcutQuantityMap() {
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

    public void handleOffers(Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product product : getProdcutQuantityMap().keySet()) {
            double quantity = getProductQuantity(product);
            if (offers.containsKey(product)) {
                Offer offer = offers.get(product);
                double unitPrice = catalog.getUnitPrice(product);
                int quantityAsInt = (int) quantity;
                Discount discount = offer.createDiscount(quantityAsInt, unitPrice);
                if (discount != null)
                    addDiscount(discount);
            }

        }
    }

    public String present(int columns) {
        StringBuilder result = new StringBuilder();
        result.append(items.stream().map(item -> item.present(columns)).collect(Collectors.joining("")));
        result.append(discounts.stream().map(item -> item.present(columns)).collect(Collectors.joining("")));
        // for (ReceiptItem item : getItems()) {
        //     String receiptItem = item.present(columns);
        //     result.append(receiptItem);
        // }
        // for (Discount discount : getDiscounts()) {
        //     String discountPresentation = discount.present(columns);
        //     result.append(discountPresentation);
        // }
        
        result.append("\n");
        result.append(ReceiptPrinter.presentTotal(getTotalPrice(), columns));
        return result.toString();
    }
}
