package edu.cmu.cs214.Santorini.god;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * God DTO, used for RESTful API serialization
 */
@JsonAutoDetect
public class GodDto {
    private String name;
    private String title;
    private String powerDetail;
    private String url;

    public GodDto(String name, String title, String powerDetail, String url) {
        this.name = name;
        this.title = title;
        this.powerDetail = powerDetail;
        this.url = url;
    }

    public GodDto() {
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPowerDetail() {
        return powerDetail;
    }

    public String getUrl() {
        return url;
    }
}
