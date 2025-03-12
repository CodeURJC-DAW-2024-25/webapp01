package es.daw01.savex.utils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

public class ImageUtils {

    private ImageUtils() {
        /* Prevent instantiation */
    }

    /**
     * Convert a Resource object to a Blob object
     *
     * @param resource The Resource object to convert
     * @return The Blob object
     */
    public static Blob resourceToBlob(Resource resource) throws IOException {
        return BlobProxy.generateProxy(resource.getInputStream(), resource.contentLength());
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
