package com.lin.controller;

import com.lin.annotation.Cacheable;
import com.lin.annotation.EncryptResponse;
import com.lin.config.DataPathConfig;
import com.lin.service.*;
import com.lin.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class DownLoadController {
    
    @Autowired
    private DataPathConfig dataPathConfig;
    
    @Autowired
    StrongStockService strongStockService;
    @Autowired
    LimitUpPoolService limitUpPoolService;
    @Autowired
    LimitDownPoolService limitDownPoolService;

    @Autowired
    LianBanChiService lianBanChiService;
    @Autowired
    VolumeTrendService volumeTrendService;

    @Autowired
    ZhaBanChiService zhaBanChiService;

    @Autowired
    IncreaseAndDecreaseService increaseAndDecreaseService;
    @Autowired
    PopularStockService popularStockService;
    @Autowired
    PluginKingService pluginKingService;


    @Cacheable(key = "downloadAllData:default", type = Cacheable.CacheType.DEFAULT, ttl = 300)
    @RequestMapping("/downloadAllData")
    @EncryptResponse(encryptAll = true)
    public ResponseEntity<Boolean> downloadAllData(HttpServletRequest request, Model model) {
        String dateIndex = "0";
        String date = CommonUtils.getTradeDay(Integer.valueOf(dateIndex));
//        date="20250919";
        strongStockService.downloadStrongSrockData(date);
        limitUpPoolService.downloadLimitUpData(date);
        limitDownPoolService.downloadLimitDownData(date);
        lianBanChiService.downloadData(date);
        volumeTrendService.saveOrUpdateData();
        zhaBanChiService.downloadData(date);
        increaseAndDecreaseService.downloadTrendData();
        increaseAndDecreaseService.generateTrendDataPhoto();
        increaseAndDecreaseService.downloadOneWordData();
        popularStockService.downLoadHotBoardAndConceptData();
        increaseAndDecreaseService.downloadWeakToStrongData();
        increaseAndDecreaseService.downDragonFirstGreenData();
        increaseAndDecreaseService.downStockPackagingData();
        increaseAndDecreaseService.downloadStockBuyLowStrong();
        increaseAndDecreaseService.downStockBuyLowNormalData();
        increaseAndDecreaseService.downStockIncreaseVolumeData();
        pluginKingService.downLoadRangeIncreaseStockData("3", "1");
        pluginKingService.downLoadRangeIncreaseStockData("5", "1");
        pluginKingService.downLoadRangeIncreaseStockData("10", "1");
        return ResponseEntity.ok(true);
    }

    /**
     * 下载所有数据文件为ZIP压缩包
     */
    @Cacheable(key = "downloadAllDataAsZip:default", type = Cacheable.CacheType.DEFAULT, ttl = 300)
    @RequestMapping("/downloadAllDataAsZip")
    public ResponseEntity<Resource> downloadAllDataAsZip(HttpServletRequest request, Model model) {
        try {
            // 先执行数据下载
            downloadAllData(request, model);
            
            // 创建ZIP文件
            String date = CommonUtils.getTradeDay(0);
            String zipFileName = "stock_data_" + date + ".zip";
            String zipFilePath = dataPathConfig.getTempFilePath(zipFileName);
            
            // 创建ZIP文件
            createZipFile(zipFilePath);
            
            // 创建文件资源
            File zipFile = new File(zipFilePath);
            Resource resource = new FileSystemResource(zipFile);
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
                    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 创建ZIP文件，包含所有下载的数据文件
     */
    private void createZipFile(String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            // 确保数据根目录存在
            dataPathConfig.createDataRootPathIfNotExists();
            
            // 使用配置的数据根目录
            File dataRootDir = new File(dataPathConfig.getRootPath());
            
            if (dataRootDir.exists() && dataRootDir.isDirectory()) {
                addDirectoryToZip(zos, dataRootDir, "");
            } else {
                // 如果配置的目录不存在，尝试当前工作目录下的常见数据目录
                String currentDir = System.getProperty("user.dir");
                String[] possibleDirs = {"data", "downloads", "files", "output"};
                for (String dirName : possibleDirs) {
                    File dir = new File(currentDir, dirName);
                    if (dir.exists() && dir.isDirectory()) {
                        addDirectoryToZip(zos, dir, dirName + "/");
                    }
                }
            }
        }
    }
    
    /**
     * 递归添加目录到ZIP文件
     */
    private void addDirectoryToZip(ZipOutputStream zos, File dir, String basePath) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归处理子目录
                    addDirectoryToZip(zos, file, basePath + file.getName() + "/");
                } else {
                    // 添加文件到ZIP
                    String entryName = basePath + file.getName();
                    ZipEntry zipEntry = new ZipEntry(entryName);
                    zos.putNextEntry(zipEntry);
                    
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                    }
                    zos.closeEntry();
                }
            }
        }
    }
}
