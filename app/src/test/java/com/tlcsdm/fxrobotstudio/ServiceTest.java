package com.tlcsdm.fxrobotstudio;

import com.tlcsdm.fxrobotstudio.service.ImageMatcher;
import com.tlcsdm.fxrobotstudio.service.WorkflowExecutor;
import com.tlcsdm.fxrobotstudio.service.WorkflowRecorder;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void testWorkflowRecorderInitialState() {
        WorkflowRecorder recorder = new WorkflowRecorder();
        assertFalse(recorder.isRecording());
        assertTrue(recorder.getRecordedSteps().isEmpty());
    }

    @Test
    void testWorkflowExecutorInitialState() {
        WorkflowExecutor executor = new WorkflowExecutor();
        assertFalse(executor.isRunning());
    }

    @Test
    void testImageMatcherFindIdenticalImage() {
        ImageMatcher matcher = new ImageMatcher();
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        // Fill with solid color
        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                image.setRGB(x, y, 0xFF0000);
            }
        }

        BufferedImage template = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                template.setRGB(x, y, 0xFF0000);
            }
        }

        Rectangle result = matcher.findImage(image, template, 0.8);
        assertNotNull(result);
        assertEquals(20, result.width);
        assertEquals(20, result.height);
    }

    @Test
    void testImageMatcherNullInputs() {
        ImageMatcher matcher = new ImageMatcher();
        assertNull(matcher.findImage(null, (BufferedImage) null, 0.8));
    }

    @Test
    void testImageMatcherTemplateLargerThanScreen() {
        ImageMatcher matcher = new ImageMatcher();
        BufferedImage screen = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage template = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        assertNull(matcher.findImage(screen, template, 0.8));
    }

}
