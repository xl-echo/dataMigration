package com.echo.one;

import java.io.File;

/**
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/23 11:37
 */
public class DrawFilePathTest {

    public static void main(String[] args) {
        //表示一个文件路径
        String path = "F:\\codework\\dataMigration\\";
        getFiles(path);
    }

    /**
     * 递归获取某路径下的所有文件，文件夹，并输出
     */
    private static void getFiles(String clientBase) {
        File file = new File(clientBase);
        // 如果这个路径是文件夹
        if (file.isDirectory()) {
            // 获取路径下的所有文件
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                // 如果还是文件夹 递归获取里面的文件 文件夹
                if (files[i].isDirectory()) {
                    //继续读取文件夹里面的所有文件
                    getFiles(files[i].getPath());
                } else {
                    joinPath(files[i].getPath());
                }
            }
        } else {
            joinPath(file.getPath());
        }
    }

    private static void joinPath(String path) {
        if (!path.contains("target") && !path.contains(".idea") && !path.contains(".git")) {
            System.out.println(path);
        }
    }

}
