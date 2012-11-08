/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penny.downloadmanager.control.di;

import penny.downloadmanager.model.db.Download;
import penny.downloadmanager.model.Model;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import penny.download.AbstractDownload;
import penny.download.DownloadProcessor;

/**
 *
 * @author john
 */
public class ImageInfo implements DownloadProcessor {

    private void getImageInfo(Download i, File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                i.setExtraProperty(Model.getApplicationSettings().getImageModel().getHeightName(), Integer.toString(image.getHeight()));
                i.setExtraProperty(Model.getApplicationSettings().getImageModel().getWidthName(), Integer.toString(image.getWidth()));
            }
            image = null;
        } catch (IOException ex) {
            Logger.getLogger(ImageInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onStartInput(AbstractDownload d) {
    }

    @Override
    public void onEndInput(AbstractDownload d) {
    }

    @Override
    public void onCompleted(AbstractDownload d) {
        if (Model.getApplicationSettings().getImageModel().isWidthAndHeight()) {
            Download i = (Download) d;
            File file = new File(i.getTempPath());
            if (file.exists()) {
                FileDataSource dataSource = new FileDataSource(file);
                if (dataSource.getContentType().startsWith("image")) {
                    getImageInfo(i, file);
                }
            } else {
                file = new File(i.getSavePath());
                if (file.exists()) {
                    FileDataSource dataSource = new FileDataSource(file);
                    if (dataSource.getContentType().startsWith("image")) {
                        getImageInfo(i, file);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCheck(AbstractDownload d) {
        return true;
    }

    @Override
    public void doChunck(AbstractDownload d, int read, byte[] buffer) {
    }

    @Override
    public void onReset(AbstractDownload d) {
    }

    @Override
    public void onInit(AbstractDownload d) {
    }
}
