package com.zhixinsou.wuyang;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;

public class SheetModel {
    private FileOutputStream fout;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;

    public SheetModel(FileOutputStream fout, HSSFWorkbook wb, HSSFSheet sheet) {
        this.fout = fout;
        this.wb = wb;
        this.sheet = sheet;
    }

    public FileOutputStream getFout() {
        return fout;
    }

    public void setFout(FileOutputStream fout) {
        this.fout = fout;
    }

    public HSSFWorkbook getWb() {
        return wb;
    }

    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }
}
