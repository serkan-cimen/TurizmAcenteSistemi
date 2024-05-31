package com.turizmAcenteSistemi.Wiew;

import com.turizmAcenteSistemi.Model.Hotel;
import com.turizmAcenteSistemi.Model.User;
import com.turizmAcenteSistemi.helper.Config;
import com.turizmAcenteSistemi.helper.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class HotelAddGUI extends JFrame{
    private JPanel wrapper;
    private JButton btn_logout;
    private JTextField fld_name;
    private JComboBox cmb_star;
    private JTextField fld_city;
    private JTextField fld_region;
    private JTextArea fld_address;
    private JTextField fld_email;
    private JTextField fld_phone;
    private JPanel jpl_welcome;
    private JComboBox cmb_boardingHouse;
    private JCheckBox chckBx_freePark;
    private JCheckBox chckBx_freeWifi;
    private JCheckBox chckBx_pool;
    private JCheckBox chckBx_fitness;
    private JCheckBox chckBx_hotelConcierge;
    private JCheckBox chckBx_spa;
    private JCheckBox chckBx_roomService;
    private JButton btn_add;
    private JLabel lbl_welcome;
    private Hotel hotel;
    private final ArrayList<String> serviceSpecList = new ArrayList<String>();

    private String message;

    public HotelAddGUI(User user) {
        add(wrapper);
        setSize(700, 600);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
        fld_address.setWrapStyleWord(true);
        fld_address.setLineWrap(true);
        loadComboStar();
        loadComboPension();
        message = "Hoş Geldiniz, " + user.getFirst_name() + " " + user.getLast_name();
        lbl_welcome.setText(message);


        chckBx_freePark.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_freePark.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_freePark.getText());
                }
            }
        });


        chckBx_freeWifi.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_freeWifi.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_freeWifi.getText());
                }
            }
        });


        chckBx_pool.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_pool.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_pool.getText());
                }
            }
        });


        chckBx_hotelConcierge.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_hotelConcierge.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_hotelConcierge.getText());
                }
            }
        });


        chckBx_spa.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_spa.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_spa.getText());
                }
            }
        });


        chckBx_fitness.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_fitness.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_fitness.getText());
                }
            }
        });


        chckBx_roomService.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    serviceSpecList.add(chckBx_roomService.getText());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    serviceSpecList.remove(chckBx_roomService.getText());
                }
            }
        });


        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = fld_name.getText();
                String star = cmb_star.getSelectedItem().toString();
                String address = fld_address.getText();
                String city = fld_city.getText();
                String region = fld_region.getText();
                String email = fld_email.getText();
                String phone = fld_phone.getText();

                String boardingHouse = cmb_boardingHouse.getSelectedItem().toString();
                System.out.println(serviceSpecList);

                if (Helper.isFieldEmpty(fld_name) || Helper.isFieldEmpty(fld_email) || Helper.isFieldEmpty(fld_city) || Helper.isFieldEmpty(fld_phone) || Helper.isFieldEmpty(fld_region)) {
                    Helper.showMsg("fill");
                } else {
                    if (Hotel.add(name, address, city,region, email, phone, star, boardingHouse,serviceSpecList)) {
                        //String name, String address, String city, String region, String email, String phone_num, String star, String boarding_house, ArrayList<String> serviceSpec
                        Helper.showMsg("Otel başarıyla eklendi.");
                    } else {
                        Helper.showMsg("error");
                    }
                }
            }
        });


        btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HotelGUI hotelGUI = new HotelGUI(user);
                dispose();
            }
        });
    }

    private void loadComboStar() {
        cmb_star.removeAllItems();
        for (String s: Config.STAR_LIST) {
            cmb_star.addItem(s);
        }
    }

    private void loadComboPension() {
        cmb_boardingHouse.removeAllItems();
        for (String s: Config.PENSION_TYPES) {
            cmb_boardingHouse.addItem(s);
        }
    }
}
