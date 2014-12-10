package com.dlohaiti.dlokiosknew.domain;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class ShoppingCartNew {
    private final Products products = new Products();
    private final Promotions promotions = new Promotions();
    private SalesChannel salesChannel;
    private CustomerAccount customerAccount;
    private final RegisterNew register;
    private String paymentMode;
    private boolean isSponsorSelected;
    private Sponsor sponsor;
    private Money sponsorAmount = Money.ZERO;
    private Money customerAmount = Money.ZERO;
    private String paymentType;
    private String deliveryTime;

    @Inject
    public ShoppingCartNew(RegisterNew register) {
        this.register = register;
    }

    public void addOrUpdateProduct(Product newProduct) {
        Integer existingProductIndex = products.getIndexOf(newProduct);
        if (existingProductIndex != null) {
            products.set(existingProductIndex, newProduct);
        } else {
            products.add(newProduct);
        }
    }

    public Products getProducts() {
        return products;
    }

    public void checkout() {
        register.checkout(this);
        clear();
    }

    public void clear() {
        products.clear();
        promotions.clear();
        salesChannel = null;
        customerAccount = null;
        paymentMode = null;
        paymentType = null;
        isSponsorSelected = false;
        sponsor = null;
        sponsorAmount = Money.ZERO;
        customerAmount = Money.ZERO;
        deliveryTime = null;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public Promotions getPromotions() {
        return promotions;
    }

    public void removePromotion(Promotion promotion) {
        promotions.remove(promotion);
    }

    public Money getActualTotal() {
        return register.actualTotal(this);
    }

    public String getCurrencyCode() {
        String currencyCode = "";
        if (products.isEmpty()) {
            return currencyCode;
        } else {
            currencyCode = products.get(0).getPrice().getCurrencyCode();
        }
        return currencyCode;
    }

    public Money getDiscountedTotal() {
        return register.discountedTotal(this);
    }

    public void clearPromotions() {
        promotions.clear();
    }

    public SalesChannel salesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(SalesChannel channel) {
        this.salesChannel = channel;
    }

    public CustomerAccount customerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(CustomerAccount account) {
        this.customerAccount = account;
    }

    public void addPromotions(List<Promotion> promotions) {
        this.promotions.addAll(promotions);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void overwrite(Promotions applicablePromotions) {
        clearPromotions();
        addPromotions(applicablePromotions);
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public void setSponsorAmount(Money sponsorAmount) {
        this.sponsorAmount = sponsorAmount;
    }

    public Money customerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(Money customerAmount) {
        this.customerAmount = customerAmount;
    }

    public Money sponsorAmount() {
        return sponsorAmount;
    }

    public Money dueAmount() {
        return getDiscountedTotal().minus(sponsorAmount).minus(customerAmount);
    }

    public Sponsor sponsor() {
        return sponsor;
    }

    public String paymentMode() {
        return paymentMode;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String paymentType() {
        return paymentType;
    }

    public void updateCustomerAmountWithTheBalanceAmount() {
        customerAmount = getDiscountedTotal().minus(sponsorAmount());
    }

    public String deliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isSponsorSelected() {
        return isSponsorSelected;
    }

    public void setIsSponsorSelected(boolean isSponsorSelected) {
        this.isSponsorSelected = isSponsorSelected;
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }
}