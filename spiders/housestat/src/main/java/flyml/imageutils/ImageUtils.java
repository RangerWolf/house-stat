package flyml.imageutils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import com.google.common.collect.Lists;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class ImageUtils {
	
	/**
     * 对图片进行放大
     * @param originalImage 原始图片
     * @param times 放大倍数
     * @return
     */
    public static BufferedImage  zoomInImage(BufferedImage  originalImage, Integer times){
        int width = originalImage.getWidth()*times;
        int height = originalImage.getHeight()*times;
        BufferedImage newImage = new BufferedImage(width,height,originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0,0,width,height,null);
        g.dispose();
        return newImage;
    }
	
	public static String extractStringFromImg(String imgPath) {
		ITesseract instance = new Tesseract();
		List<String> configs = Lists.newArrayList("digits");
		instance.setConfigs(configs);
		BufferedImage bi = SoundBinImage.image2Binary(imgPath, 150);
//		bi = zoomInImage(bi, 2);
		
		// 备注：
		// 解析杭州的图片的时候， 需要设置150，并且不放大
		// 解析南京的图片的时候，需要设置到210并且放大两倍
		// 并且目前还不能解析小数点，同时只解析数字，没有包括英文字母
		instance.setDatapath("D:/Program Files/Tesseract-OCR/tessdata");
		String s = "Error";
		try {
//			s = instance.doOCR(new File(imgPath));
			s = instance.doOCR(bi);
		} catch (TesseractException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static void main(String[] args) {
		System.out.println(extractStringFromImg("E:/test_number3.png"));
	}
	
}
