package com.xdee.jpmart.model;

/**
 * Một dòng trong giỏ hàng (bộ nhớ, theo phiên làm việc).
 */
public class CartItem {
    private final int productId;
    private final String name;
    private final double price;
    private int quantity;
    /** Tồn kho — cập nhật khi thêm lại từ danh sách sản phẩm */
    private int maxStock;

    public CartItem(int productId, String name, double price, int quantity, int maxStock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.maxStock = maxStock;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public double getLineTotal() {
        return price * quantity;
    }
}
