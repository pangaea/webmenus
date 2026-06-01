package com.genesys.webmenus.orders;

public class OrderStatusUtil {
    /*
    <value text="0 - open" code="0"/>
    <value text="1 - payment_pending" code="1"/>
    <value text="2 - payment_failed" code="2"/>
    <value text="3 - processing" code="3"/>
    <value text="4 - out_for_devilery" code="4"/>
    <value text="5 - read_for_pickup" code="5"/>
    <value text="6 - processed" code="6"/>
    */
    public static String convertStatusToLabel(int status) {
        switch(status) {
            case 0: return "open";
            case 1: return "paymentpending";
            case 2: return "payment_failed";
            case 3: return "inprogress";
            case 4: return "readyforpickup";
            case 5: return "outfordelivery";
            case 6: return "complete";
        }
        return "open";
    }

	public static int convertLabelToStatus(String label) {
        switch(label) {
            case "open": return 0;
            case "paymentpending": return 1;
            case "payment_failed": return 2;
            case "inprogress": return 3;
            case "readyforpickup": return 4;
            case "outfordelivery": return 5;
            case "complete": return 6;
        }
        return 0;
    }
}
