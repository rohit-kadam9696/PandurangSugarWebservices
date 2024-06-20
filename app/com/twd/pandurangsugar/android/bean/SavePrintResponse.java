package com.twd.pandurangsugar.android.bean;

public class SavePrintResponse extends ActionResponse {

    String htmlContent;
    private String printHead;

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

	public String getPrintHead() {
		return printHead;
	}

	public void setPrintHead(String printHead) {
		this.printHead = printHead;
	}
}