package com.shengxian.service.impl;

import com.shengxian.service.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Description:
 *
 * @Author: yang
 * @Date: 2018-10-31
 * @Version: 1.0
 */
@Service
public class ExcelServiceImpl implements ExcelService {


    @Override
    public void excelDownload(HttpServletResponse response, String fileName, Workbook workbook) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.reset();
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition","attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xls");
            workbook.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
