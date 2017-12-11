package com.neusoft.tg;
/**
 * dujl 2011-4-6 解密函数，通联算法
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import javax.crypto.Cipher;

public class CipherUtil {

	// 解密测试主函数
	public static void main(String[] args) {
		deCipher("D:/log/2017.txt","D:/log/2019.txt");
	}
	/**
	 * 解密方法
	 * @param sFilePath
	 * @param oFilePath
	 * @return
	 */
	public static int deCipher(String sFilePath,String oFilePath)
	{
		File file = new File(sFilePath);
		try{
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(10000);
			byte[] b = new byte[10000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			String sKey = "3132333435363738";
			//按tl提供key生成方法获得key值
			Key key = getKey(sKey.getBytes("GB2312"));
			Cipher encryptCipher = null;
			encryptCipher = (Cipher) Cipher.getInstance("DES/ECB/NoPadding");
			//解密
			encryptCipher.init(Cipher.DECRYPT_MODE, key);
			byte[] Decrypt=encryptCipher.doFinal(out.toByteArray());
			//写入文件
			FileOutputStream fos = null;

			fos=new FileOutputStream(oFilePath, false);
			fos.write(new String (Decrypt,"GBK").getBytes("GBK"));
			fos.flush();
			fos.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return 0;

	}
	private static Key getKey(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[8];

		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}


}
