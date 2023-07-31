package website.booking_homestay.DTO.combox;

import lombok.Data;

@Data
public class BranchCombox {
    private Long branchId;
    private String name;
    private String address;

    public BranchCombox(Long branchId, String name, String address) {
        this.branchId = branchId;
        this.name = name;
        this.address = address;
    }

    public BranchCombox() {
    }
}
