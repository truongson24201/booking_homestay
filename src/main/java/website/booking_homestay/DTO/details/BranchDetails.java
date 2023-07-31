package website.booking_homestay.DTO.details;

import lombok.Data;
import website.booking_homestay.DTO.DistrictDTO;
import website.booking_homestay.DTO.ProvinceDTO;
import website.booking_homestay.DTO.WardDTO;
import website.booking_homestay.entity.Districts;
import website.booking_homestay.entity.Provinces;
import website.booking_homestay.entity.Wards;

@Data
public class BranchDetails {
    private Long branchId;
    private String name;
    private String mapLocation;
    private Boolean status;
    private ProvinceDTO province;
    private DistrictDTO district;
    private WardDTO ward;
}
