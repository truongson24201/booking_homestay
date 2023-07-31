package website.booking_homestay.service;

import org.springframework.web.multipart.MultipartFile;
import website.booking_homestay.entity.Images;

public interface IImageStoreService {
    Images store(MultipartFile file);
}
