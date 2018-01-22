package com.zhixinsou.wuyang;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessWellsFargoSecurities {
    public static List<AnalysisReport> getAnalysisReportPageInfo(String filePath) {
        List<AnalysisReport> reports = new ArrayList<>();
        int startPage = 1;
        File file = new File(filePath);
        String[] lines = FileUtil.pdfReader(file, startPage, startPage).split("\n");
        Pattern pattern = Pattern.compile("([0-9]+)\\s-\\s([0-9]+)$");
        while (lines.length > 1 && lines[0].equals("Table of Contents")) {
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    AnalysisReport report = new AnalysisReport();
                    report.setFilePath(filePath);
                    report.setFileName(file.getName());
                    report.setStartPage(Integer.parseInt(matcher.group(1)));
                    report.setEndPage(Integer.parseInt(matcher.group(2)));
                    report.setBrokerName("Wr Hambrecht.");

                    reports.add(report);
                }
            }

            startPage++;
            lines = FileUtil.pdfReader(filePath, startPage, startPage).split("\n");
        }
        return reports;
    }

    public static void getAnalysisReportDetail2003(AnalysisReport report) {
        getAnalysisReportDetail2005_internal(report);
    }

    public static void getAnalysisReportDetail2005_internal(AnalysisReport report) {
        String content = FileUtil.pdfReader(report.getFilePath(), report.getStartPage(), report.getStartPage(), false);
        String[] lines = content.split("\n");
        int dateLine = 0;
        Pattern patternDate = Pattern.compile("^(January|February|March|April|May|June|July|August|September|October|November|December){1}[^\r\n0-9]+[0-9]{1,2},[^\r\n0-9][0-9]{4}");
        for (String line : lines) {
            Matcher matcherDate = patternDate.matcher(line.trim());
            dateLine++;
            if (matcherDate.find()) {
                report.setDate(matcherDate.group());
                break;
            }
        }

        if (StringUtil.isEmpty(report.getDate())) {
            patternDate = Pattern.compile("^(January|February|March|April|May|June|July|August|September|October|November|December){1}\\s*[0-9]+,\\s*[0-9]{4}");
            dateLine++;
            for (String line : lines) {
                Matcher matcherDate = patternDate.matcher(line.trim());
                dateLine++;
                if (matcherDate.find()) {
                    report.setDate(matcherDate.group());
                    break;
                }
            }
        }

        if (StringUtil.isEmpty(report.getDate())) {
            patternDate = Pattern.compile("^(JANUARY|FEBRUARY|MARCH|APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER){1}\\s*[0-9]+,\\s*[0-9]{4}");
            dateLine = 0;
            for (String line : lines) {
                Matcher matcherDate = patternDate.matcher(line.trim());
                dateLine++;
                if (matcherDate.find()) {
                    report.setDate(matcherDate.group());
                    break;
                }
            }
        }
        if (StringUtil.isEmpty(report.getDate())) {
            patternDate = Pattern.compile("^[0-9]+\\s+(January|February|March|April|May|June|July|August|September|October|November|December){1}\\s+[0-9]{4}");
            dateLine = 0;
            for (String line : lines) {
                dateLine++;
                Matcher matcherDate = patternDate.matcher(line.trim());
                if (matcherDate.find()) {
                    report.setDate(matcherDate.group());
                    break;
                }
            }
        }
        if (StringUtil.isEmpty(report.getDate())) {
            patternDate = Pattern.compile("^(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec|Sep){1}\\.\\s*[0-9]+,\\s*[0-9]{4}");
            dateLine = 0;
            for (String line : lines) {
                dateLine++;
                Matcher matcherDate = patternDate.matcher(line.trim());
                if (matcherDate.find()) {
                    report.setDate(matcherDate.group());
                    break;
                }
            }
        }

        if (report.getAnalystNames() == null || report.getAnalystNames().size() == 0) {
            Pattern patternName = Pattern.compile("^([^\\(]+)[^@]+@");
            for (int i = 0;i < lines.length; i++) {
                Matcher matcherName = patternName.matcher(lines[i].trim());
                while (matcherName.find()) {
                    //System.out.println(i);
                    if(!lines[i - 1].trim().equals(""))
                        report.addAnalystName(lines[i - 1].trim());
                }
            }
        }
        if (report.getAnalystNames() == null || report.getAnalystNames().size() == 0) {
            Pattern patternName = Pattern.compile("([^\\(]+)\\([0-9\\s]+\\)\\s*[0-9]{3}-[0-9]{4}");
            for (int i = 0;i < lines.length;i++) {
                Matcher matcherName = patternName.matcher(lines[i].trim());
                while (matcherName.find()) {
                    report.addAnalystName(matcherName.group(1));
                }
            }
        }

        
        Pattern patternCompanyName = Pattern.compile("([a-zA-Z0-9&\\s\\.,’\\-–!]+)\\s*\\(\\s*([a-zA-Z]+)\\s*[-–]{1}(.*)\\)");
        for (String line : lines) {
            Matcher matcher = patternCompanyName.matcher(line.trim());
            if (matcher.find()) {
                report.setCompanyName(matcher.group(1));
                report.setTicker(matcher.group(2));
                break;
            }
        }
        if (StringUtil.isEmpty(report.getCompanyName())) {
            boolean nameFound = false;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (!nameFound && line.equals("Equity Research")) {
                    nameFound = true;
                    continue;
                }

                if (nameFound) {
                    if (StringUtil.isEmpty(line)) {
                        continue;
                    }

                    report.setCompanyName(line);

                    Pattern patternTicker = Pattern.compile("^([a-zA-Z]+):");
                    Matcher matcherTicker = patternTicker.matcher(lines[i + 1].trim());
                    if (matcherTicker.find()) {
                        report.setTicker(matcherTicker.group(1));
                    }
                    
                    break;
                }
            }
        }
        if (StringUtil.isEmpty(report.getCompanyName())) {
            boolean nameFound = false;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (!nameFound && line.endsWith("EQUITY RESEARCH DEPARTMENT")) {
                    nameFound = true;
                    continue;
                }

                if (nameFound) {
                    if (StringUtil.isEmpty(line)) {
                        continue;
                    }

                    report.setCompanyName(line);

                    Pattern patternTicker = Pattern.compile("^([a-zA-Z]+):");
                    Matcher matcherTicker = patternTicker.matcher(lines[i + 1].trim());
                    if (matcherTicker.find()) {
                        report.setTicker(matcherTicker.group(1));
                    }

                    break;
                }
            }
        }


        Pattern patternRating = Pattern.compile("^RATING:(.*)$");
        Pattern patternTicker = Pattern.compile(("^([A-Z]+):([\\sA-Z]+)$"));
        Pattern patternPrice = Pattern.compile("^PRICE:(.*)$");
        for (int i = 0; i < lines.length; i++) {
            Matcher matcher = patternRating.matcher(lines[i].trim());
            if (matcher.find()) {
                if (i > 0) {
                    Matcher preMatcher = patternPrice.matcher(lines[i-1].trim());
                    if (!preMatcher.find()) {
                        preMatcher = patternTicker.matcher(lines[i-1].trim());
                        if (preMatcher.find()) {
                            report.setTicker(preMatcher.group(2));
                            report.setExchange(preMatcher.group(1));
                        }
                    }
                }
                if (i > 1) {
                    Matcher sndMatcher = patternPrice.matcher(lines[i-2].trim());
                    if (!sndMatcher.find()) {
                        sndMatcher = patternTicker.matcher(lines[i-2].trim());
                        if (sndMatcher.find()) {
                            report.setTicker(sndMatcher.group(2));
                            report.setExchange(sndMatcher.group(1));
                        }
                    }
                }

                report.setRating(matcher.group(1));
                break;
            }
        }
        if (StringUtil.isEmpty(report.getRating())) {
            patternRating = Pattern.compile("^([A-Z]{1}[a-z]+\\s+)*([A-Z]{1}[a-z]+perform|Perform){1}( / V){0,1}$");
            for (String line : lines) {
                Matcher matcher = patternRating.matcher(line.trim());
                if (matcher.find()) {
                    report.setRating(line.trim());
                    break;
                }
            }
        }
        
        
        content = FileUtil.pdfReader(report.getFilePath(), report.getStartPage(), report.getEndPage());
        lines = content.split("\n");
        Pattern patternSentence1 = Pattern.compile("^(?=.*?\\bhas\\b)(?=.*?\\ba\\b)(?=.*?\\blong\\b).*$");
        for (String line : lines) {
            Matcher matcher1 = patternSentence1.matcher(line);
            if (matcher1.find()) {
                report.addOwnershipDisclosure(matcher1.group());
            }
        }


        System.out.println(JSON.toJSON(report));
    }

    public static void process5(String rootDir, String resultPath) {
        List<AnalysisReport> reports = new ArrayList<>();
        File[] Files = new File(rootDir).listFiles();
        System.out.println(Files.length);
        for (File file : Files) {
            if(file.getName().endsWith(".pdf")) {
                try {
                    reports.addAll(getAnalysisReportPageInfo(file.getPath()));
                } catch (Exception e) {
                    System.out.println(file.getName());
                    e.printStackTrace();
                    continue;
                }
            }
        }

        //以下代码为写入excel
        List<String> headers = new ArrayList<>();
        headers.add("Date");
        headers.add("Broker name");
        headers.add("Analyst name");
        headers.add("Company name");
        headers.add("Exchange");
        headers.add("Ticker");
        headers.add("Rating");
        headers.add("Ownership disclosure");
        headers.add("PDF file name");
        headers.add("StartPage");
        headers.add("EndPage");
        SheetModel sheetModel = FileUtil.makeSheet(resultPath, "result", headers);
        System.out.println(reports.size());
        List<String[]> data = new ArrayList<>();
        for (AnalysisReport report : reports) {
            try {
                getAnalysisReportDetail2003(report);
            } catch (Exception e) {
                System.out.println(JSON.toJSON(report));
                e.printStackTrace();
                continue;
            }

            String[] line = new String[]{report.getDate(),
                    report.getBrokerName(),
                    JSON.toJSONString(report.getAnalystNames()),
                    report.getCompanyName(),
                    report.getExchange(),
                    report.getTicker(),
                    report.getRating(),
                    JSON.toJSONString(report.getOwnershipDisclosures()),
                    report.getFileName(),
                    report.getStartPage() + "",
                    report.getEndPage() + ""};
            data.add(line);
        }

        FileUtil.exportSheet(sheetModel.getSheet(), data);
        FileUtil.closeSheet(sheetModel);
        System.out.println("Success!");
    }
}
