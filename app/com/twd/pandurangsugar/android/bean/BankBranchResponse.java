package com.twd.pandurangsugar.android.bean;

import java.util.List;

public class BankBranchResponse extends MainResponse{

	private List<BankBranchMaster> bankBranchList;

	public List<BankBranchMaster> getBankBranchList() {
		return bankBranchList;
	}

	public void setBankBranchList(List<BankBranchMaster> bankBranchList) {
		this.bankBranchList = bankBranchList;
	}
	
}