package website.booking_homestay.DTO;

import lombok.Data;
import website.booking_homestay.DTO.view.BranchView;
import website.booking_homestay.entity.*;

import java.util.Date;
import java.util.List;

@Data
public class HomestayDTO {
    private Long homestayId; // no enter
    private String name;
//    private Double price;// no enter
    private Integer numPeople;
    private String status;


//    private List<FacilityDTO> facilities;
//    @JsonIgnore
//    private BranchView branch;
//    private List<Images> images;
//    private List<Bill> bills = new ArrayList<>();
}
