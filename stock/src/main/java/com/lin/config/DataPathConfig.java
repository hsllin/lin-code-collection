package com.lin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 数据路径配置类
 * 用于管理不同操作系统下的数据存储路径
 */
@Component
@ConfigurationProperties(prefix = "data")
public class DataPathConfig {
    
    /**
     * 数据存储根目录
     * Windows: D:\1stock
     * Linux: /opt/stock
     */
    private String rootPath = "D:/1stock";
    
    /**
     * 临时文件目录
     */
    private String tempPath = System.getProperty("java.io.tmpdir");
    
    /**
     * 获取数据根目录路径
     * @return 数据根目录路径
     */
    public String getRootPath() {
        return rootPath;
    }
    
    /**
     * 设置数据根目录路径
     * @param rootPath 数据根目录路径
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    
    /**
     * 获取临时文件目录路径
     * @return 临时文件目录路径
     */
    public String getTempPath() {
        return tempPath;
    }
    
    /**
     * 设置临时文件目录路径
     * @param tempPath 临时文件目录路径
     */
    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }
    
    /**
     * 获取完整的数据文件路径
     * @param fileName 文件名
     * @return 完整路径
     */
    public String getDataFilePath(String fileName) {
        return rootPath + System.getProperty("file.separator") + fileName;
    }
    
    /**
     * 获取完整的临时文件路径
     * @param fileName 文件名
     * @return 完整路径
     */
    public String getTempFilePath(String fileName) {
        return tempPath + System.getProperty("file.separator") + fileName;
    }
    
    /**
     * 检查数据根目录是否存在
     * @return 是否存在
     */
    public boolean isDataRootPathExists() {
        return new java.io.File(rootPath).exists();
    }
    
    /**
     * 创建数据根目录（如果不存在）
     * @return 是否创建成功
     */
    public boolean createDataRootPathIfNotExists() {
        java.io.File dir = new java.io.File(rootPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }
    
    /**
     * 获取数据文件的FileWriter
     * @param fileName 文件名
     * @return FileWriter
     * @throws IOException IO异常
     */
    public java.io.FileWriter getDataFileWriter(String fileName) throws IOException {
        createDataRootPathIfNotExists();
        return new java.io.FileWriter(getDataFilePath(fileName));
    }
    
    /**
     * 获取数据文件的File对象
     * @param fileName 文件名
     * @return File对象
     */
    public java.io.File getDataFile(String fileName) {
        createDataRootPathIfNotExists();
        return new java.io.File(getDataFilePath(fileName));
    }
    
    /**
     * 获取图片文件的完整路径
     * @param fileName 图片文件名
     * @return 完整路径
     */
    public String getImageFilePath(String fileName) {
        return getDataFilePath(fileName);
    }
}
