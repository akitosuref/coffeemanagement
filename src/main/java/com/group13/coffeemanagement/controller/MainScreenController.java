package com.group13.coffeemanagement.controller;

import com.group13.coffeemanagement.App;
import com.group13.coffeemanagement.database.OrderBillDB;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.group13.coffeemanagement.database.ShopDB;
import com.group13.coffeemanagement.database.UserDB;
import com.group13.coffeemanagement.enums.TableStatus;
import com.group13.coffeemanagement.model.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.time.LocalDateTime;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

public class MainScreenController implements Initializable {

	@FXML
	private FlowPane danhSachBan;

	@FXML
	private FlowPane danhSachMon;

	@FXML
	private Label messageTableLabel;

	private int choseTable;
	private int choseFood;
	private int chosenBill;

	@FXML
	private TableView<OrderItemView> bangHoaDon;

	@FXML
	private TableColumn<OrderItemView, String> foodNameCol;
	@FXML
	private TableColumn<OrderItemView, Long> quantityCol;
	@FXML
	private TableColumn<OrderItemView, Long> pricePerItemCol;
	@FXML
	private TableColumn<OrderItemView, Long> totalPriceCol;

	@FXML
	private ChoiceBox<Category> categoryChoiceBox;

	@FXML
	private Spinner<Integer> quantitySpinner;

	@FXML
	private Button addFoodButton; // Add this line for the button

	private ToggleGroup tableBtnGroup = new ToggleGroup();
	private ToggleGroup foodBtbGroup = new ToggleGroup();

	@FXML
	private TextField totalPrice;

	private List<OrderItemView> orderItems = new ArrayList<>();

	@FXML
	private Button gotoAdminButton;

	@FXML
	private TextField discountField;

	@FXML
	private Button thanhToanButton;

	private List<ImageView> imageViews = new ArrayList<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		thanhToanButton.setDisable(true);

		if (UserDB.isAdmin) {
			gotoAdminButton.setDisable(false);
		} else {
			gotoAdminButton.setDisable(true);
		}

		loadTables();
		loadFoods();
		loadCategories();

		setupOrderTable();
		// populateOrderTable();
		addFoodButton.setOnAction(event -> addFood());

		quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

		categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldCategory, newCategory) -> {
			updateFoodDisplay(newCategory);
		});
	}

	@FXML
	private void gotoAdminScreen() throws IOException {

		App.setRoot("AdminScreen");
	}

	private void loadTables() {
		for (Table table : ShopDB.tables) {
			ToggleButton b = new ToggleButton();
			b.setToggleGroup(tableBtnGroup);
			b.setText(table.getName());
			b.setPrefWidth(80);
			b.setPrefHeight(80);

			b.setUserData(table.getId());

			// On CLick thì sẽ truyền event vào chonTable()
			b.setOnAction(event -> chonTable(event));

			danhSachBan.getChildren().add(b);
		}
		System.out.println("So luong ban: " + danhSachBan.getChildren().size());
	}

	private void loadFoods() {
		for (Food food : ShopDB.foods) {
			VBox vBox = new VBox();

			vBox.setPrefWidth(100);
			vBox.setPrefHeight(100);
			vBox.setAlignment(Pos.CENTER);

			StackPane stackPane = new StackPane();

			ImageView imageView = new ImageView();
			imageView.setFitWidth(100);
			imageView.setFitHeight(100);

			imageViews.add(imageView);

			File imageFile = new File("images/" + food.getImgName());

			Image image = new Image(imageFile.toURI().toString());

			imageView.setImage(image);

			ToggleButton b = new ToggleButton();
			b.setToggleGroup(foodBtbGroup);
			b.setPrefHeight(100);
			b.setPrefWidth(100);
			b.setOpacity(0);
			b.setUserData(food.getId());
			b.setOnAction(event -> chonFood(event, imageView));

			stackPane.getChildren().add(imageView);
			stackPane.getChildren().add(b);

			vBox.getChildren().add(stackPane);

			Label label = new Label(food.getName());
			label.setWrapText(true);
			vBox.getChildren().add(label);

			danhSachMon.getChildren().add(vBox);

		}
		System.out.println("So luong mon: " + danhSachMon.getChildren().size());
	}

	private void loadCategories() {
		categoryChoiceBox.getItems().clear();
		categoryChoiceBox.getItems().addAll(ShopDB.categories);
	}

	private void updateFoodDisplay(Category selectedCategory) {
		// Clear the FlowPane first
		danhSachMon.getChildren().clear();

		// Get the foods based on selected category
		List<Food> foodsToDisplay = selectedCategory == null
				? ShopDB.foods
				: ShopDB.foods.stream()
						.filter(food -> food.getCategoryId() == selectedCategory.getId())
						.collect(Collectors.toList());

		imageViews.clear();

		// Create buttons for each food item and add to FlowPane
		for (Food food : foodsToDisplay) {
			VBox vBox = new VBox();

			vBox.setPrefWidth(100);
			vBox.setPrefHeight(100);
			vBox.setAlignment(Pos.CENTER);

			StackPane stackPane = new StackPane();

			ImageView imageView = new ImageView();
			imageView.setFitWidth(100);
			imageView.setFitHeight(100);

			imageViews.add(imageView);

			File imageFile = new File("images/" + food.getImgName());

			System.out.println(imageFile.toURI().toString());

			Image image = new Image(imageFile.toURI().toString());

			imageView.setImage(image);

			ToggleButton b = new ToggleButton();
			b.setToggleGroup(foodBtbGroup);
			b.setPrefHeight(100);
			b.setPrefWidth(100);
			b.setOpacity(0);
			b.setUserData(food.getId());
			b.setOnAction(event -> chonFood(event, imageView));

			stackPane.getChildren().add(imageView);
			stackPane.getChildren().add(b);

			vBox.getChildren().add(stackPane);

			Label label = new Label(food.getName());
			label.setWrapText(true);
			vBox.getChildren().add(label);

			danhSachMon.getChildren().add(vBox);
		}
		System.out.println("So luong mon: " + danhSachMon.getChildren());
	}

	@FXML
	public void chonFood(javafx.event.ActionEvent event, ImageView imageView) {
		// Lấy button được clicked
		ToggleButton clickedButton = (ToggleButton) event.getSource();

		for (ImageView imgV : imageViews) {
			imgV.setOpacity(1);
		}

		imageView.setOpacity(0.5);

		// Lấy data (food id) từ button được click
		int foodID = (int) clickedButton.getUserData();

		choseFood = foodID;
		System.out.println("Selected Food ID: " + choseFood);
	}

	@FXML
	public void chonTable(javafx.event.ActionEvent event) {
		for (Toggle btn : foodBtbGroup.getToggles()) {
			btn.setSelected(false);
		}

		choseFood = 0;

		// Lấy button được clicked
		ToggleButton clickedButton = (ToggleButton) event.getSource();

		// Lấy data (table id) từ button được click
		int tableId = (int) clickedButton.getUserData();

		choseTable = tableId;
		chosenBill = getBillIdByTableId(choseTable);
		System.out.println("Selected Table ID: " + choseTable);

		updateOrderTable(choseTable);

	}

	private int getBillIdByTableId(int tableId) {
		// Check ban co nguoi hay khong
		for (Table table : ShopDB.tables) {
			if (table.getId() == tableId) {
				if (table.getStatus() == TableStatus.USED) {
					messageTableLabel.setText("Bàn " + tableId + " có khách!");
				} else {
					messageTableLabel.setText("Bàn đang trống");
				}
			}
		}
		// Check co hoa don hay khong
		for (Bill bill : OrderBillDB.bills) {
			if (bill.getTableID() == tableId && !bill.isPaid()) {
				return bill.getId();
			}
		}

		return 0;
	}

	private double updateOrderTable(int tableId) {
		for (Bill bill : OrderBillDB.bills) {
			if (bill.getTableID() == tableId && !bill.isPaid()) {
				// Get the order items related to this bill ID
				orderItems = getOrderItemsForBill(bill.getId());
				ObservableList<OrderItemView> observableList = FXCollections.observableArrayList(orderItems);

				// Update the TableView with the filtered order items
				bangHoaDon.setItems(observableList);

				bangHoaDon.refresh();

				// Break the loop since we've found the matching bill
				break;
			} else {
				bangHoaDon.getItems().clear();
			}
		}

		double totalPriceLocal = 0;

		for (OrderItemView orderItem : orderItems) {
			totalPriceLocal += orderItem.getTotalPrice();
		}

		totalPrice.setText(convertToVND(totalPriceLocal));

		return totalPriceLocal;
	}

	private List<OrderItemView> getOrderItemsForBill(int billId) {
		List<OrderItemView> orderItemViews = new ArrayList<>();

		// Iterate over orders to find those matching the bill ID
		for (Order order : OrderBillDB.orders) {
			if (order.getBillID() == billId) { // Filter by bill ID
				Food foundFood = null;

				// Find the corresponding food item
				for (Food food : ShopDB.foods) {
					if (food.getId() == order.getFoodID()) {
						foundFood = food;
						break; // Exit the loop once the food is found
					}
				}

				// Create an OrderItemView object if the food exists
				if (foundFood != null) {
					OrderItemView itemView = new OrderItemView(foundFood.getName(), order.getCount(),
							foundFood.getPrice(), foundFood.getId());
					orderItemViews.add(itemView);
				}
			}
		}

		return orderItemViews; // Return the list of OrderItemView objects
	}

	// Setup TableView columns
	private void setupOrderTable() {
		foodNameCol.setCellValueFactory(new PropertyValueFactory<>("foodName"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		pricePerItemCol.setCellValueFactory(new PropertyValueFactory<>("pricePerItem"));
		totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

		quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.LongStringConverter()));

		quantityCol.setOnEditCommit(event -> {
			OrderItemView item = event.getRowValue(); // Get the current item
			long newQuantity = event.getNewValue(); // Get the new quantity

			item.setQuantity(newQuantity); // Update the quantity in the model

			// Optionally, you can update the total price here
			long pricePerItem = item.getPricePerItem();
			item.setTotalPrice(pricePerItem * newQuantity);

			OrderBillDB.orders.stream()
					.filter(orderInfo -> orderInfo.getBillID() == chosenBill
							&& orderInfo.getFoodID() == item.getFoodID()) // Use chosenBillID to identify the bill
					.findFirst()
					.ifPresent(orderInfo -> {
						orderInfo.setCount(newQuantity); // Update quantity in OrderInfo
					});

			bangHoaDon.refresh(); // Refresh the TableView to show updated values
		});
	}

	// Tao list hoa don view tu Orders va Foods
	// private List<OrderItemView> getOrderItems() {
	// List<OrderItemView> orderItemViews = new ArrayList<>();

	// for (Order order : OrderBillDB.orders) {
	// Food foundFood = null;

	// // Find the food by matching the ID in the foods list
	// for (Food food : ShopDB.foods) {
	// if (food.getId() == order.getFoodID()) {
	// foundFood = food;
	// break; // Exit the loop once the food is found
	// }
	// }

	// // If food is found, create and add OrderItemView to the list
	// if (foundFood != null) {
	// OrderItemView itemView = new OrderItemView(foundFood.getName(),
	// order.getCount(), foundFood.getPrice(),
	// foundFood.getId());
	// orderItemViews.add(itemView);
	// }
	// }

	// return orderItemViews; // Return the list of order item views
	// }

	@FXML
	public void addFood() {
		Integer quantity = quantitySpinner.getValue();

		// Check da chon table va chon food
		if (choseTable == 0) {
			System.out.println("Please select a table.");
			return;
		}
		if (choseFood == 0) {
			System.out.println("Please select a food item.");
			return;
		}

		chosenBill = getBillIdByTableId(choseTable);

		thanhToanButton.setDisable(false);

		// Tao hoa don moi neu ban khong co khach
		if (chosenBill == 0) {
			Bill newBill = new Bill();
			newBill.setTableID(choseTable);
			newBill.setDisCount(0);
			newBill.setTotalPrice(0);
			OrderBillDB.bills.add(newBill);

			chosenBill = newBill.getId();

			for (Table table : ShopDB.tables) {
				if (table.getId() == choseTable) {
					table.setStatus(TableStatus.USED);
					messageTableLabel.setText("Bàn " + choseTable + " có khách!");
					break;
				}
			}

			System.out.println("New Bill: ID = " + newBill.getId());
		}

		// Create a new Order entry
		Order newOrder = new Order();
		newOrder.setBillID(chosenBill);
		newOrder.setFoodID(choseFood);
		newOrder.setCount(quantity);

		// Add to order info list and update the display
		OrderBillDB.orders.add(newOrder);

		double totalPriceLocal = 0;

		totalPriceLocal = updateOrderTable(choseTable); // Refresh the table view

		totalPrice.setText(convertToVND(totalPriceLocal));

		quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

		System.out
				.println("Added food ID: " + choseFood + " with quantity: " + quantity + " to table ID: " + choseTable
						+ " with bill ID: " + chosenBill);

		updateOrderTable(choseTable);

	}

	@FXML
	public void thanhToan() {
		for (Table table : ShopDB.tables) {
			if (table.getId() == choseTable) {
				table.setStatus(TableStatus.NO_USE);

				for (Bill bill : OrderBillDB.bills) {
					if (bill.getId() == chosenBill) {

						double totalPriceLocal = 0;

						if (bill.getDisCount() > 0) {
							totalPriceLocal = bill.getTotalPrice();
						} else {
							for (OrderItemView orderItem : orderItems) {
								totalPriceLocal += orderItem.getTotalPrice();
							}
						}

						bill.setPaid(true);
						bill.setPaidDate(LocalDateTime.now());
						bill.setTotalPrice(totalPriceLocal);
					}
				}

				discountField.setText("0");
				totalPrice.setText("0");

				bangHoaDon.getItems().clear();

				thanhToanButton.setDisable(true);

				ShopDB.saveShopDB();
				OrderBillDB.saveBill();
			}
		}
	}

	@FXML
	public void apKhuyenMai() {
		for (Bill bill : OrderBillDB.bills) {
			if (bill.getId() == chosenBill) {

				int discount = Integer.parseInt(discountField.getText());

				bill.setDisCount(discount);

				double totalPriceLocal = 0;

				for (OrderItemView orderItem : orderItems) {
					totalPriceLocal += orderItem.getTotalPrice();
				}

				double newTotalPrice = totalPriceLocal - (totalPriceLocal * (discount / 100.0));

				bill.setTotalPrice(newTotalPrice);

				totalPrice.setText(convertToVND(newTotalPrice));
			}
		}
	}

	private String convertToVND(double money) {
		// Create a NumberFormat instance for VND
		Locale vietnamLocale = Locale.forLanguageTag("vi-VN");

		// Create a NumberFormat instance for VND
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vietnamLocale);

		// Display the formatted currency
		System.out.println("Formatted amount: " + vndFormat.format(money));
		return vndFormat.format(money);
	}

	@FXML
	private void gotoLoginScreen() throws IOException {
		UserDB.isAdmin = false;
		App.setRoot("login");
	}

}
