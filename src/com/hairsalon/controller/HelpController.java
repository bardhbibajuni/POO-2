package com.hairsalon.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class HelpController {

    @FXML
    private TextArea helpTextArea;

    @FXML
    public void initialize() {
        showAlbanianHelp();
    }

    @FXML
    public void showAlbanianHelp() {
        helpTextArea.setText("""
                MIRË SE VINI NË HAIR SALON SCHEDULER

                Ky aplikacion përdoret për menaxhimin e termineve të klientëve në një sallon parukerie.

                FUNKSIONET KRYESORE:
                1. Appointments - shtimi dhe shfaqja e termineve.
                2. Clients - menaxhimi i klientëve.
                3. Dashboard - statistika dhe grafikë.
                4. AI Insight - rekomandime të thjeshta bazuar në të dhëna.

                SI TË SHTONI NJË TERMIN:
                - Klikoni Appointments.
                - Plotësoni emrin e klientit.
                - Zgjedhni shërbimin, stilistin, datën, orën dhe statusin.
                - Klikoni Add.

                SI TË SHTONI NJË KLIENT:
                - Klikoni Clients.
                - Shkruani emrin, telefonin dhe email-in.
                - Klikoni Add.

                SHKURTESA:
                - Mund të përdorni tastin Tab për navigim mes fushave.
                - Butonat kryesorë mund të përdoren përmes menusë dhe toolbar-it.
                """);
    }

    @FXML
    public void showEnglishHelp() {
        helpTextArea.setText("""
                WELCOME TO HAIR SALON SCHEDULER

                This application is used to manage client appointments in a hair salon.

                MAIN FEATURES:
                1. Appointments - add and view appointments.
                2. Clients - manage salon clients.
                3. Dashboard - statistics and charts.
                4. AI Insight - simple recommendations based on data.

                HOW TO ADD AN APPOINTMENT:
                - Click Appointments.
                - Enter the client name.
                - Select service, stylist, date, time and status.
                - Click Add.

                HOW TO ADD A CLIENT:
                - Click Clients.
                - Enter name, phone and email.
                - Click Add.

                SHORTCUTS:
                - Use Tab to navigate between fields.
                - Main functions are accessible through menu and toolbar.
                """);
    }
}