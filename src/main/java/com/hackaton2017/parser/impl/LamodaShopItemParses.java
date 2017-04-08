package com.hackaton2017.parser.impl;

import com.google.common.collect.ImmutableMap;
import com.hackaton2017.domain.Job;
import com.hackaton2017.domain.ShopItem;
import com.hackaton2017.parser.ShopItemParser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vadzim Kavalkou on 08.04.2017.
 */
public class LamodaShopItemParses implements ShopItemParser, Function<Map<String, Elements>, ShopItem> {

    private static final String INFO_SELECTOR = "body > div.page > div:nth-child(4) > div > div > div.ii-product__aside > div > div.ii-product__buy.js-widget-buy > div > div.product__cart-add > script";
    private static final String NAME_SELECTOR = "body > div.page > div:nth-child(4) > div > div > div.ii-product__aside > div > h1";
    private static final String PRICE_SELECTOR = "body > div.page > div:nth-child(4) > div > div > div.ii-product__aside > div > div.ii-product__buy.js-widget-buy > div > div.ii-product__price > div.ii-product__price-current";

    private final static String INFO_PREFIX = "<script type=\"text/javascript\" data-module=\"statistics\">;(function(LMDA, d, n){ d[\"product\"] = ";
    private final static String INFO_POSTFIX = "; LMDA[n] = LMDA[n] || []; return LMDA[n].push(d); })(window.LMDA = window.LMDA || {}, {}, 'PAGEDATA');</script>";

    @Override
    public ShopItem parse(Job job) {

        Document doc = null;

        try {
            doc = Jsoup.connect("http://www.lamoda.by/p/ex003awfww45/shoes-exquily-sapogi/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Elements> elementsMap = ImmutableMap.of("info", doc.select(INFO_SELECTOR), "name", doc.select(NAME_SELECTOR), "price", doc.select(PRICE_SELECTOR));

        return apply(elementsMap);
    }

    @Override
    public ShopItem apply(Map<String, Elements> elements) {

        JSONObject object;
        ShopItem item = new ShopItem();

        String name = elements.get("name").text();
        String info = elements.get("info").toString().replace(INFO_PREFIX, "").replace(INFO_POSTFIX, "");
        String cost = elements.get("price").text();
        String itemSize = "NONE";

        Pattern pattern = Pattern.compile("\\d+.?\\d+");
        Matcher matcher = pattern.matcher(cost);

        if (matcher.find()) {
            cost = matcher.group();
        }


        try {
            object = new JSONObject(info);

            JSONObject size = new JSONObject(object.getJSONObject("size").toString());

            itemSize = size.get("sizes").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        item.setName(name);
        item.setCost(Double.parseDouble(cost));

        System.out.println(item.toString());

        return item;
    }
}
