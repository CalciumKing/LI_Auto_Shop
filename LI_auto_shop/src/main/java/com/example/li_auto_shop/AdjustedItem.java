package com.example.li_auto_shop;
public class AdjustedItem {
    final int trans_ID, adjustment;
    final String item_ID;
    
    public AdjustedItem(int trans_ID, int adjustment, String item_ID) {
        this.trans_ID = trans_ID;
        this.adjustment = adjustment;
        this.item_ID = item_ID;
    }
    
    // region Getters
    public int getTrans_ID() {
        return trans_ID;
    }
    public int getAdjustment() {
        return adjustment;
    }
    public String getItem_ID() {
        return item_ID;
    }
    // endregion
}
