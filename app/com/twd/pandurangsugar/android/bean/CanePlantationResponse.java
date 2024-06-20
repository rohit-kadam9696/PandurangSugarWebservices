package com.twd.pandurangsugar.android.bean;

import java.util.ArrayList;

public class CanePlantationResponse extends MainResponse{
	
	private ArrayList<String> removeEntry=new ArrayList<>();

public ArrayList<String> getRemoveEntry() {
	return removeEntry;
}

public void setRemoveEntry(ArrayList<String> removeEntry) {
	this.removeEntry = removeEntry;
}
private String successMessage;
public String getSuccessMessage() {
	return successMessage;
}

public void setSuccessMessage(String successMessage) {
	this.successMessage = successMessage;
}


}
