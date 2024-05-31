package com.turizmAcenteSistemi.Wiew;

import com.turizmAcenteSistemi.Model.Hotel;
import com.turizmAcenteSistemi.Model.Room;
import com.turizmAcenteSistemi.helper.Config;
import com.turizmAcenteSistemi.helper.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomAddGUI extends JFrame{
    private JPanel wrapper;
    private JComboBox cmbx_type;
    private JTextField fld_bed;
    private JTextField fld_stock;
    private JCheckBox chck_tv;
    private JCheckBox chck_minibar;
    private JCheckBox chck_safe;
    private JLabel lbl_hotel_name;
    private JButton btn_add;
    private Hotel hotel;
    private final String[] roomTypeList = {"Single", "Double", "Suit"};


    public RoomAddGUI(String hotelName) {
        this.hotel = Hotel.getHotelByName(hotelName);
        add(wrapper);
        setSize(750, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        loadRoomTypeCombo();
        lbl_hotel_name.setText(hotelName);


        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String room_type = cmbx_type.getSelectedItem().toString();
                int stock = Integer.parseInt(fld_stock.getText());
                int hotel_id = hotel.getId();
                int bed_num = Integer.parseInt(fld_bed.getText());
                boolean tv = chck_tv.isSelected();
                boolean minibar = chck_minibar.isSelected();
                boolean safe = chck_safe.isSelected();

                if (Room.isRoomTypeAdded(room_type, hotel_id) == 0) {
                    if (Room.add(room_type, stock, hotel_id, bed_num, tv, minibar, safe)) {
                        Helper.showMsg("done");
                    } else {
                        Helper.showMsg("Bu otele ait " + room_type + " tipinde oda daha önce eklenmiştir.");
                    }
                }
            }
        });
    }

    private void loadRoomTypeCombo() {
        cmbx_type.removeAllItems();
        for (String s : roomTypeList) {
            cmbx_type.addItem(s);
        }
    }
}
