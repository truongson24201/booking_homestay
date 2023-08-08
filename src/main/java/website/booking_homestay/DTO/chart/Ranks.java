package website.booking_homestay.DTO.chart;

import lombok.Data;

@Data
public class Ranks {
    private String name;
    private Double total;

    public Ranks(String name, Double total) {
        this.name = name;
        this.total = total;
    }

    public Ranks() {
    }
}
