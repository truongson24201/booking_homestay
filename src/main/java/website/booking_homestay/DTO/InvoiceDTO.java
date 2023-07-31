package website.booking_homestay.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import website.booking_homestay.DTO.view.HomestayView;
import website.booking_homestay.entity.enumreration.ECardType;

import java.util.Date;

@Data
public class InvoiceDTO {
    private Long invoiceId;
    private String identityNumber;
    private String cardType;
    private String status;
}
