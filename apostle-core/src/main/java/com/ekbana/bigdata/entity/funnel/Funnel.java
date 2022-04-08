package com.ekbana.bigdata.entity.funnel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Funnel {
    @JsonProperty("year")
    private Long year=0L;
    @JsonProperty("month")
    private Long month=0L;
    @JsonProperty("day")
    private Long day=0L;
    @JsonProperty("hour")
    private Long hour=0L;
    @JsonProperty("minute")
    private Long minute=0L;
    @JsonProperty("total_calls")
    private Long totalCalls=0L;

    public Funnel(long year, long month, long day, long hour, long minute) {
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.minute=minute;
    }

    public static Funnel create(String funnel) throws JsonProcessingException {
        return new ObjectMapper().readValue(funnel,Funnel.class);
    }
}
