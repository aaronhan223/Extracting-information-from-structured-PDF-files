package com.zhixinsou.wuyang;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private static Map<String, File> fileCache = new HashMap<>();
    
    public static String pdfReader(String filePath) {
        String result = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(filePath));
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            result = stripper.getText(document);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    public static String pdfReader(String filePath, int startPage, int endPage) {
        File file = fileCache.get(filePath);
        if (file == null)
            file = new File(filePath);
        return pdfReader(file, startPage, endPage);
    }

    public static String pdfReader(String filePath, int startPage, int endPage, boolean sortByPosition) {
        File file = fileCache.get(filePath);
        if (file == null)
            file = new File(filePath);
        return pdfReader(file, startPage, endPage, sortByPosition);
    }
    
    public static String pdfReader(File file, int startPage, int endPage) {
        return pdfReader(file, startPage, endPage, true);
    }
    
    public static String pdfReader(File file, int startPage, int endPage, boolean sortByPosition) {
        String result = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(sortByPosition);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            result = stripper.getText(document);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println(result);
        return result;
    }

    public static String pdfReaderWithFonts(String filePath, int startPage, int endPage) {
        return pdfReaderWithFonts(filePath, startPage, endPage);
    }
    
    public static String pdfReaderWithFonts(String filePath, int startPage, int endPage, boolean sortByPosition) {
        String result = null;
        PDDocument document = null;
        try {
            File file = fileCache.get(filePath);
            if (file == null)
                file = new File(filePath);
            
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper(){
                String prevBaseFont = "";
                double prevBaseFontSize = 0;

                protected void writeString(String text, List<TextPosition> textPositions) throws IOException
                {
                    StringBuilder builder = new StringBuilder();

                    for (TextPosition position : textPositions)
                    {
                        String baseFont = position.getFont().getName();
                        double baseFontSize = position.getFontSizeInPt();
                        if(baseFont != null && baseFont.contains("IIGGKO"))
                            baseFont = baseFont.replace("IIGGKO", "IIGGKN");
                        if (baseFont != null && (!baseFont.equals(prevBaseFont) || baseFontSize != prevBaseFontSize))
                        {
                            builder.append('[').append(baseFont).append("]<" + baseFontSize + ">");
                            prevBaseFont = baseFont;
                            prevBaseFontSize = baseFontSize;
                        }
                        builder.append(position.getUnicode());
                    }

                    writeString(builder.toString());
                }
            };
            stripper.setSortByPosition(sortByPosition);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            result = stripper.getText(document);
            document.getPages().get(0).getMatrix();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    public static SheetModel makeSheet(String filePath, String sheetName, List<String> headers) {
        FileOutputStream fout = null;
        HSSFWorkbook wb = null;
        try {
            fout = new FileOutputStream(filePath, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        sheet.setDefaultColumnWidth((short) 22);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);

        for (int i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(style);
        }

        return new SheetModel(fout, wb, sheet);
    }
    
    public static void exportSheet(HSSFSheet sheet, List<String[]> data) {
        int startRowNo = sheet.getLastRowNum() + 1;
        for (int i = 0; i < data.size(); i++) {
            HSSFRow row = sheet.createRow(startRowNo + i);
            String[] line = data.get(i);
            for (int j = 0; j < line.length; j++) {
                HSSFCell cell = row.createCell((short) j);
                cell.setCellValue(new HSSFRichTextString(line[j]));
            }
        }
    }

    public static void closeSheet(SheetModel sheet) {
        if(sheet.getFout() != null) {
            try {
                sheet.getWb().write(sheet.getFout());
                sheet.getFout().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean exportCsv(String filePath, List<String> dataList, boolean append){
        boolean isSucess=false;

        FileOutputStream out=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            out = new FileOutputStream(filePath, append);
            osw = new OutputStreamWriter(out);
            bw =new BufferedWriter(osw);
            if(dataList!=null && !dataList.isEmpty()){
                for(String data : dataList){
                    bw.append(data).append("\r");
                }
            }
            isSucess=true;
        } catch (Exception e) {
            isSucess=false;
        }finally{
            if(bw!=null){
                try {
                    bw.close();
                    bw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(osw!=null){
                try {
                    osw.close();
                    osw=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return isSucess;
    }
}
