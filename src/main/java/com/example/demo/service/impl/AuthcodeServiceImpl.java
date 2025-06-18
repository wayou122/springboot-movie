package com.example.demo.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.example.demo.service.AuthcodeService;

@Service
public class AuthcodeServiceImpl implements AuthcodeService {

	@Override
	public String generateAuthcode() {
		String chars = "3456789abcdefghjkmnpqrstuvwxyABCDEFGHJKLMNPQRSTUVWXY";
		//String chars = "5";
		int length = 4;
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0; i<length; i++) {
			int index = random.nextInt(chars.length()); // 隨機取位置
			sb.append(chars.charAt(index)); // 取得該位置的資料
		}
		return sb.toString();
	}

	@Override
	public byte[] generateAuthcodeImg(String authcode) throws IOException{
		
		// 創建一個80x30像素的RGB圖像
		Random rand = new Random();
		BufferedImage img = new BufferedImage(80,30,BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.LIGHT_GRAY); //設定顏色填滿背景
		g.fillRect(0,0,80,30);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD, 20));
		//寫上驗證碼
    for (int i = 0; i < authcode.length(); i++) {
      g.setColor(new Color(rand.nextInt(150), rand.nextInt(150), rand.nextInt(150)));
      int x = 5 + i * 15;
      int y = 15 + rand.nextInt(5); 
      g.drawString(String.valueOf(authcode.charAt(i)), x, y); 
    }
		//干擾線
		g.setColor(Color.WHITE);
		Random random = new Random();
		for (int i=0; i<5; i++) {
			int x1 = random.nextInt(80);
			int y1 = random.nextInt(30);
			int x2 = random.nextInt(80);
			int y2 = random.nextInt(30);
			g.drawLine(x1,y1,x2,y2);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img,"JPG",baos);
    return baos.toByteArray();
	}

}
