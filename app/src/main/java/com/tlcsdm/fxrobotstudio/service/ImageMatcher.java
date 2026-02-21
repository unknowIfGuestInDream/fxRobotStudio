package com.tlcsdm.fxrobotstudio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Provides image recognition functionality using JavaCV.
 * Compares a template image against a screen capture to find matches.
 */
public class ImageMatcher {

    private static final Logger log = LoggerFactory.getLogger(ImageMatcher.class);
    private static final double DEFAULT_THRESHOLD = 0.8;

    /**
     * Attempts to find the template image within the screen image.
     *
     * @param screenImage  the screen capture
     * @param templateFile the template image file to search for
     * @return the location of the best match, or null if not found above threshold
     */
    public Rectangle findImage(BufferedImage screenImage, File templateFile) {
        return findImage(screenImage, templateFile, DEFAULT_THRESHOLD);
    }

    /**
     * Attempts to find the template image within the screen image.
     *
     * @param screenImage  the screen capture
     * @param templateFile the template image file to search for
     * @param threshold    the matching threshold (0.0 to 1.0)
     * @return the location of the best match, or null if not found above threshold
     */
    public Rectangle findImage(BufferedImage screenImage, File templateFile, double threshold) {
        try {
            BufferedImage templateImage = ImageIO.read(templateFile);
            if (templateImage == null) {
                log.error("Failed to load template image: {}", templateFile);
                return null;
            }
            return findImage(screenImage, templateImage, threshold);
        } catch (IOException e) {
            log.error("Error reading template image: {}", templateFile, e);
            return null;
        }
    }

    /**
     * Attempts to find the template image within the screen image.
     *
     * @param screenImage   the screen capture
     * @param templateImage the template image to search for
     * @param threshold     the matching threshold (0.0 to 1.0)
     * @return the location of the best match, or null if not found above threshold
     */
    public Rectangle findImage(BufferedImage screenImage, BufferedImage templateImage, double threshold) {
        if (screenImage == null || templateImage == null) {
            return null;
        }

        int screenWidth = screenImage.getWidth();
        int screenHeight = screenImage.getHeight();
        int templateWidth = templateImage.getWidth();
        int templateHeight = templateImage.getHeight();

        if (templateWidth > screenWidth || templateHeight > screenHeight) {
            log.warn("Template image is larger than screen image");
            return null;
        }

        double bestScore = 0;
        int bestX = -1;
        int bestY = -1;

        for (int y = 0; y <= screenHeight - templateHeight; y++) {
            for (int x = 0; x <= screenWidth - templateWidth; x++) {
                double score = computeSimilarity(screenImage, templateImage, x, y);
                if (score > bestScore) {
                    bestScore = score;
                    bestX = x;
                    bestY = y;
                }
            }
        }

        if (bestScore >= threshold) {
            log.debug("Image found at ({},{}) with score {}", bestX, bestY, bestScore);
            return new Rectangle(bestX, bestY, templateWidth, templateHeight);
        }

        log.debug("Image not found (best score: {})", bestScore);
        return null;
    }

    /**
     * Checks if the stop image is currently visible on the screen.
     */
    public boolean isStopImageVisible(BufferedImage screenImage, File stopImageFile) {
        return findImage(screenImage, stopImageFile) != null;
    }

    private double computeSimilarity(BufferedImage screen, BufferedImage template, int offsetX, int offsetY) {
        int tw = template.getWidth();
        int th = template.getHeight();
        long totalPixels = (long) tw * th;

        int sampleStep = Math.max(1, (int) Math.sqrt(totalPixels / 1000.0));

        long sampledPixels = 0;
        long sampledMatching = 0;

        for (int y = 0; y < th; y += sampleStep) {
            for (int x = 0; x < tw; x += sampleStep) {
                int screenRgb = screen.getRGB(offsetX + x, offsetY + y);
                int templateRgb = template.getRGB(x, y);

                int sr = (screenRgb >> 16) & 0xFF;
                int sg = (screenRgb >> 8) & 0xFF;
                int sb = screenRgb & 0xFF;

                int tr = (templateRgb >> 16) & 0xFF;
                int tg = (templateRgb >> 8) & 0xFF;
                int tb = templateRgb & 0xFF;

                int diff = Math.abs(sr - tr) + Math.abs(sg - tg) + Math.abs(sb - tb);
                if (diff < 30) {
                    sampledMatching++;
                }
                sampledPixels++;
            }
        }

        return sampledPixels == 0 ? 0 : (double) sampledMatching / sampledPixels;
    }

}
