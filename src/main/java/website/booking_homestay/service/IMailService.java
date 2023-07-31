package website.booking_homestay.service;

import website.booking_homestay.DTO.AccountInforSendMail;

public interface IMailService {
    public void sendMailRegister(AccountInforSendMail account);
}
