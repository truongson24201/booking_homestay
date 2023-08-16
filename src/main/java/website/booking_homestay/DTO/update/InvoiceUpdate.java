package website.booking_homestay.DTO.update;

import lombok.Data;

@Data
public class InvoiceUpdate {
    private Long homestayId;
    private String identityNumber;
    private String cardType;
    private String status;
}
