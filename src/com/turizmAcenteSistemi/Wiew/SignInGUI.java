package com.turizmAcenteSistemi.Wiew;

import com.turizmAcenteSistemi.Model.User;
import com.turizmAcenteSistemi.helper.Config;
import com.turizmAcenteSistemi.helper.DBConnector;
import com.turizmAcenteSistemi.helper.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class SignInGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_email;
    private JTextField fld_firstName;
    private JTextField fld_lastName;
    private JTextField fld_uName;
    private JTextField fld_pass;
    private JTextField fld_passAgain;
    private JButton btn_signin;

    public SignInGUI() {
        add(wrapper);
        setSize(600, 500);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_signin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = fld_email.getText();
                String firstname = fld_firstName.getText();
                String lastname = fld_lastName.getText();
                String uname = fld_uName.getText();
                String pass = fld_pass.getText();
                String passagain = fld_passAgain.getText();
                String type = "operator";

                if (Helper.isFieldEmpty(fld_email) || Helper.isFieldEmpty(fld_firstName) || Helper.isFieldEmpty(fld_lastName) || Helper.isFieldEmpty(fld_uName) || Helper.isFieldEmpty(fld_pass) || Helper.isFieldEmpty(fld_passAgain)) {
                    Helper.showMsg("fill");
                } else {
                    if (isEmailValid(email) && isPasswordMatch(pass, passagain)) {
                        Helper.showMsg("Kullanıcı başarıyla eklendi.");
                        LoginGUI loginGUI = new LoginGUI();
                    }
                }
            }
        });
    }

    private boolean isEmailValid(String mail) {
        if (mail.contains("@gmail.com") || mail.contains("@hotmail.com") || mail.contains("@icloud.com") || mail.contains("@yahoo.com") || mail.contains("@yandex.com") || mail.contains("@outlook.com")) {
            return true;
        } else {
            Helper.showMsg("Lütfen geçerli bir e-posta adresi girin!");
            return false;
        }
    }

    private boolean isPasswordMatch(String pass, String passagain) {
        if (!(pass.equals(passagain))) {
            Helper.showMsg("Şifre aynı değil!");
            return false;
        }
        return true;
    }

    private boolean addNewUser(String firstname, String lastname, String uname, String email, String pass, String type) {
        String query = "INSERT INTO user (firstname, lastname, uname, email, pass, type) VALUES (?,?,?,?,?,?)";

        User findUser = User.getFetch(uname, pass);
        if (findUser != null) {
            Helper.showMsg("Bu kullanıcı adı mevcut. Lütfen farklı bir kullanıcı adı giriniz.");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, firstname);
            pr.setString(2, lastname);
            pr.setString(3, uname);
            pr.setString(4, email);
            pr.setString(5, pass);
            pr.setObject(6, type, Types.OTHER);

            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
