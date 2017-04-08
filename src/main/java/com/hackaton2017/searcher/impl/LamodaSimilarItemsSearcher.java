package com.hackaton2017.searcher.impl;

import com.google.common.base.Joiner;
import com.hackaton2017.domain.ShopItem;
import com.hackaton2017.searcher.SimilarItemsSearcher;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Vadzim Kavalkou on 08.04.2017.
 */
public class LamodaSimilarItemsSearcher implements SimilarItemsSearcher {

    private final static String MAIN_URL = "http://www.lamoda.by/catalogsearch/result";
    private final static String COMMA = ",";

    @Override
    public URI getSimilarItemsUrl(ShopItem item) {

        HttpGet getRequest = new HttpGet(MAIN_URL);

        URI uri = null;

        try {
            uri = new URIBuilder(getRequest.getURI())
                    .addParameter("q", item.getName())
                    .addParameter("from", "button")
                    .addParameter("submit", "y")
                    .addParameter("price", "0," + item.getCost().intValue())
                    .addParameter("size_values", Joiner.on(COMMA).join(item.getSize()))
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return uri;
    }

}
