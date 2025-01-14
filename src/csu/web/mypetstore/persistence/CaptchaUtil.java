package csu.web.mypetstore.persistence;



import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    // 用于生成验证码字符的字符集
    private static final String CAPTCHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成指定长度的随机验证码文本
     */
    public static String generateCaptchaText(int length) {
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            captchaText.append(CAPTCHA_CHARS.charAt(random.nextInt(CAPTCHA_CHARS.length())));
        }
        return captchaText.toString();
    }

    /**
     * 根据给定的文本生成验证码图片
     */
    public static BufferedImage generateCaptchaImage(String text) {
        int width = 160;
        int height = 50;

        // 创建图像并设置背景色
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体和颜色，绘制验证码文本
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.BLACK);

        // 随机位置绘制验证码字符
        Random random = new Random();
        for (int i = 0; i < text.length(); i++) {
            int x = 30 + i * 20 + random.nextInt(10);
            int y = 35 + random.nextInt(10);
            g.drawString(String.valueOf(text.charAt(i)), x, y);
        }

        // 加入一些干扰线
        g.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose();
        return image;
    }
}
