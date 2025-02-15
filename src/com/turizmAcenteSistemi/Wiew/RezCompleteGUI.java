package com.turizmAcenteSistemi.Wiew;

import com.turizmAcenteSistemi.Model.User;
import com.turizmAcenteSistemi.helper.Config;
import com.turizmAcenteSistemi.helper.DBConnector;
import com.turizmAcenteSistemi.helper.Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RezCompleteGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_info_name;
    private JTextField fld_info_phone;
    private JTextField fld_rez_note;
    private JTextField fld_info_mail;
    private JButton btn_save;
    private ArrayList<JLabel> guestNumLabelList = new ArrayList<>();
    private ArrayList<JTextField> nameTextFieldList = new ArrayList<>();
    private ArrayList<JTextField> nationTextFieldList = new ArrayList<>();
    private ArrayList<JTextField> TCTextFieldList = new ArrayList<>();
    private ArrayList<JPanel> panelList = new ArrayList<>();
    private JLabel lbl_guestNum;
    private JTextField fld_name;
    private JTextField fld_nation;
    private JTextField fld_tc;
    private JPanel pnl_wrapper;

    public RezCompleteGUI(User user, int hotel_id, int room_id, String room_type, int numAdult, int numChild, Date checkIn, Date checkOut) {
        System.out.println(numChild);
        int numGuest = numAdult + numChild;
        add(wrapper);
        setSize(1000, 600);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        GridLayout myLayout = new GridLayout(5, 2);
        setLayout(new FlowLayout());


        fld_info_mail.setPreferredSize(new Dimension(250, 25));
        fld_info_name.setPreferredSize(new Dimension(250, 25));
        fld_info_phone.setPreferredSize(new Dimension(250, 25));
        fld_rez_note.setPreferredSize(new Dimension(250, 25));

        for (int i = 0; i < numGuest; i++) {
            guestNumLabelList.add(new JLabel());
            nameTextFieldList.add(new JTextField("Adı Soyadı"));
            nationTextFieldList.add(new JTextField("Ülke"));
            TCTextFieldList.add(new JTextField("Kimlik/Pasaport No"));
            panelList.add(new JPanel());
        }

        for (int i = 0; i < numGuest; i++) {
            lbl_guestNum = guestNumLabelList.get(i);
            fld_name = nameTextFieldList.get(i);
            fld_nation = nationTextFieldList.get(i);
            fld_tc = TCTextFieldList.get(i);
            pnl_wrapper = panelList.get(i);
            lbl_guestNum.setText(i + 1 + " .Misafir");
            fld_name.setPreferredSize(new Dimension(250, 25));
            fld_nation.setPreferredSize(new Dimension(250, 25));
            fld_tc.setPreferredSize(new Dimension(250, 25));
            pnl_wrapper.add(lbl_guestNum);pnl_wrapper.add(fld_name);pnl_wrapper.add(fld_nation);pnl_wrapper.add(fld_tc);
            add(pnl_wrapper);
        }
        setVisible(true);


        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean status = false;
                String contactName = fld_info_name.getText();
                String contactRezNote = fld_rez_note.getText();
                String contactPhone = fld_info_phone.getText();
                String contactEmail = fld_info_mail.getText();
                String guestName = null, guestNation = null, guestTC = null;
                for (int i = 0; i < numGuest; i++) {
                    guestName = nameTextFieldList.get(i).getText();
                    guestNation = nationTextFieldList.get(i).getText();
                    guestTC = TCTextFieldList.get(i).getText();
                    status = addRez(contactName, contactRezNote, contactPhone, contactEmail, guestName, guestNation, guestTC, hotel_id, room_id, checkIn, checkOut);
                }
                if (Boolean.TRUE.equals(status)) {
                    updateStock(room_id);
                    Helper.showMsg("Rezervasyon tamamlandı.");
                    dispose();
                    RezListGUI rezListGUI = new RezListGUI(user);
                } else {
                    Helper.showMsg("Bir hata oluştu!");
                }
            }
        });
    }

    private Boolean addRez(String contactName, String contactRezNote, String contactPhone, String contactEmail, String guestName, String guestNation, String guestTC, int hotelId, int roomId, Date checkIn, Date checkOut) {
        String query = "INSERT INTO rez " + "(contact_name, contact_phone, contact_email,rez_note,\" +\n" +
                "                \"guest_name,guest_nation,guest_identity,hotel_id,room_id,check_in,check_out)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, contactName);
            pr.setString(2, contactPhone);
            pr.setString(3, contactEmail);
            pr.setString(4, contactRezNote);
            pr.setString(5, guestName);
            pr.setString(6, guestNation);
            pr.setString(7, guestTC);
            pr.setInt(8, hotelId);
            pr.setInt(9, roomId);
            pr.setDate(10, checkIn);
            pr.setDate(11, checkOut);

            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getStockSizeOfRoom(int room_id) {
        int stock = -1;
        String query = "SELECT stock FROM room WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, room_id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stock;
    }

    public boolean updateStock(int room_id) {
        int stock = getStockSizeOfRoom(room_id);
        if (stock == -1) {
            return false;
        }
        int newStock = stock - 1;
        String query = "UPDATE room SET stock = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, newStock);
            pr.setInt(2, room_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
