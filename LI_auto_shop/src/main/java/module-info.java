module com.example.li_auto_shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    
    opens com.example.li_auto_shop to javafx.fxml;
    exports com.example.li_auto_shop;
}