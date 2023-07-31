package website.booking_homestay.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterHomestay {
    private Long homestayId;
    private String name;
    private String address;
    private String fullName;
    private Date checkIn;
    private Date checkOut;
    private String email;
    private String phoneNumber;
    private Integer numPeople;
    private Double total;

    public RegisterHomestay(Long homestayId, String name, String address, String fullName, Date checkIn, Date checkOut, String email, String phoneNumber, Integer numPeople, Double total) {
        this.homestayId = homestayId;
        this.name = name;
        this.address = address;
        this.fullName = fullName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.numPeople = numPeople;
        this.total = total;
    }

    public RegisterHomestay() {
    }
}
