package es.daw01.savex.utils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

    private ImageUtils() {
        /* Prevent instantiation */
    }

    /**
     * Convert a MultipartFile object to a Blob object
     *
     * @param multipartfile The MultipartFile object to convert
     * @return The Blob object
     */
    public static Blob multipartFileToBlob(MultipartFile multipartfile) throws IOException {
        return BlobProxy.generateProxy(multipartfile.getInputStream(), multipartfile.getSize());
    }

    /**
     * Convert a Blob object to a Resource object
     * 
     * @param blob The Blob object to convert
     * @return The Resource object
     */
    public static Resource blobToResource(Blob blob) throws SQLException {
        return new InputStreamResource(blob.getBinaryStream());
    }
}
