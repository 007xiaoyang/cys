package com.shengxian.controller;

import com.shengxian.common.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Description: 图片上传控制层
 *
 * @Author: yang
 * @Date: 2018-10-23
 * @Version: 1.0
 */
@Api(description = "图片上传")
@RestController
@RequestMapping("/fileupload")
public class UploadFileController {

    static Log log = LogFactory.getLog(UploadFileController.class);

    private static final String path= "/home/uploadImg/imgs/";//上传服务器路径
    private static final String pathfind="http://www.bxy8888.com/imgs/";//返回前端图片路径

    /**
     * 单张图片上传
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ApiOperation(value = "单张图片上传" ,httpMethod = "POST")
    @ApiImplicitParam(name = "file" ,value = "图片")
    public Message upload(@RequestParam("file") MultipartFile file){
        Message message = Message.non();
        Long time = null;
        //判断文件是否为空
        if (!file.isEmpty()){
            try {
            File directory   = new File(path);//随便指定目录，可以是完整目录，也可以存在Tomcat根目录下
            if(!directory .exists()){
                directory .mkdir();
            }
            time = System.currentTimeMillis();
            // 文件保存路径
            String filePath  =  path +time+file.getOriginalFilename();
                // 转存文件
            file.transferTo(new File(filePath ));
            } catch (IOException e) {
                log.info(e);
            }
        }
        return message.code(Message.codeSuccessed).data(pathfind+time+file.getOriginalFilename()).message("获取成功");
    }

    /**
     * 多张图片上传
     * @param files
     * @return
     */
    @RequestMapping("/uploadList")
    @ApiOperation(value = "多张图片上传" ,httpMethod = "POST")
    @ApiImplicitParam(name = "file" ,value = "图片")
    public Message uploadList(@RequestParam MultipartFile[] files){
        Message message = Message.non();
        //判断file数组不能为空并且长度大于0
        if (files != null && files.length>0){
            //循环获取file数组中得文件
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                upload(file);
            }
        }
        return message.code(Message.codeSuccessed).message("获取成功");
    }



}
