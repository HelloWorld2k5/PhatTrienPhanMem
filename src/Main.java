import com.coffeeshop.view.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
// Import các class quản lý cấu hình của bạn ở đây
// import com.coffeeshop.util.DatabaseConnection; 

public class Main {
    public static void main(String[] args) {
        // 1. Thiết lập giao diện đẹp hơn (Look and Feel của hệ điều hành)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Chạy giao diện
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}