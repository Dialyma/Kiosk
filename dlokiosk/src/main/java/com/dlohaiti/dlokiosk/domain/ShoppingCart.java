package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptsRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class ShoppingCart {
    private final List<Product> products = new ArrayList<Product>();
    private final List<Promotion> promotions = new ArrayList<Promotion>();
    private final ReceiptsRepository repository;

    @Inject
    public ShoppingCart(ReceiptsRepository repository) {
        this.repository = repository;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(int position) {
        products.remove(position);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void checkout() {
        repository.add(products, promotions);
        products.clear();
        promotions.clear();
    }

    public boolean isEmpty() {
        return products.isEmpty() && promotions.isEmpty();
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public void removePromotion(Promotion promotion) {
        promotions.remove(promotion);
    }

    public Money getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        String currencyCode = "";
        for (Product p : products) {
            subtotal = subtotal.add(p.getPrice().getAmount());
            currencyCode = p.getPrice().getCurrencyCode();
        }
        return new Money(subtotal, currencyCode);
    }

    public String getCurrencyCode() {
        String currencyCode = "";
        if (products.isEmpty()) {
            return currencyCode;
        } else {
            currencyCode = products.get(0).getPrice().getCurrencyCode();
        }
        for (Product p : products) {
            if (!currencyCode.equals(p.getPrice().getCurrencyCode())) {
                currencyCode = "MISMATCHED CURRENCIES";
            }
        }
        return currencyCode;
    }

    public BigDecimal getTotal() {
        Collections.sort(promotions);
        BigDecimal total = getSubtotal().getAmount();
        for (Promotion promo : promotions) {
            if (promo.appliesToBasket()) {
                if (promo.isPercentOff()) {
                    BigDecimal amount = promo.getAmount().divide(new BigDecimal(100));
                    total = total.multiply(BigDecimal.ONE.subtract(amount));
                } else if (promo.isAmountOff()) {
                    total = total.subtract(promo.getAmount());
                }
            }
        }
        for (Product product : products) {
            for (Promotion promo : promotions) {
                if (promo.isFor(product) && promo.isNotUsed()) {
                    if (promo.isPercentOff()) {
                        BigDecimal amount = promo.getAmount().divide(new BigDecimal(100));
                        BigDecimal discount = product.getPrice().getAmount().multiply(amount);
                        total = total.subtract(discount);
                    } else if (promo.isAmountOff()) {
                        total = total.subtract(promo.getAmount());
                    }
                    promo.use();
                }
            }
        }
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return total;
    }

    public void removePromotion(int id) {
        promotions.remove(id);
    }
}
