# Quản lý Quán Cà Phê

## Bộ Code Version 1

**Link GitHub**: [https://github.com/akitosuref/coffeemanagement](https://github.com/akitosuref/coffeemanagement)

**Nhóm 9**:

- **Vương Quang Quý** - 23010039
- **Trần Văn Hiệp** - 23010025

---

## 1. Tài khoản đăng nhập

Admin:

- **Username**: admin
- **Password**: 123

Nhân viên:

- **Username**: nhanvien
- **Password**: 123

---

## 2. Môi trường

1. **IDE**: NetBeans 23, JDK 23
2. **Dependencies**:
   - GSON: Quản lý lưu trữ dữ liệu JSON
   - JavaFX: Giao diện đồ họa

---

## 3. Cơ sở dữ liệu

1. **Lưu trữ dưới dạng JSON** bằng thư viện GSON
2. **Các file dữ liệu**:
   - `shop_data.json`: Danh sách bàn và món ăn
   - `users.json`: Danh sách user
   - `order_bill.json`: Danh sách hóa đơn và order

---

## 4. Giao diện

1. **TableView**: Hiển thị danh sách hóa đơn
2. **ComboBox**: Chọn danh mục món ăn
3. **DatePicker**: Chọn ngày cho hóa đơn
4. **FlowPane**: Chọn bàn và chọn món
5. **TableView**: Hiện thị hóa đơn, món ăn

---

## 5. Thực thể

1. **Món ăn**:

   - `int id`: Mã món
   - `String name`: Tên món
   - `double price`: Giá món
   - `int categoryID`: Mã danh mục

2. **Category**

   - `int id`: Mã danh mục
   - `String name`: Tên danh mục

3. **Hóa đơn**:

   - `int id`: Mã hóa đơn
   - `int tableID`: Số bàn
   - `boolean paid`: Đã Thanh Toán
   - `LocalDateTime orderDate`: Ngày đặt
   - `LocalDateTime paidDate`: Ngày thanh toán
   - `double totalPrice`: Tổng tiền

4. **Order**

   - `int id`: Mã order
   - `int billID`: Mã hóa đơn
   - `int foodID`: Mã thức ăn
   - `long count` : Số lượng

5. **OrderItemView**
   - `String foodName`: Tên món
   - `long quantity`: Số lượng
   - `long pricePerItem`: Giá món
   - `long totalPrice`: Tổng tiền
   - `int foodID`: Mã món

6. **Bàn**:
   - `int id`: Mã bàn
   - `String name`: Tên bàn
   - `TableStatus status`: Trạng thái bàn

7. **User**
   - `String name`: Tên user
   - `Integer userId`: id user
   - `String username`: Tên Đăng Nhập
   - `String passwordHash`: Mật khẩu
   - `UserRole role`: Chức vụ

---

## 6. View

1. **LoginView**: Giao diện đăng nhập
2. **MainScreen**: Giao diện quản lý order
3. **AdminScreen**: Giao diện quản lý bàn, món ăn, hóa đơn, danh mục

---

## 7. Chức năng

1. [x] **Thêm/sửa/xóa món ăn**.
2. [ ] **Them sửa/xoa danh mục món ăn**.
3. [ ] **Quản lý users**.
2. [ ] **Thêm/sửa/xóa bàn**.
3. [x] **Đặt món**: Thêm món vào bàn.
4. [x] **Quản lý hóa đơn**: Tạo, xem.
5. [ ] **Tìm kiếm** món ăn theo từ khóa.
6. [x] **Sắp xếp** món ăn theo tên hoặc giá.
7. [x] **Xem chi tiết hóa đơn**: Hiển thị danh sách món của từng hóa đơn.
8. [x] **Thanh toán**: Tính tổng tiền và cập nhật trạng thái bàn.

---

## 8. Xử lý ngoại lệ

1. **Tên món không được để trống**.
2. **Giá món phải lớn hơn 0**.
3. **Số lượng bàn phải là số dương**.
4. **Ngày hóa đơn không được để trống**.
5. **Hóa đơn phải có ít nhất một món**.

---

## 9. Chức năng hiện có

- **Bảo mật tài khoản**: Sử dụng mã hóa AES cho mật khẩu.
- **Hỗ trợ phân quyền**: Thêm vai trò admin và nhân viên.

## 9. Dự kiến phát triển thêm

1. **Thống kê**:

   - Tổng số hóa đơn trong tháng, tổng doanh thu.
   - Thống kê món ăn được gọi nhiều nhất.
   - Vẽ biểu đồ doanh thu theo thời gian.

2. **Xuất hóa đơn**: In hóa đơn.

---


## 10. Quick Deploy

```bash
python .\build_and_copy.py
```
