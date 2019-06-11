package com.shengxian.service;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {

    void excelDownload(HttpServletResponse response, String fileName, Workbook workbook);



}
