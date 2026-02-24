package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Branding {
    public Font jetBrainsBExtraSmall, jetBrainsBSmall, jetBrainsBMedium, jetBrainsBLarge, jetBrainsBExtraLarge, jetBrainsBGiant;
    public Font jetBrainsRExtraSmall, jetBrainsRSmall, jetBrainsRMedium, jetBrainsRLarge, jetBrainsRExtraLarge, jetBrainsRGiant;
    public ImageIcon icoSimulate, icoHowToUse, icoSettings, icoExit;
    public ImageIcon icoAddProcess, icoRemoveProcess, icoImportProcess, icoRandomProcess;

    public Branding() {
        initializeFonts();
        initializeImages();
    }

    public Color mainColor = Color.decode("#09090B");
    public Color white = Color.decode("#ffffff");
    public Color[] processColor = {
        Color.decode("#b75a5a"),
        Color.decode("#b7845a"),
        Color.decode("#b7b45a"),
        Color.decode("#8ab75a"),
        Color.decode("#5ab760"),
        Color.decode("#5ab78a"),
        Color.decode("#5ab7b4"),
        Color.decode("#5a9db7"),
        Color.decode("#5a76b7"),
        Color.decode("#715ab7"),
        Color.decode("#9a5ab7"),
        Color.decode("#b75aaf"),
        Color.decode("#b75a7e"),
        Color.decode("#d63636"),
        Color.decode("#d69136"),
        Color.decode("#c9d636"),
        Color.decode("#36d63b"),
        Color.decode("#36d6d6"),
        Color.decode("#3669d6"),
        Color.decode("#7e36d6")
    };

    public void initializeFonts() {
        System.out.println("Loading Fonts...");

        try {
            Font jetBrainsB = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/fonts/JetBrainsMono-Bold.ttf"));
            jetBrainsBExtraSmall = jetBrainsB.deriveFont(Font.BOLD, 10f);
            jetBrainsBSmall = jetBrainsB.deriveFont(Font.BOLD, 14f);
            jetBrainsBMedium = jetBrainsB.deriveFont(Font.BOLD, 16f);
            jetBrainsBLarge = jetBrainsB.deriveFont(Font.BOLD, 24f);
            jetBrainsBExtraLarge = jetBrainsB.deriveFont(Font.BOLD, 32f);
            jetBrainsBGiant = jetBrainsB.deriveFont(Font.BOLD, 200f);

            Font jetBrainsR = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/assets/fonts/JetBrainsMono-Regular.ttf"));
            jetBrainsRExtraSmall = jetBrainsR.deriveFont(Font.BOLD, 10f);
            jetBrainsRSmall = jetBrainsR.deriveFont(Font.BOLD, 14f);
            jetBrainsRMedium = jetBrainsR.deriveFont(Font.BOLD, 16f);
            jetBrainsRLarge = jetBrainsR.deriveFont(Font.BOLD, 24f);
            jetBrainsRExtraLarge = jetBrainsR.deriveFont(Font.BOLD, 32f);
            jetBrainsRGiant = jetBrainsR.deriveFont(Font.BOLD, 200f);
        } catch (FontFormatException | IOException e){
            System.err.println("Font failed to load.");
        }
    }

    public void initializeImages(){
        System.out.println("Loading Images...");
        try {
            BufferedImage buff_icoSimulate = ImageIO.read(getClass().getResourceAsStream("/assets/icons/simulate.png"));
            BufferedImage buff_icoHowToUse = ImageIO.read(getClass().getResourceAsStream("/assets/icons/how_to_use.png"));
            BufferedImage buff_icoSettings = ImageIO.read(getClass().getResourceAsStream("/assets/icons/settings.png"));
            BufferedImage buff_icoExit = ImageIO.read(getClass().getResourceAsStream("/assets/icons/exit.png"));

            BufferedImage buff_icoAddProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/add_process.png"));
            BufferedImage buff_icoRemoveProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/remove_process.png"));
            BufferedImage buff_icoImportProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/import_process.png"));
            BufferedImage buff_icoRandomProcess = ImageIO.read(getClass().getResourceAsStream("/assets/icons/random_process.png"));

            icoSimulate = resizeImage(buff_icoSimulate, 0.06f);
            icoHowToUse = resizeImage(buff_icoHowToUse, 0.06f);
            icoSettings = resizeImage(buff_icoSettings, 0.06f);
            icoExit = resizeImage(buff_icoExit, 0.06f);

            icoAddProcess = resizeImage(buff_icoAddProcess, 0.06f);
            icoRemoveProcess = resizeImage(buff_icoRemoveProcess, 0.06f);
            icoImportProcess = resizeImage(buff_icoImportProcess, 0.06f);
            icoRandomProcess = resizeImage(buff_icoRandomProcess, 0.06f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ImageIcon resizeGIF(ImageIcon icon, int width, int height) {
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }

    public ImageIcon resizeImage(BufferedImage original, float scale) {
        int newWidth  = Math.round(original.getWidth()  * scale);
        int newHeight = Math.round(original.getHeight() * scale);

        Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return new ImageIcon(resized);
    }

    public ImageIcon resizeImageIcon(ImageIcon original, float scale) {
        Image img = original.getImage();
        
        int newWidth  = Math.round(img.getWidth(null)  * scale);
        int newHeight = Math.round(img.getHeight(null) * scale);

        Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        
        return new ImageIcon(resized);
    }
}