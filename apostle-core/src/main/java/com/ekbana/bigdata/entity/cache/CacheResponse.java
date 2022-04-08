package com.ekbana.bigdata.entity.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheResponse {
    private String key;
    private String hashKey;
    private Long ttl;
    private Long cachedOn;
}
