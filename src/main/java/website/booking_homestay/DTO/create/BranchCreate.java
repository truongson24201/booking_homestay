package website.booking_homestay.DTO.create;

import lombok.Data;

@Data
public class BranchCreate {
    private String name;
    private String mapLocation;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
}
