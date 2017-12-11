package dec;

import org.apache.activemq.console.command.store.tar.TarEntry;
import org.apache.activemq.console.command.store.tar.TarInputStream;
import org.apache.activemq.console.command.store.tar.TarOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarUtils {
    public static void main(String[] args) {
        rar("D:/log/2017back.txt","D:/log/2018back.tar");
    }
    public static void rar(String filePath, String rarFilePath) {
        File srcFile = new File(filePath);//要归档的文件对象
        File targetTarFile = new File(rarFilePath);//归档后的文件名
        TarOutputStream out = null;
        boolean boo = false;//是否压缩成功
        try {
            out = new TarOutputStream(new BufferedOutputStream(new FileOutputStream(targetTarFile)));
            tar(srcFile, out, "", true);
            boo = true;
           // return targetTarFile;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {

            try {
                if (out != null)
                    out.close();
            } catch (IOException ex) {
                throw new RuntimeException("关闭Tar输出流出现异常", ex);
            } finally {
                //清理操作
                if (!boo && targetTarFile.exists())//归档不成功,
                    targetTarFile.delete();
            }
        }
    }

    public static void tar(File file, TarOutputStream out, String dir, boolean boo) throws IOException {
        if (file.isDirectory()) {//是目录
            File[] listFile = file.listFiles();//得出目录下所有的文件对象
            if (listFile.length == 0 && boo) {//空目录归档
                out.putNextEntry(new TarEntry(dir + file.getName() + "/"));//将实体放入输出Tar流中
                System.out.println("归档." + dir + file.getName() + "/");
                return;
            } else {
                for (File cfile : listFile) {
                    tar(cfile, out, dir + file.getName() + "/", boo);//递归归档
                }
            }
        } else if (file.isFile()) {//是文件
            System.out.println("归档." + dir + file.getName() + "/");
            byte[] bt = new byte[2048 * 2];
            TarEntry ze = new TarEntry(dir + file.getName());//构建tar实体
            //设置压缩前的文件大小
            ze.setSize(file.length());
            //ze.setName(file.getName());//设置实体名称.使用默认名称
            out.putNextEntry(ze);////将实体放入输出Tar流中
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                int i = 0;
                while ((i = fis.read(bt)) != -1) {//循环读出并写入输出Tar流中
                    out.write(bt, 0, i);
                }
            } catch (IOException ex) {
                throw new IOException("写入归档文件出现异常", ex);
            } finally {
                try {
                    if (fis != null)
                        fis.close();//关闭输入流
                    out.closeEntry();
                } catch (IOException ex) {
                    throw new IOException("关闭输入流出现异常");
                }

            }
        }

    }
    public void unTar(File file, String outputDir) throws IOException {

        TarInputStream tarIn = null;
        try {
            tarIn = new TarInputStream(new FileInputStream(file), 1024 * 2);
            createDirectory(outputDir, null);//创建输出目录
            TarEntry entry = null;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (entry.isDirectory()) {//是目录
                    createDirectory(outputDir, entry.getName());//创建空目录
                } else {//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/", null);//创建输出目录
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while ((length = tarIn.read(b)) != -1) {
                            out.write(b, 0, length);
                        }
                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if (out != null)
                            out.close();
                    }
                }
            }

        } catch (IOException ex) {
            throw new IOException("解压归档文件出现异常", ex);
        } finally {
            try {
                if (tarIn != null) {
                    tarIn.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭tarFile出现异常", ex);
            }
        }

    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public void createDirectory(String outputDir, String subDir) {

        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
