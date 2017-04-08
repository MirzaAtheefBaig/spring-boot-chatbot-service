package com.hackaton2017.parser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton2017.domain.Job;
import com.hackaton2017.domain.ShopItem;
import com.hackaton2017.parser.ShopItemParser;
import com.hackaton2017.parser.product_items.ProductItemWildberries;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by Kanstantsin_Tolstsik on 4/4/2017.
 */
public class WildberriesShopItemParser implements ShopItemParser {

    @Override
    public ShopItem parse(final Job job) {

        Document document = null;
        try {
            document = Jsoup.connect("https://www.wildberries.by/catalog/3798636/detail.aspx?targetUrl=GP").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element test = document.body().select("script").first();
        String json = test.html().replaceAll("var google_tag_params = ", "").replaceAll(";", "");

        ObjectMapper mapper = new ObjectMapper();
        ProductItemWildberries productItemWildberries = null;
        try {
            productItemWildberries = mapper.readValue(json, ProductItemWildberries.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element el = document.getElementById("j-similar-carousel");

        System.out.println(productItemWildberries);

        return null; //TODO IMPLEMENT LOGIC
    }
}