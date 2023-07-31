package website.booking_homestay.service;

import website.booking_homestay.entity.User;

public interface IContextHolder {
    public String getUsernameFromContext();
    public String getRoleFromContext();
//    public AdminManager getManager();
    public User getUser();
}
