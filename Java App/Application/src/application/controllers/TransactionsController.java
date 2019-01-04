/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.DoubleTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.IntegerTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Valon
 */
public class TransactionsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static final String COMPUTER_DEPARTMENT = "Computer Department";
    private static final String SALES_DEPARTMENT = "Sales Department";
    private static final String IT_DEPARTMENT = "IT Department";
    private static final String HR_DEPARTMENT = "HR Department";

//    @FXML
//    private TreeTableColumn<Transaction, Integer> ttc_id;
//    @FXML
//    private TreeTableColumn<Transaction, String> ttc_amount;
//    @FXML
//    private TreeTableColumn<Transaction, String> ttc_date;
//    @FXML
//    private TreeTableColumn<Transaction, String> ttc_payerWallet;
//    @FXML
//    private TreeTableColumn<Transaction, String> ttc_receiverWallet;
//    @FXML
//    private TreeTableColumn<Transaction, String> ttc_ticket;
    @FXML
    private Pane content;

//    
//    private static final class Transaction extends RecursiveTreeObject<Transaction> {
//        final IntegerProperty id;
//        final DoubleProperty amount;
//        final StringProperty date;
//        final IntegerProperty payer_Wallet;
//        final IntegerProperty receiver_Wallet;
//        final IntegerProperty ticket;
//
//        public Transaction(int id, double amount, String date, int payer_Wallet, int receiver_Wallet, int ticket) {
//            this.id = new SimpleIntegerProperty(id);
//            this.amount = new SimpleDoubleProperty(amount);
//            this.date = new SimpleStringProperty(date);
//            this.payer_Wallet = new SimpleIntegerProperty(payer_Wallet);
//            this.receiver_Wallet = new SimpleIntegerProperty(receiver_Wallet);
//            this.ticket = new SimpleIntegerProperty(ticket);
//        }        
//    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
    }

    private static final String PREFIX = "( ";
    private static final String POSTFIX = " )";

    // editable table view
    @FXML
    private JFXTreeTableView<Transaction> ttv;
    private JFXTreeTableColumn<Transaction, Integer> ttc_id;
    private JFXTreeTableColumn<Transaction, Double> ttc_amount;
    private JFXTreeTableColumn<Transaction, String> ttc_date;
    private JFXTreeTableColumn<Transaction, Integer> ttc_payer;
    private JFXTreeTableColumn<Transaction, Integer> ttc_receiver;
    private JFXTreeTableColumn<Transaction, Integer> ttc_ticket;

    private Label treeTableViewCount;
    @FXML
    private JFXButton treeTableViewAdd;
    @FXML
    private JFXButton treeTableViewRemove;
    @FXML
    private Label editableTreeTableViewCount;
    @FXML
    private JFXTextField searchField2;

    static final class Transaction extends RecursiveTreeObject<Transaction> {

        final IntegerProperty id;
        final DoubleProperty amount;
        final StringProperty date;
        final IntegerProperty payer_Wallet;
        final IntegerProperty receiver_Wallet;
        final IntegerProperty ticket;

        public Transaction(int id, double amount, String date, int payer_Wallet, int receiver_Wallet, int ticket) {
            this.id = new SimpleIntegerProperty(id);
            this.amount = new SimpleDoubleProperty(amount);
            this.date = new SimpleStringProperty(date);
            this.payer_Wallet = new SimpleIntegerProperty(payer_Wallet);
            this.receiver_Wallet = new SimpleIntegerProperty(receiver_Wallet);
            this.ticket = new SimpleIntegerProperty(ticket);
        }

        public IntegerProperty getId() {
            return id;
        }

        public StringProperty getDate() {
            return date;
        }

        public IntegerProperty getPayer_Wallet() {
            return payer_Wallet;
        }

        @Override
        public String toString() {
            return "Transaction{" + "id=" + id + ", amount=" + amount + ", date=" + date + ", payer_Wallet=" + payer_Wallet + ", receiver_Wallet=" + receiver_Wallet + ", ticket=" + ticket + '}';
        }
        
        
    }

    private final Random random = new SecureRandom();

    /**
     * init fxml when loaded.
     */
    public void init() {
        ttc_id = new JFXTreeTableColumn<>("ID");
        ttc_amount = new JFXTreeTableColumn<>("Amount");
        ttc_date = new JFXTreeTableColumn<>("Date");
        ttc_payer = new JFXTreeTableColumn<>("Payer");
        ttc_receiver = new JFXTreeTableColumn<>("Receiver");
        ttc_ticket = new JFXTreeTableColumn<>("Ticket");
        
        ttc_id.setPrefWidth(166);
        ttc_amount.setPrefWidth(166);
        ttc_date.setPrefWidth(166);
        ttc_payer.setPrefWidth(166);
        ttc_receiver.setPrefWidth(166);
        ttc_ticket.setPrefWidth(166);
        

        setupEditableTableView();
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Transaction, T> column, Function<Transaction, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Transaction, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupEditableTableView() {
        setupCellValueFactory(ttc_id, p -> p.id.asObject());
        setupCellValueFactory(ttc_amount, p -> p.amount.asObject());
        setupCellValueFactory(ttc_date, Transaction::getDate);
        setupCellValueFactory(ttc_payer, p -> p.payer_Wallet.asObject());
        setupCellValueFactory(ttc_receiver, p -> p.receiver_Wallet.asObject());
        setupCellValueFactory(ttc_ticket, p -> p.ticket.asObject());

//        // add editors
//        ttc_id.setCellFactory((TreeTableColumn<Transaction, Integer> param) -> {
//            return new GenericEditableTreeTableCell<>(
//                    new IntegerTextFieldEditorBuilder());
//        });
//        ttc_amount.setCellFactory((TreeTableColumn<Transaction, Double> param) -> {
//            return new GenericEditableTreeTableCell<>(
//                    new DoubleTextFieldEditorBuilder());
//        });

//        ttc_date.setOnEditCommit((CellEditEvent<Transaction, String> t) -> {
//            t.getTreeTableView()
//                    .getTreeItem(t.getTreeTablePosition()
//                            .getRow())
//                    .getValue().date.set(t.getNewValue());
//        });
//
//        ttc_payer.setCellFactory((TreeTableColumn<Transaction, Integer> param) -> {
//            return new GenericEditableTreeTableCell<>(
//                    new IntegerTextFieldEditorBuilder());
//        });
//        ttc_receiver.setCellFactory((TreeTableColumn<Transaction, Integer> param) -> {
//            return new GenericEditableTreeTableCell<>(
//                    new IntegerTextFieldEditorBuilder());
//        });
//        ttc_ticket.setCellFactory((TreeTableColumn<Transaction, Integer> param) -> {
//            return new GenericEditableTreeTableCell<>(
//                    new IntegerTextFieldEditorBuilder());
//        });
        //final TreeItem<Transaction> root = new RecursiveTreeItem<>(users, RecursiveTreeObject::getChildren);
        ObservableList<Transaction> data = generateDummyData(100);
        System.out.println(data.get(45));
        final TreeItem<Transaction> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        ttv.getColumns().setAll(ttc_id, ttc_amount, ttc_payer, ttc_receiver, ttc_ticket);
        ttv.setRoot(root);
        ttv.setShowRoot(false);
        ttv.setEditable(true);
        
        editableTreeTableViewCount.textProperty().bind(Bindings.createStringBinding(() -> PREFIX + ttv.getCurrentItemsCount() + POSTFIX,ttv.currentItemsCountProperty()));
        //searchField2.textProperty().addListener(setupSearchField(editableTreeTableView));
    }

//    private ChangeListener<String> setupSearchField(final JFXTreeTableView<TransactionsController.Transaction> tableView) {
//        return (o, oldVal, newVal) ->
//            tableView.setPredicate(personProp -> {
//                final Transaction t = personProp.getValue();
//                return t.firstName.get().contains(newVal)
//                    || t.lastName.get().contains(newVal)
//                    || Integer.toString(t.age.get()).contains(newVal);
//            });
//        return null;
//    }
    private ObservableList<Transaction> generateDummyData(final int numberOfEntries) {
        final ObservableList<Transaction> dummyData = FXCollections.observableArrayList();
        for (int i = 0; i < numberOfEntries; i++) {
            dummyData.add(createNewRandomPerson());
        }
        return dummyData;
    }

    private Transaction createNewRandomPerson() {
        return new Transaction(random.nextInt(),random.nextDouble(), "2242",random.nextInt() , random.nextInt(), random.nextInt());

    }

    /*
     * data class
     */
}
