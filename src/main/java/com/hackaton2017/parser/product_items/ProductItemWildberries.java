package com.hackaton2017.parser.product_items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

/**
 * Created by Kanstantsin_Tolstsik on 4/6/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItemWildberries {

    @JsonProperty("PageType")
    private String pageType;

    @JsonProperty("Ptype")
    private String[] productType;

    @JsonProperty("Pbrand")
    private String productBrand;

    @JsonProperty("ProdID")
    private String[] productId;

    @JsonProperty("Value")
    private String[] price;

    @JsonProperty("Psize")
    private String[] availableSizes;

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String[] getProductType() {
        return productType;
    }

    public void setProductType(String[] productType) {
        this.productType = productType;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String[] getProductId() {
        return productId;
    }

    public void setProductId(String[] productId) {
        this.productId = productId;
    }

    public String[] getPrice() {
        return price;
    }

    public void setPrice(String[] price) {
        this.price = price;
    }

    public String[] getAvailableSizes() {
        return availableSizes;
    }

    public void setAvailableSizes(String[] availableSizes) {
        this.availableSizes = availableSizes;
    }

    @Override
    public String toString() {
        return "ProductItemWildberries{" +
                "pageType='" + pageType + '\'' +
                ", productType=" + Arrays.toString(productType) +
                ", productBrand='" + productBrand + '\'' +
                ", productId=" + Arrays.toString(productId) +
                ", price=" + Arrays.toString(price) +
                ", availableSizes=" + Arrays.toString(availableSizes) +
                '}';
    }
}