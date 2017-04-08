package com.hackaton2017.parser.product_items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vadzim Kavalkou on 08.04.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItemLamoda extends ProductItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("ProdID")
    private String productId;

    @JsonProperty("Value")
    private Double price;

    @JsonProperty("Psize")
    private String[] availableSizes;

    @JsonProperty("PColors")
    private String[] colors;

    @JsonProperty("ImageLink")
    private String imageLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String[] getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(String[] availableSizes) {
        this.availableSizes = availableSizes;
    }

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProductItemLamoda that = (ProductItemLamoda) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(name, that.name)
                .append(productId, that.productId)
                .append(price, that.price)
                .append(availableSizes, that.availableSizes)
                .append(colors, that.colors)
                .append(imageLink, that.imageLink)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(name)
                .append(productId)
                .append(price)
                .append(availableSizes)
                .append(colors)
                .append(imageLink)
                .toHashCode();
    }
}
