package website.booking_homestay.DTO.view;

import lombok.Data;

@Data
public class BranchView {
    private Long branchId;
    private String name;
    private String address;
    private String status;

    public BranchView(Long branchId, String name, String address) {
        this.branchId = branchId;
        this.name = name;
        this.address = address;
    }

    public BranchView() {
    }
}
