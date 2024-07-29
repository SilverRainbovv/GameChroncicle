package com.didenko.gameservice.dto;

import lombok.Getter;

@Getter
public enum ImageSize {

    //90x128
    COVER_SMALL("cover_small"),
    //569x320
    SCREENSHOT_MED("screenshot_med"),
    //264x374
    COVER_BIG("cover_big"),
    //284x160
    LOGO_MED("logo_med"),
    //889 x 500
    SCREENSHOT_BIG("screenshot_big"),
    //1280 x 720
    SCREENSHOT_HUGE("screenshot_huge"),
    //90 x 90
    THUMB("thumb"),
    //	35 x 35
    MICRO("micro"),
    //1280 x 720
    HD("720p"),
    //1920 x 1080
    FULL_HD("1080p");

    private final String size;

    ImageSize(String size) {
        this.size = size;
    }

}