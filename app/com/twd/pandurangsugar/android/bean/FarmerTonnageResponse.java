package com.twd.pandurangsugar.android.bean;

import java.util.List;

public class FarmerTonnageResponse extends MainResponse {
    private String farmerName;

    List<FarmerTonnage> farmerTonnages;

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public List<FarmerTonnage> getFarmerTonnages() {
        return farmerTonnages;
    }

    public void setFarmerTonnages(List<FarmerTonnage> farmerTonnages) {
        this.farmerTonnages = farmerTonnages;
    }
}
