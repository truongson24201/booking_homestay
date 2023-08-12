package website.booking_homestay.DTO.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Calendar {
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private Date date;

    public Calendar(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public Calendar() {
    }
}
