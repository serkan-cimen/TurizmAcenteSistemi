package com.turizmAcenteSistemi.Wiew;

import com.turizmAcenteSistemi.Model.Hotel;
import com.turizmAcenteSistemi.Model.User;
import com.turizmAcenteSistemi.helper.Config;
import com.turizmAcenteSistemi.helper.Helper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Array;
import java.sql.SQLException;


public class HotelGUI extends JFrame {
    private JPanel wrapper;
    private JTable tbl_hotel_list;
    private JLabel lbl_welcome;
    private JButton btn_hotel_add;
    private JButton btn_room_add;
    private JButton btn_room_price;
    private JButton btn_back;
    private DefaultTableModel mdl_hotel_list;
    private Object[] row_hotel_list;
    private User user;
    private String hotelName;

    public HotelGUI(User user) {
        this.user = user;
        add(wrapper);
        setSize(1000, 500);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);


        lbl_welcome.setText("Hoş geldiniz " + user.getFirst_name() + " " + user.getLast_name());

        mdl_hotel_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"Otel Adı", "Adres", "E-Posta", "Telefon Numarası", "Pansiyon Tipi", "Şehir", "Bölge", "Yıldız", "Tesis Özellikleri"};
        mdl_hotel_list.setColumnIdentifiers(col_user_list);
        row_hotel_list = new Object[col_user_list.length];

        loadHotelModel();
        tbl_hotel_list.setModel(mdl_hotel_list);
        tbl_hotel_list.getTableHeader().setReorderingAllowed(false);


        btn_hotel_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HotelAddGUI hotelAddGUI = new HotelAddGUI(user);
                dispose();
            }
        });


        btn_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI loginGUI = new LoginGUI();
                dispose();
            }
        });


        tbl_hotel_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_hotel_list.getSelectedRow();
                tbl_hotel_list.setRowSelectionInterval(0, selected_row);
                hotelName = tbl_hotel_list.getValueAt(selected_row, 0).toString();
                System.out.println(hotelName);
            }
        });


        btn_room_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hotelName != null) {
                    RoomAddGUI roomAddGUI = new RoomAddGUI(hotelName);
                } else {
                    Helper.showMsg("Lütfen bir otel seçiniz.");
                }
            }
        });


        btn_room_price.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hotelName != null) {
                    RoomPricingGUI roomPricingGUI = new RoomPricingGUI(hotelName);
                } else {
                    Helper.showMsg("Lütfen bir otel seçiniz.");
                }
            }
        });

    }

    private void loadHotelModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_hotel_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Hotel obj: Hotel.getList()) {
            i = 0;
            row_hotel_list[i++] = obj.getName();
            row_hotel_list[i++] = obj.getAddress();
            row_hotel_list[i++] = obj.getEmail();
            row_hotel_list[i++] = obj.getPhone_num();
            row_hotel_list[i++] = obj.getBoarding_house();
            row_hotel_list[i++] = obj.getCity();
            row_hotel_list[i++] = obj.getRegion();
            row_hotel_list[i++] = obj.getStar();
            row_hotel_list[i++] = parseArray(obj.getService_spec());

            mdl_hotel_list.addRow(row_hotel_list);
        }
    }

    private Object parseArray(Array service_spec) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] tempArr;
        try {
            tempArr = (String[]) service_spec.getArray();
            for (String s: tempArr) {
                stringBuilder.append(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }
}
