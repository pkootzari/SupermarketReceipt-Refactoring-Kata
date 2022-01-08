package dojo.supermarket;

import dojo.supermarket.model.*;

import java.util.Locale;

public class ReceiptPrinter {

    private final int columns;

    public ReceiptPrinter() {
        this(40);
    }

    public ReceiptPrinter(int columns) {
        this.columns = columns;
    }

    public String printReceipt(Receipt receipt) {
        return receipt.present(columns);
    }

    // private String presentReceiptItem(ReceiptItem item) {
    //     String totalPricePresentation = presentPrice(item.getTotalPrice());
    //     String name = item.getProduct().getName();

    //     String line = formatLineWithWhitespace(name, totalPricePresentation);

    //     if (item.getQuantity() != 1) {
    //         line += "  " + presentPrice(item.getPrice()) + " * " + presentQuantity(item) + "\n";
    //     }
    //     return line;
    // }

    // private String presentDiscount(Discount discount) {
    //     String name = discount.getDescription() + "(" + discount.getProduct().getName() + ")";
    //     String value = presentPrice(discount.getDiscountAmount());

    //     return formatLineWithWhitespace(name, value);
    // }

    public static String presentTotal(double price, int columns) {
        String name = "Total: ";
        String value = presentPrice(price);
        return ReceiptPrinter.formatLineWithWhitespace(name, value, columns);
    }

    public static String formatLineWithWhitespace(String name, String value, int columns) {
        StringBuilder line = new StringBuilder();
        line.append(name);
        int whitespaceSize = columns - name.length() - value.length();
        for (int i = 0; i < whitespaceSize; i++) {
            line.append(" ");
        }
        line.append(value);
        line.append('\n');
        return line.toString();
    }

    public static String presentPrice(double price) {
        return String.format(Locale.UK, "%.2f", price);
    }

}
