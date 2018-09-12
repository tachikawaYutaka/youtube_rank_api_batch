package com.wakabatimes.youtube_rank_api_batch.entity;

import lombok.Data;

@Data
public class YoutubeProperty {
    private String baseUrl;
    private String params;
    private String regionCode;
    private String videoCategoryId;
    private String key;
}
