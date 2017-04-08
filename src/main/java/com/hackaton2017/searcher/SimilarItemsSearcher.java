package com.hackaton2017.searcher;

import com.hackaton2017.domain.ShopItem;

import java.net.URI;

/**
 * Created by Vadzim Kavalkou on 08.04.2017.
 */
public interface SimilarItemsSearcher {

    URI getSimilarItemsUrl(ShopItem item);
}
