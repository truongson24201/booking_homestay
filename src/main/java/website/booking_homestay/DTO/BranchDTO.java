package website.booking_homestay.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import website.booking_homestay.entity.Tourist;

import java.util.List;
import java.util.Set;

// xem lại database về hình ảnh 1 phòng, up file xóa đơn giản k có chức năng quản lý hình ảnh nữa.
// tách riêng ra list, add và up chung, combox, code lại dto

@Data
public class BranchDTO {
    private Long branchId;
    private String name;
    private String mapLocation;
    private Boolean status;
    private ProvinceDTO province;
    private DistrictDTO district;
    private WardDTO ward;
//    private List<ManagerDTO> managers;
//    private List<TouristDTO> tourists;
}
