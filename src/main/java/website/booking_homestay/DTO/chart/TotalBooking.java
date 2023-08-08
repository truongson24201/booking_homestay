package website.booking_homestay.DTO.chart;

import lombok.Data;

@Data
public class TotalBooking {
    private Long invoiceCount;
    private Double total;

    public TotalBooking(Long invoiceCount, Double total) {
        this.invoiceCount = invoiceCount;
        this.total = total;
    }

    public TotalBooking() {
        invoiceCount = 0L;
        total = 0D;
    }
}
