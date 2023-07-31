package website.booking_homestay.service.Ipml;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import website.booking_homestay.service.IImagesService;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;


@Service
public class FirebaseStoreServiceImpl implements IImagesService {


    @Override
    public String getImageUrl(String name) {
            return "https://storage.googleapis.com/booking-3f25f.appspot.com/".concat(name);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // Tạo một số ngẫu nhiên dưới dạng UUID và ghép vào tên tệp
        String name = UUID.randomUUID().toString() + extension;

        bucket.create(name, file.getBytes(), file.getContentType());

        return name;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));

        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public void delete(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }
}
