* DỰ ÁN NÀY DÙNG JDK 21 VÀ DÙNG VSCODE ĐỂ LÀM (YÊU CẦU TẢI JDK21 RỒI ADD ENVIRONMENT VÀ TẢI EXTENSION JAVA TRÊN VSCODE)

* Thư viện ngoài:
    - mysql-connector 9.4.0 (Vào Referenced Library trong dự án để add file .jar vào)

* Cấu trúc thư mục:

coffee-shop-management/
│
├── .vscode/
│   └── launch.json
├── lib/
│   └── (thư viện JDBC / file export / dependency ngoài)
├── src/
│   └── com/
│       └── coffeeshop/
│           ├── Main.java
│           │
│           ├── config/
│           │   ├── AppConfig.java
│           │   └── DatabaseConnection.java
│           │   model có thêm User.java, CurrentUser.java
│           ├── model/   
│           │   ├── MenuItem.java
│           │   ├── CoffeeItem.java
│           │   ├── BakeryItem.java
│           │   ├── CoffeeCombo.java
│           │   ├── OrderItem.java
│           │   ├── Invoice.java
│           │   └── OrderState.java
│           │   view có thêm LoginFrame.java, RegisterFrame.java
│           ├── view/
│           │   ├── MainFrame.java
│           │   ├── SidebarPanel.java
│           │   ├── MenuPanel.java
│           │   ├── CartPanel.java
│           │   ├── CheckoutDialog.java
│           │   └── DrinkOptionDialog.java
│           │   controller có thêm AuthController.java
│           ├── controller/
│           │   ├── MenuController.java
│           │   ├── CartController.java
│           │   ├── CheckoutController.java
│           │   └── AuthController.java
│           │   service có thêm AuthService.java
│           ├── service/
│           │   ├── OrderService.java
│           │   ├── InvoiceService.java
│           │   ├── PaymentService.java
│           │   └── CloneOrderService.java
│           │   repository có thêm AuthRepository.java
│           ├── repository/
│           │   ├── MenuRepository.java
│           │   ├── OrderRepository.java
│           │   ├── InvoiceRepository.java
│           │   └── UserRepository.java
│           │
│           ├── factory/
│           │   ├── CoffeeFactory.java
│           │   ├── UIThemeFactory.java
│           │   ├── AdminUIFactory.java
│           │   └── StaffUIFactory.java
│           │
│           ├── decorator/
│           │   ├── CoffeeDecorator.java
│           │   ├── ExtraShotDecorator.java
│           │   ├── AlmondMilkDecorator.java
│           │   └── CaramelSyrupDecorator.java
│           │
│           ├── command/
│           │   ├── Command.java
│           │   ├── AddOrderCommand.java
│           │   ├── RemoveItemCommand.java
│           │   ├── UpdateQuantityCommand.java
│           │   ├── UndoCommand.java
│           │   └── RedoCommand.java
│           │
│           ├── memento/
│           │   ├── OrderMemento.java
│           │   ├── OrderCaretaker.java
│           │   └── OrderState.java
│           │
│           ├── observer/
│           │   ├── Subject.java
│           │   ├── Observer.java
│           │   ├── CartSubject.java
│           │   ├── CartTotalLabelObserver.java
│           │   └── CartTableObserver.java
│           │
│           ├── adapter/
│           │   ├── InvoicePrinter.java
│           │   ├── FileInvoiceAdapter.java
│           │   └── ExcelInvoiceAdapter.java
│           │
│           └── util/
│               ├── MoneyFormatter.java
│               ├── DateTimeUtil.java
│               ├── ValidationUtil.java
│               └── Constants.java
│
└── README.md

* Giải thích từng nhiệm vụ của từng thư mục:

- config/
Chứa cấu hình hệ thống và kết nối DB, ví dụ AppConfig, DatabaseConnection. Đây là phần khởi tạo dùng chung cho toàn app. Trong báo cáo, Singleton được gán cho AppConfig và lớp quản lý kết nối cơ sở dữ liệu.

- model/
Chứa dữ liệu lõi và trạng thái nghiệp vụ: MenuItem, CoffeeItem, BakeryItem, CoffeeCombo, OrderItem, Invoice, OrderState... Đây là phần “Model” đúng nghĩa trong MVC.

- view/
Chứa toàn bộ giao diện Swing: MainFrame, MenuPanel, CartPanel, CheckoutDialog, LoginFrame, AdminDashboard, StaffDashboard, các dialog chọn size/topping. View chỉ hiển thị và gửi sự kiện lên controller, không tự xử lý nghiệp vụ nặng.

- controller/
Nhận sự kiện từ view và điều phối model/service. Ví dụ: MenuController, CartController, CheckoutController, AuthController. Nút bấm trong Swing sẽ gọi controller, thay vì nhét logic trực tiếp vào ActionListener trong MainFrame.

- service/
Chứa nghiệp vụ chính: tính tiền, áp khuyến mãi, xử lý đơn hàng, xác nhận thanh toán, clone đơn, tạo hóa đơn. Nếu muốn code sạch hơn, mọi thứ như “thêm món”, “xóa món”, “thanh toán” nên đi qua service trước khi đụng database.

- repository/
Chỉ lo truy xuất dữ liệu: MenuItemRepository, OrderRepository, InvoiceRepository, UserRepository. Layer này tách riêng SQL khỏi controller/service để dễ bảo trì.

- factory/
Giữ các pattern tạo đối tượng: CoffeeFactory, UIThemeFactory, AdminUIFactory, StaffUIFactory. Trong báo cáo, nhóm đã dùng Factory Method và Abstract Factory cho cả menu lẫn giao diện.

- decorator/
Giữ logic tùy biến đồ uống: thêm size, topping, extra shot, caramel... Đây đúng với phần báo cáo nói về Decorator cho popup chọn size/topping.

- command/
Chứa các lệnh thao tác giỏ hàng: AddCoffeeCommand, RemoveCoffeeCommand, UpdateQuantityCommand, ApplyDiscountCommand, UndoCommand, RedoCommand. Báo cáo của nhóm đã gắn Command với undo/redo cho giỏ hàng.

- memento/
Lưu snapshot giỏ hàng trước khi thay đổi lớn, để hoàn tác an toàn. Nhóm đã mô tả Memento phối hợp với Command để phục hồi trạng thái cũ của cart.

- observer/
Phát sự kiện khi giỏ hàng thay đổi để tự cập nhật JTable, JLabel tổng tiền, hoặc màn hình phụ. Đây là đúng mô tả Observer trong báo cáo.

- adapter/
Chuyển đổi dữ liệu hóa đơn sang file .txt, .xlsx, hoặc định dạng khác. Báo cáo cũng đã nêu InvoiceAdapter dùng để xuất hóa đơn ra file vật lý.

- util/
Chứa tiện ích chung: format tiền, validate input, hằng số, helper cho date/time, export name, icon loader.

===> Cách hiểu ngắn gọn là thế này:
view chỉ vẽ giao diện, controller nhận click, model giữ dữ liệu, service xử lý nghiệp vụ, repository nói chuyện với DB. Các pattern như Factory, Decorator, Command, Observer, Memento, Adapter, Singleton vẫn dùng bình thường, nhưng được đặt vào đúng chỗ hơn.