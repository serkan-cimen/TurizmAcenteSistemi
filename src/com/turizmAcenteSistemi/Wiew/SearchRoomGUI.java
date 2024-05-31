package com.turizmAcenteSistemi.Wiew;

import com.turizmAcenteSistemi.Model.Hotel;
import com.turizmAcenteSistemi.Model.User;
import com.turizmAcenteSistemi.helper.Config;
import com.turizmAcenteSistemi.helper.DBConnector;
import com.turizmAcenteSistemi.helper.Helper;

import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

public class SearchRoomGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_location;
    private JSpinner spn_adult;
    private JSpinner spn_child;
    private JTextField fld_checkin;
    private JTextField fld_checkout;
    private JButton btn_search;
    private JButton btn_back;
    private String checkInText = "";
    private String checkOutText = "";
    private boolean statusIn = true;
    private boolean statusOut = true;
    private User user;

    private ArrayList<Integer> hotelIdList;
    private ArrayList<Integer> roomIdList;

    public SearchRoomGUI(User user) {
        this.user = user;
        add(wrapper);
        setSize(700, 500);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        int min = 0;
        int max = 100;
        int step = 1;
        int initValue = 0;

        SpinnerModel spinnerModelAdult = new SpinnerNumberModel(initValue, min, max, step);
        SpinnerModel spinnerModelChild = new SpinnerNumberModel(initValue, min, max, step);

        spn_adult.setModel(spinnerModelAdult);
        spn_child.setModel(spinnerModelChild);

        guestNumController();


        fld_checkin.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                checkInText = String.valueOf(checkInText);
                if (statusIn) {
                    try {
                        checkInText = checkInText.substring(0, checkInText.length() - 1);
                    } catch (StringIndexOutOfBoundsException exception) {
                        exception.getStackTrace();
                    }

                    statusIn = false;
                } else {
                    int length = fld_checkin.getText().length();
                    if (length == 7 || length == 4) {
                        checkInText = fld_checkin.getText();
                        checkInText += "-";
                        fld_checkin.setText(checkInText);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    statusIn = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        fld_checkout.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (statusOut) {
                    try {
                        checkOutText = checkOutText.substring(0, checkOutText.length() - 1);
                    } catch (StringIndexOutOfBoundsException exception) {
                        exception.getStackTrace();
                    }
                    statusOut = false;
                } else {
                    int length = fld_checkout.getText().length();
                    if (length == 7 || length == 4) {
                        checkOutText = fld_checkout.getText();
                        checkOutText += "-";
                        fld_checkout.setText(checkOutText);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    statusOut = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.isFieldEmpty(fld_location) || Helper.isFieldEmpty(fld_checkin) || Helper.isFieldEmpty(fld_checkout)) {
                    Helper.showMsg("fill");
                } else {
                    String location = fld_location.getText();
                    Date checkIn = Date.valueOf(fld_checkin.getText());
                    Date checkOut = Date.valueOf(fld_checkout.getText());
                    int numAdult = Integer.parseInt(spn_adult.getValue().toString());
                    int numChild = Integer.parseInt(spn_child.getValue().toString());
                    search(location, checkIn, checkOut, numAdult, numChild);
                }
            }
        });


        btn_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RezListGUI rezListGUI = new RezListGUI(user);
                dispose();
            }
        });
    }

    private void search(String location, Date checkIn, Date checkOut, int numAdult, int numChild) {
        ArrayList<Integer> hotels = searchHotel(searchQuery(location, checkIn, checkOut, numAdult, numChild));
        for (int hotelId : hotels) {
            if (!isRoomAvailable(hotelId)) {
                Helper.showMsg("İstenilen oda bulunamadı!");
            } else {
                ArrayList<Date> dates = getCheckInOutDate(hotelId);
                if (!isDateAvailable(dates, checkIn, checkOut)) {
                    Helper.showMsg("Belirtilen tarihlerde uygun otel bulunamadı!");
                } else {
                    ArrayList<Integer> rooms = getRoomList();
                    Hotel hotel = Hotel.getFetch(hotelId);
                    dispose();
                    SearchOutputGUI searchOutputGUI = new SearchOutputGUI(user, hotel, rooms, checkIn, checkOut, numAdult, numChild);
                }
            }
        }
    }

    private ArrayList<Integer> searchHotel(String query) {
        ArrayList<Integer> searchHotelList = new ArrayList<>();
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                searchHotelList.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return searchHotelList;
    }

    private boolean isRoomAvailable(int hotel_id) {
        int stock;
        boolean status = false;
        String query = "SELECT id, stock FROM room WHERE hotel_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, hotel_id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                stock = rs.getInt("stock");
                if (stock > 0) {
                    status = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return status;
    }

    private ArrayList<Date> getCheckInOutDate(int hotel_id) {
        String query = "SELECT checkin, checkout FROM rez WHERE hotel_id = ?";
        ArrayList<Date> dates = new ArrayList<>();
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, hotel_id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                Date checkin = rs.getDate("checkin");
                Date checkout = rs.getDate("checkout");
                dates.add(checkin);
                dates.add(checkout);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dates;
    }

    private boolean isDateAvailable(ArrayList<Date> dates, Date checkIn, Date checkOut) {
        try {
            Date checkin = dates.get(0);
            Date checkout = dates.get(1);
            return checkout.before(checkIn);
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private String searchQuery(String location, Date checkIn, Date checkOut, int numAdult, int numChild) {
        String query = "SELECT id, name, FROM hotel WHERE name ILIKE '%{{name}}%' OR city ILIKE %{{city}}%' OR region ILIKE %{{region}}%'";
        query = query.replace("{{name}}", location);
        query = query.replace("{{city}}", location);
        query = query.replace("{{region}}", location);
        return query;
    }


    private void guestNumController() {
        int child;
        int adult;
        try {
            adult = (int) spn_adult.getValue();
            child = (int) spn_child.getValue();
            if (adult < 0) {
                spn_adult.setValue(adult * -1);
            }
            if (child < 0) {
                spn_child.setValue(child * -1);
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ArrayList<Integer> getRoomList() {
        return roomIdList;
    }
}