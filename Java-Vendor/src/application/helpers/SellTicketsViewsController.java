/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.helpers;

import application.controllers.SelectConcertController;
import application.controllers.SellTicketsController;

/**
 *
 * @author Marcel Judth
 */
public class SellTicketsViewsController {
    private static SellTicketsController parentController;
    private static SelectConcertController childController;

    public static SellTicketsController getParentController() {
        return parentController;
    }

    public static void setParentController(SellTicketsController parentController) {
        SellTicketsViewsController.parentController = parentController;
    }

    public static void setChildController(SelectConcertController childController) {
        SellTicketsViewsController.childController = childController;
    }
    
    public static SelectConcertController getChildrenController() {
        return childController;
    }
    
    
}
