package website.booking_homestay.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class HomePriceId implements Serializable {
    private Long homestay;
    private Long priceList;

}
