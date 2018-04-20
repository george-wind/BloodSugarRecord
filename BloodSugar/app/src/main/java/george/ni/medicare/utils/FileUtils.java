package george.ni.medicare.utils;

import android.media.MediaMetadataRetriever;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Thinkpad on 2018/4/20.
 */

public class FileUtils {

    public static void writeStingToFile(String path,String text,boolean append){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path,append);
            fileWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "text/plain";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}
