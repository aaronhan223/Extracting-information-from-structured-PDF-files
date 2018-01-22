package com.zhixinsou.wuyang;

import java.util.ArrayList;
import java.util.List;

public class AnalysisReport {
    private String filePath;
    private String fileName;
    private int startPage;
    private int endPage;
    private String date;
    private String brokerName;
    private List<String> analystNames;
    private int analystNameCount;
    private String companyName;
    private String ticker = "";
    private String exchange;
    private String ric = "";
    private String rating = "";
    private String initiatingCoverage = "";
    private String priorRecommendation;
    private List<String> ownershipDisclosures;
    private List<String> companiesMentioned;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public List<String> getCompaniesMentioned() {
        return companiesMentioned;
    }

    public void setCompaniesMentioned(List<String> companiesMentioned) {
        this.companiesMentioned = companiesMentioned;
    }

    public void addCompaniesMentioned(String companyMentioned) {
        if(this.companiesMentioned == null)
            this.companiesMentioned = new ArrayList<>();

        this.companiesMentioned.add(companyMentioned);
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAnalystNameCount() {
        return analystNameCount;
    }

    public void setAnalystNameCount(int analystNameCount) {
        this.analystNameCount = analystNameCount;
    }

    public void addAnalystNameCount(int increased) {
        this.analystNameCount += increased;
    }
    
    public List<String> getAnalystNames() {
        return analystNames;
    }

    public void setAnalystNames(List<String> analystNames) {
        this.analystNames = analystNames;
    }

    public void addAnalystName(String analystName) {
        if(analystNames == null)
            analystNames = new ArrayList<>();

        analystNames.add(analystName);
    }
    
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRic() {
        return ric;
    }

    public void setRic(String ric) {
        this.ric = ric;
    }

    public String getInitiatingCoverage() {
        return initiatingCoverage;
    }

    public void setInitiatingCoverage(String initiatingCoverage) {
        this.initiatingCoverage = initiatingCoverage;
    }

    public String getPriorRecommendation() {
        return priorRecommendation;
    }

    public void setPriorRecommendation(String priorRecommendation) {
        this.priorRecommendation = priorRecommendation;
    }

    public List<String> getOwnershipDisclosures() {
        return ownershipDisclosures;
    }

    public void setOwnershipDisclosures(List<String> ownershipDisclosures) {
        this.ownershipDisclosures = ownershipDisclosures;
    }

    public void addOwnershipDisclosure(String ownershipDisclosure) {
        if(ownershipDisclosures == null)
            ownershipDisclosures = new ArrayList<>();

        ownershipDisclosures.add(ownershipDisclosure);
    }
}
