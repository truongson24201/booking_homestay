package website.booking_homestay.DTO.details;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import website.booking_homestay.entity.HomesPrices;
import website.booking_homestay.entity.Homestay;
import website.booking_homestay.entity.PriceList;
import website.booking_homestay.entity.enumreration.EPrice;

import java.time.LocalDate;
import java.util.Date;


//@SqlResultSetMapping(
//        name = "HomesPricesDetailsMapping",
//        classes = @ConstructorResult(
//                targetClass = HomesPricesDetails.class,
//                columns = {
//                        @ColumnResult(name = "homestayId", type = Long.class),
//                        @ColumnResult(name = "pricelistId", type = Long.class),
//                        @ColumnResult(name = "name", type = String.class),
//                        @ColumnResult(name = "numPeople", type = Integer.class),
//                        @ColumnResult(name = "effectiveDate", type = Date.class),
//                        @ColumnResult(name = "price", type = Double.class),
//                        @ColumnResult(name = "status", type = String.class)
//                }
//        )
//)
//@NamedNativeQuery(name = "HomesPrices.findHomesPricesByPriceId",
//        resultSetMapping = "HomesPricesDetailsMapping",
//        query =
//)

@Data
public class HomesPricesDetails {
    private Long homestayId;
    private Long pricelistId;
    private String name;
    private Integer numPeople;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date effectiveDate;
    private Double price;
    @Enumerated(EnumType.STRING)
    private EPrice status;

    public HomesPricesDetails(Long homestayId, Long pricelistId, String name, Integer numPeople, Date effectiveDate, Double price, EPrice status) {
        this.homestayId = homestayId;
        this.pricelistId = pricelistId;
        this.name = name;
        this.numPeople = numPeople;
        this.effectiveDate = effectiveDate;
        this.price = price;
        this.status = status;
    }

    public HomesPricesDetails() {
    }

    public static HomesPricesDetails map(HomesPrices homesPrices){
        HomesPricesDetails homesPricesDetails = new HomesPricesDetails();
        homesPricesDetails.setHomestayId(homesPrices.getHomestay().getHomestayId());
        homesPricesDetails.setPricelistId(homesPrices.getPriceList().getPricelistId());
        homesPricesDetails.setName(homesPrices.getHomestay().getName());
        homesPricesDetails.setNumPeople(homesPrices.getHomestay().getNumPeople());
        homesPricesDetails.setEffectiveDate(homesPrices.getPriceList().getEffectiveDate());
        homesPricesDetails.setPrice(homesPrices.getPrice());
        homesPricesDetails.setStatus(homesPrices.getStatus());
        return homesPricesDetails;
    }
}
