package com.didenko.gameservice.service;

import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.utils.Endpoints;
import com.didenko.gameservice.util.IgdbWrapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/*
* Provides and caches data required by the UtilRestController
* */
@RequiredArgsConstructor
@Service
public class UtilService {

    private final IgdbWrapperUtil igdbWrapperUtil;
    private IGDBWrapper igdbWrapper;
    private static final String POPULAR_PLATFORM_IDS = " where id =(6, 163, 8, 41, 9, 11, 18, 20, 167, 169, 37, 165, 390, 7, 48, 12, 49, 3, 5, 14, 19, 130);";
    private static final String SORT_ORDER = " sort generation desc;";

    @Cacheable(value = "platforms")
    public String getPlatformsList(){
        if (igdbWrapper == null) {
            igdbWrapper = igdbWrapperUtil.getIGDBWrapper();
        }

        try {
            return igdbWrapper.apiJsonRequest(Endpoints.PLATFORMS, "fields name; exclude alternative_name; limit 500;"
                    + POPULAR_PLATFORM_IDS +
                    SORT_ORDER);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

}