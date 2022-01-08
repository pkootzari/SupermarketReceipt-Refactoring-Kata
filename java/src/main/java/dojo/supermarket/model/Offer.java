package dojo.supermarket.model;


public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }

    Product getProduct() {
        return this.product;
    }

    public Discount createDiscount(int quantity, double unitPrice) {
        switch(offerType) {
            case ThreeForTwo:
                return getThreeForTwoDiscount(quantity, unitPrice);
            case TwoForAmount:
                return getTwoForAmountDiscount(quantity, unitPrice);
            case FiveForAmount:
                return getFiveForAmountDiscount(quantity, unitPrice);
            case TenPercentDiscount:
                return getTenPercentDiscount(quantity, unitPrice);
            default:
                return null;
        }
        
    }

    private double originalPrice(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }

    private Discount getThreeForTwoDiscount(int quantity, double unitPrice) {
        if(quantity <= 2)
            return null;
        int packSize = 3;
        int numberOfPacks = quantity / packSize;
        int itemsInNoPacks = quantity % packSize; 
        double discountAmount = originalPrice(quantity, unitPrice) - (numberOfPacks*2*unitPrice + itemsInNoPacks*unitPrice);
        return new Discount(product, "3 for 2", discountAmount);
    }

    private Discount getTwoForAmountDiscount(int quantity, double unitPrice) {
        if(quantity <= 1)
            return null;
        int packSize = 2;
        int numberOfPacks = quantity / packSize;
        int itemsInNoPacks = quantity % packSize;
        double discountAmount = originalPrice(quantity, unitPrice) - (numberOfPacks*argument + itemsInNoPacks*unitPrice);
        return new Discount(product, "2 for " + argument, discountAmount);
    }

    private Discount getFiveForAmountDiscount(int quantity, double unitPrice) {
        if(quantity <= 4)
            return null;
        int packSize = 5;
        int numberOfPacks = quantity / packSize;
        int itemsInNoPacks = quantity % packSize;
        double discountAmount = originalPrice(quantity, unitPrice) - (numberOfPacks*argument + itemsInNoPacks*unitPrice);
        return new Discount(product, packSize + " for " + argument, discountAmount);
    }

    private Discount getTenPercentDiscount(int quantity, double unitPrice) {
        return new Discount(product, argument + "% off",originalPrice(quantity, unitPrice)*(argument / 100.0));
    }
}
