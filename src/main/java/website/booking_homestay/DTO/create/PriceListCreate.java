package website.booking_homestay.DTO.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import website.booking_homestay.DTO.view.HomestayView;

import java.util.Date;
import java.util.List;

@Data
public class PriceListCreate {
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date effectiveDate;
//    private Boolean status;
//    private BranchView branch;
//    private List<HomestayView> homestays;

}
