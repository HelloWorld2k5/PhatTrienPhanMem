
// File Main chạy App

import javax.swing.SwingUtilities;

import com.coffeeshop.view.LoginFrame;

public class Main {
    public static void main(String[] args) throws Exception {
        
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));

    }
}
