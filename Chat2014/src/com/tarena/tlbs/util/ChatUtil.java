package com.tarena.tlbs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ChatUtil {
	public static String getAudio(String body)
	{
		String audioFilePath="";
		FileOutputStream fos=null;
		try {
			body=body.substring(Const.TYPE_AUDIO.length(), body.length()-Const.TYPE_END_TAG.length());
			//base64
			byte[] data=Base64.decode(body, Base64.DEFAULT);
			//writeFile //mnt/sdcard/tlbs/audio.mp3
			fos=new FileOutputStream(getAudioFile());
			fos.write(data);
			fos.close();
			audioFilePath=getAudioFile().getAbsolutePath();
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			try {
				if (fos!=null) fos.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return audioFilePath;
	}
	public static String addAudioTag(File file) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Const.TYPE_AUDIO);
		// ���ļ�
		FileInputStream fis = null;
		try {

			//1,�Ƽ��� ��ζ� buffer 8k
			//2,һ�ζ���
			fis=new FileInputStream(file);
			int dataLength=(int) file.length();
			byte[]  data=new byte[dataLength];
			fis.read(data);
			String body=Base64.encodeToString(data, Base64.DEFAULT);
			stringBuilder.append(body);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stringBuilder.append(Const.TYPE_END_TAG);
		return stringBuilder.toString();

	}


	/**
	 * �����͵�ͼƬ����
	 * @param imageData
	 * @return
	 */
	public static String addImageTag(byte[] imageData)
	{
		StringBuilder stringBuilder=new StringBuilder();
		
		stringBuilder.append(Const.TYPE_IMAGE);
		String body=Base64.encodeToString(imageData, Base64.DEFAULT);
		stringBuilder.append(body);
		stringBuilder.append(Const.TYPE_END_TAG);
		return stringBuilder.toString();
	}
	
	/**
	 * �յ����죬���ݺ�ת��bitmap
	 * @param body
	 * @return
	 */
	public static Bitmap getImage(String body)
	{
		//ȡ�м��ͼƬ����
		body=body.substring(Const.TYPE_IMAGE.length(), body.length()-Const.TYPE_END_TAG.length());
		//ת��byte[]
		byte[] imageData=Base64.decode(body, Base64.DEFAULT);
		//ת��bitmap
		Bitmap bitmap=BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
		return bitmap;
	}

	/**
	 * ���ͱ���ʱ����һ��tag
	 * 
	 * @param faceImageId
	 * @return
	 */
	public static String addFaceHeadTag(int faceImageId) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Const.TYPE_FACE);
		stringBuilder.append(String.valueOf(faceImageId));
		stringBuilder.append(Const.TYPE_END_TAG);
		return stringBuilder.toString();
	}
	
	public static String addTextHeadTag(String text) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Const.TYPE_TEXT);
		stringBuilder.append(text);
		stringBuilder.append(Const.TYPE_END_TAG);
		return stringBuilder.toString();
	}

	/**
	 * �õ����ݵ����� text,face,image,audio.map
	 * 
	 * @param body
	 * @return
	 */
	public static String getType(String body) {
		// type_face=<!---face>
		if (body.startsWith(Const.TYPE_FACE)) {
			return Const.TYPE_FACE;
		} else if (body.startsWith(Const.TYPE_IMAGE)) {
			return Const.TYPE_IMAGE;
		}else if (body.startsWith(Const.TYPE_TEXT)) {
			return Const.TYPE_TEXT;
		}else if (body.startsWith(Const.TYPE_AUDIO)) {
			return Const.TYPE_AUDIO;
		}else if (body.startsWith(Const.TYPE_MAP)) {
			return Const.TYPE_MAP;
		}
		return null;
	}
	
	public static int getFaceImageId(String body)
	{
		//<!---face>2130837519<-->
		//��Ҫд10 -4,����ά��
		String id=body.substring(Const.TYPE_FACE.length(), body.length()-Const.TYPE_END_TAG.length());
		return Integer.parseInt(id);
		
	}
	
	public static String getText(String body)
	{
		//<!---face>2130837519<-->
		//��Ҫд10 -4,����ά��
		String text=body.substring(Const.TYPE_TEXT.length(), body.length()-Const.TYPE_END_TAG.length());
		return text;
		
	}
	/**
	 * ����һ���ļ� 
	 * @return
	 */
		public static File getAudioFile() {

			File fileSdcardRoot=android.os.Environment.getExternalStorageDirectory();
			//ÿ��Ӧ�õ��ļ� �������Լ���Ŀ¼��
			File fileDirectory=new File(fileSdcardRoot,"tlbs");
			if (!fileDirectory.exists())
			{
				fileDirectory.mkdirs();
			}
			//mnt/sdcard/tlbs/audio.mp3
			File fileAudio=new File(fileDirectory,"audio.mp3");
			return fileAudio;
			
		}

}
