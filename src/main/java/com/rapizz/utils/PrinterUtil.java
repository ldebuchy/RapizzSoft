package com.rapizz.utils;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class PrinterUtil {

    public static void printFicheLivraison(JPanel panel, String title) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        pf.setOrientation(PageFormat.PORTRAIT);

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // Convertir le panel en image
                int w = panel.getWidth();
                int h = panel.getHeight();
                BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                panel.paint(image.getGraphics());

                // Calculer le ratio pour que l'image tienne sur la page
                double scaleX = pageFormat.getImageableWidth() / w;
                double scaleY = pageFormat.getImageableHeight() / h;
                double scale = Math.min(scaleX, scaleY);

                // Appliquer la transformation
                g2d.scale(scale, scale);

                // Dessiner l'image
                g2d.drawImage(image, 0, 0, null);

                return PAGE_EXISTS;
            }
        }, pf);

        try {
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null,
                "Erreur lors de l'impression : " + e.getMessage(),
                "Erreur d'impression",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 