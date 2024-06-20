package com.twd.pandurangsugar.web.bean;

public class Department extends WebServerError{

	private int nsubdeptId;
	private String vsubdeptNameLocal;
	public int getNsubdeptId() {
		return nsubdeptId;
	}
	public void setNsubdeptId(int nsubdeptId) {
		this.nsubdeptId = nsubdeptId;
	}
	public String getVsubdeptNameLocal() {
		return vsubdeptNameLocal;
	}
	public void setVsubdeptNameLocal(String vsubdeptNameLocal) {
		this.vsubdeptNameLocal = vsubdeptNameLocal;
	}
	
}
