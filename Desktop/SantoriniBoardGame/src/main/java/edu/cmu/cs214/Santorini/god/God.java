package edu.cmu.cs214.Santorini.god;

import edu.cmu.cs214.Santorini.god.strategy.GodActionStrategy;

/**
 * God, containing its strategy (where god power logic locates at)
 * Note: "Man" is a god without god power
 */
public record God(String name, String title, String powerDetail,
                  String url,
                  GodActionStrategy strategy) {

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPowerDetail() {
        return powerDetail;
    }

    public GodActionStrategy getStrategy() {
        return strategy;
    }

    public GodDto toDto() {
        return new GodDto(name, title, powerDetail, url);
    }

    @Override
    public String toString() {
        return "God{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", powerDetail='" + powerDetail + '\'' +
                '}';
    }
}
