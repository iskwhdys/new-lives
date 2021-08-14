package com.iskwhdys.newlives.infra.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.awt.Color;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageEditor {

    private ImageEditor() {
    }

    public static byte[] trimSquare(final byte[] src, final float quality, boolean center) throws IOException {

        try (ByteArrayInputStream is = new ByteArrayInputStream(src);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            BufferedImage srcImage = ImageIO.read(is);
            int shortLength = srcImage.getWidth() <= srcImage.getHeight() ? srcImage.getWidth() : srcImage.getHeight();

            BufferedImage destImage = center ? trimCenter(srcImage, shortLength, shortLength)
                    : trimTop(srcImage, shortLength, shortLength);

            // 保存品質はユーザー指定に従う
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(destImage, null, null), param);
            writer.dispose();

            return os.toByteArray();
        }
    }

    public static byte[] trim(final byte[] src, int width, int height, final float quality, boolean center)
            throws IOException {

        try (ByteArrayInputStream is = new ByteArrayInputStream(src);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            BufferedImage srcImage = ImageIO.read(is);

            BufferedImage destImage = center ? trimCenter(srcImage, width, height) : trimTop(srcImage, width, height);

            // 保存品質はユーザー指定に従う
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(destImage, null, null), param);
            writer.dispose();

            return os.toByteArray();
        }
    }

    public static byte[] png2jpg(final byte[] src, Color background) throws IOException {

        try (ByteArrayInputStream is = new ByteArrayInputStream(src);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            BufferedImage srcImage = ImageIO.read(is);

            BufferedImage newBufferedImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(srcImage, 0, 0, background, null);

            // 保存品質はユーザー指定に従う
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1.0f);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(newBufferedImage, null, null), param);
            writer.dispose();

            return os.toByteArray();
        }
    }

    public static byte[] resize(final byte[] src, int width, int height, final float quality) throws IOException {

        try (ByteArrayInputStream is = new ByteArrayInputStream(src);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageOutputStream ios = ImageIO.createImageOutputStream(os)) {
            BufferedImage srcImage = ImageIO.read(is);
            BufferedImage destImage = resizeImage(srcImage, width, height);

            // 保存品質はユーザー指定に従う
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(destImage, null, null), param);
            writer.dispose();

            return os.toByteArray();
        }
    }

    private static BufferedImage resizeImage(final BufferedImage image, int width, int height) {

        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
        resizedImage.getGraphics().drawImage(
                image.getScaledInstance(width, height, java.awt.Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);

        return resizedImage;
    }

    private static BufferedImage trimCenter(final BufferedImage image, int width, int height) {

        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
        var img = image.getSubimage(0, (image.getHeight() - height) / 2, width, height);

        resizedImage.getGraphics().drawImage(img, 0, 0, width, height, null);

        return resizedImage;
    }

    private static BufferedImage trimTop(final BufferedImage image, int width, int height) {

        BufferedImage resizedImage = new BufferedImage(width, height, image.getType());
        var img = image.getSubimage(0, 0, width, height);

        resizedImage.getGraphics().drawImage(img, 0, 0, width, height, null);

        return resizedImage;
    }
}
