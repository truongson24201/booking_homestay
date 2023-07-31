package website.booking_homestay.entity;

import jakarta.persistence.*;
import lombok.Data;
import website.booking_homestay.DTO.details.HomesPricesDetails;
import website.booking_homestay.entity.enumreration.EPrice;

import java.util.Date;


@Entity
@Table(name = "homestays_prices")
@IdClass(HomePriceId.class)
@Data
public class HomesPrices {
    @Id
    @ManyToOne
    @JoinColumn(name = "homestay_id")
    private Homestay homestay;

    @Id
    @ManyToOne
    @JoinColumn(name = "pricelist_id")
    private PriceList priceList;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    private EPrice status;

    public HomesPrices(Homestay homestay, PriceList priceList) {
        this.homestay = homestay;
        this.priceList = priceList;
    }

    public HomesPrices() {
    }
}

//
//@NamedNativeQuery(
//        name = "HomesPrices.findHomesPricesByPriceId",
//        query = "SELECT h.homestay_id as homestayId, pl.pricelist_id as pricelistId, h.name, h.num_people as numPeople, pl.effective_date as effectiveDate, hp.price, hp.status " +
//                "FROM homestays h " +
//                "JOIN homestays_prices hp ON h.homestay_id = hp.homestay_id " +
//                "JOIN price_list pl ON pl.pricelist_id = hp.pricelist_id " +
//                "WHERE hp.pricelist_id = :priceId",
//        resultSetMapping = "HomesPricesDetailsMapping"
//)
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
