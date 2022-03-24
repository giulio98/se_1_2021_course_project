package it.polito.ezshop;

import java.io.IOException;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.view.EZShopGUI;


public class EZShop {
    public static void main(String[] args) {
        EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
        EZShopGUI gui = new EZShopGUI(ezShop);
    }

}
