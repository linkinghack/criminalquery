package com.linkinghack.criminalquerybase.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.linkinghack.criminalquerymodel.transfer_model.UniversalResponse;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "oss")
public class FileService {
    @Setter
    private String bucketName;
    @Setter
    private String endPoint;
    @Setter
    private String accessKeyID;
    @Setter
    private String accessKeySecret;

    public UniversalResponse uploadFile(MultipartFile file) {
        OSSClient client = new OSSClient(endPoint, accessKeyID, accessKeySecret);
        try {
            String fileID = Base64.getEncoder().encodeToString(
                    DigestUtils.md5Hex((file.getOriginalFilename() + UUID.randomUUID())).getBytes()
            ) + file.getOriginalFilename() ;
            PutObjectResult result = client.putObject(bucketName, fileID, new ByteArrayInputStream(file.getBytes()));
            client.shutdown();
            // 设置URL过期时间为1小时。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = client.generatePresignedUrl(bucketName, fileID, expiration);
            HashMap<String,String> resp = new HashMap<>();
            resp.put("fileID", fileID);
            resp.put("url", url.toString());
            return UniversalResponse.Ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return UniversalResponse.ServerFail("上传失败");
        }
    }

    public UniversalResponse deleteFile(String fileID) {
        OSSClient client = new OSSClient(endPoint, accessKeyID, accessKeySecret);
        client.deleteObject(bucketName, fileID);
        return UniversalResponse.Ok("deleted");
    }

    /**
     * 用于内部其他功能获取文件临时url
     * @param fileID OSS fileID
     * @param expire 过期时间
     * @return url
     */
    public String getTempraryURL(String fileID, Date expire) {
        if (expire == null) {
            expire = new Date(new Date().getTime() + 3600 * 1000);
        }
        OSSClient client = new OSSClient(endPoint, accessKeyID, accessKeySecret);
        URL url = client.generatePresignedUrl(bucketName, fileID, expire);
        return url.toString();
    }

}
