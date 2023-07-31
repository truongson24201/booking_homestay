package website.booking_homestay.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import website.booking_homestay.entity.Homestay;

import java.util.HashSet;
import java.util.Set;

@Data
public class FacilityDTO {
    private Long facilityId;
    private String name;
//    private Double price;
//    @JsonIgnore
//    private Set<Homestay> homestays = new HashSet<>();
}
