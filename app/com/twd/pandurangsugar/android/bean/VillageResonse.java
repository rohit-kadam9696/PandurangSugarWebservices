package com.twd.pandurangsugar.android.bean;

import java.util.List;

public class VillageResonse extends MainResponse{
	 private List<KeyPairBoolData> villList;

	public List<KeyPairBoolData> getVillList() {
		return villList;
	}

	public void setVillList(List<KeyPairBoolData> villList) {
		this.villList = villList;
	}
}
