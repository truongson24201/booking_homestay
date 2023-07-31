package website.booking_homestay.DTO.update;

import lombok.Data;

@Data
public class BranchUpdate {
    private String name;
    private String mapLocation;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private Boolean status;
}
