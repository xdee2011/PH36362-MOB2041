package com.xdee.jpmart.cart;

import com.xdee.jpmart.model.CartItem;
import com.xdee.jpmart.model.SanPham;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Giỏ hàng trong RAM (dùng chung giữa Product và Cart).
 */
public final class CartManager {

    private static final Object LOCK = new Object();
    private static final List<CartItem> ITEMS = new ArrayList<>();

    private CartManager() {
    }

    /**
     * Thêm 1 sản phẩm (hoặc tăng số lượng nếu đã có). Không vượt tồn kho.
     *
     * @return true nếu đã thêm/tăng được
     */
    public static boolean addProduct(SanPham sp) {
        if (sp == null || sp.getQuantity() <= 0) {
            return false;
        }
        synchronized (LOCK) {
            for (CartItem c : ITEMS) {
                if (c.getProductId() == sp.getId()) {
                    c.setMaxStock(sp.getQuantity());
                    if (c.getQuantity() >= sp.getQuantity()) {
                        return false;
                    }
                    c.setQuantity(c.getQuantity() + 1);
                    return true;
                }
            }
            ITEMS.add(new CartItem(sp.getId(), sp.getName(), sp.getPrice(), 1, sp.getQuantity()));
            return true;
        }
    }

    public static List<CartItem> getItems() {
        synchronized (LOCK) {
            return new ArrayList<>(ITEMS);
        }
    }

    public static double getTotal() {
        synchronized (LOCK) {
            double t = 0;
            for (CartItem c : ITEMS) {
                t += c.getLineTotal();
            }
            return t;
        }
    }

    public static void setQuantity(int productId, int quantity) {
        synchronized (LOCK) {
            Iterator<CartItem> it = ITEMS.iterator();
            while (it.hasNext()) {
                CartItem c = it.next();
                if (c.getProductId() == productId) {
                    if (quantity <= 0) {
                        it.remove();
                    } else {
                        int q = Math.min(quantity, c.getMaxStock());
                        c.setQuantity(Math.max(1, q));
                    }
                    return;
                }
            }
        }
    }

    public static void increment(int productId) {
        synchronized (LOCK) {
            for (CartItem c : ITEMS) {
                if (c.getProductId() == productId) {
                    if (c.getQuantity() < c.getMaxStock()) {
                        c.setQuantity(c.getQuantity() + 1);
                    }
                    return;
                }
            }
        }
    }

    public static void decrement(int productId) {
        synchronized (LOCK) {
            Iterator<CartItem> it = ITEMS.iterator();
            while (it.hasNext()) {
                CartItem c = it.next();
                if (c.getProductId() == productId) {
                    if (c.getQuantity() <= 1) {
                        it.remove();
                    } else {
                        c.setQuantity(c.getQuantity() - 1);
                    }
                    return;
                }
            }
        }
    }

    public static void remove(int productId) {
        synchronized (LOCK) {
            ITEMS.removeIf(c -> c.getProductId() == productId);
        }
    }

    public static void clear() {
        synchronized (LOCK) {
            ITEMS.clear();
        }
    }

    public static int getTotalQuantity() {
        synchronized (LOCK) {
            int n = 0;
            for (CartItem c : ITEMS) {
                n += c.getQuantity();
            }
            return n;
        }
    }
}
