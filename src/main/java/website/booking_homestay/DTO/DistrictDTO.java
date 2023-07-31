package website.booking_homestay.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import website.booking_homestay.entity.Branch;
import website.booking_homestay.entity.Provinces;
import website.booking_homestay.entity.Wards;

import java.util.ArrayList;
import java.util.List;

@Data
public class DistrictDTO {
    private Long districtId;
    private String name;
    @JsonIgnore
    private ProvinceDTO provinces;
//    private List<Wards> wards = new ArrayList<>();
//    private List<Branch> branches = new ArrayList<>();
}
