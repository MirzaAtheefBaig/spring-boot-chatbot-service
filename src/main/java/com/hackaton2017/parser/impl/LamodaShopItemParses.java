package com.hackaton2017.parser.impl;

import com.google.common.collect.Maps;
import com.hackaton2017.domain.Job;
import com.hackaton2017.domain.ShopItem;
import com.hackaton2017.parser.ShopItemParser;
import com.hackaton2017.parser.product_items.ProductItemLamoda;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vadzim Kavalkou on 08.04.2017.
 */
public class LamodaShopItemParses implements ShopItemParser, Function<Map<String, Elements>, ProductItemLamoda>, Consumer<JSONObject> {

    private static final String INFO_SELECTOR = "body > div.page > div:nth-child(4) > div > div > div.ii-product__aside > div > div.ii-product__buy.js-widget-buy > div > div.product__cart-add > script";
    private static final String NAME_SELECTOR = "body > div.page > div:nth-child(4) > div > div > div.ii-product__aside > div > h1";
    private static final String PRICE_SELECTOR = "body > div.page > div:nth-child(4) > div > div > div.ii-product__aside > div > div.ii-product__buy.js-widget-buy > div > div.ii-product__price > div.ii-product__price-current";
    private static final String ID_SELECTOR = "body > div.page > div.width-wrapper.js-width-wrapper-breadcrumb.clearfix > div.breadcrumbs__sku";

    private final static String INFO_PREFIX = "<script type=\"text/javascript\" data-module=\"statistics\">;(function(LMDA, d, n){ d[\"product\"] = ";
    private final static String INFO_POSTFIX = "; LMDA[n] = LMDA[n] || []; return LMDA[n].push(d); })(window.LMDA = window.LMDA || {}, {}, 'PAGEDATA');</script>";

    private List<String> availableSizes = new ArrayList<>();

    private String name;
    private String info;
    private String cost;
    private String id;

    @Override
    public ShopItem parse(Job job) {

        final ShopItem shopItem = new ShopItem();
        Document doc = null;

        try {
            doc = Jsoup.connect(job.getUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Elements> elementsMap = Maps.newHashMap();
        elementsMap.put("info", doc.select(INFO_SELECTOR));
        elementsMap.put("name", doc.select(NAME_SELECTOR));
        elementsMap.put("price", doc.select(PRICE_SELECTOR));
        elementsMap.put("id", doc.select(ID_SELECTOR));

        ProductItemLamoda productItem = apply(elementsMap);

        shopItem.setCode(productItem.getProductId());
        shopItem.setName(productItem.getName());
        shopItem.setCost(productItem.getPrice());
        shopItem.setSize(productItem.getAvailableSizes());

        System.out.println(shopItem.toString());

        return shopItem;
    }

    @Override
    public ProductItemLamoda apply(Map<String, Elements> elements) {

        ProductItemLamoda item = new ProductItemLamoda();

        name = elements.get("name").text();
        info = elements.get("info").toString().replace(INFO_PREFIX, "").replace(INFO_POSTFIX, "");
        cost = elements.get("price").text();
        id = elements.get("id").text().replace("Артикул ", "");

        List<JSONObject> jsonObjectSizes = new ArrayList<>();

        try {

            JSONObject infoObject = new JSONObject(info);

            if (infoObject.get("isInStock").toString().equals("true")) {


                JSONArray jsonSizesArray = new JSONArray(new JSONObject(infoObject.get("size").toString()).get("sizes").toString());

                if (jsonSizesArray != null) {

                    for (int i = 0; i < jsonSizesArray.length(); i++) {
                        jsonObjectSizes.add(new JSONObject(jsonSizesArray.getString(i)));
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectSizes.stream().forEach(jsonObject -> accept(jsonObject));

        Pattern pattern = Pattern.compile("\\d+.?\\d+");
        Matcher matcher = pattern.matcher(cost);

        if (matcher.find()) {
            cost = matcher.group();
        }

        item.setName(name);
        item.setPrice(Double.parseDouble(cost));
        item.setProductId(id);
        item.setAvailableSizes(availableSizes);

        return item;
    }

    @Override
    public void accept(JSONObject jsonObject) {

        try {

            if (jsonObject.get("available").toString().equals("true")) {
                availableSizes.add(jsonObject.get("value").toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
