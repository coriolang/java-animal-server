package view;

import controller.MainController;
import controller.listeners.ApplyConfigButtonListener;
import controller.listeners.WindowListener;
import controller.listeners.StartServerButtonListener;
import controller.listeners.StopServerButtonListener;

import java.awt.*;

public class ServerFrame extends Frame {

    // Labels
    private Label serverLabel,
            ipAddressLabel,
            ipAddressTextLabel,
            portLabel,
            configLabel,
            languageLabel,
            initializationTypeLabel,
            logsLabel;

    // Choices
    private Choice languageChoice,
            initializationTypeChoice;

    // Buttons
    private Button applyConfigButton,
            startServerButton,
            stopServerButton;

    // TextFields
    private TextField portTextField;

    // TextArea
    private TextArea logsTextArea;

    public ServerFrame(String appTitle) {
        // Frame
        super(appTitle);

        setLayout(null);
        setSize(640, 720);
        setLocationRelativeTo(null);
        setBackground(Color.GREEN);

        addWindowListener(new WindowListener(this));

        setLabels();
        setChoices();
        setButtons();
        setTextFields();
        setTextAreas();

        setVisible(true);
    }

    private void setLabels() {
        Font robotoBold = new Font("Roboto", Font.BOLD, 18);

        // Labels
        serverLabel = new Label(MainController.stringResources.getString("SERVER_LABEL"));
        ipAddressLabel = new Label(MainController.stringResources.getString("IP_ADDRESS_LABEL"));
        ipAddressTextLabel = new Label();
        portLabel = new Label(MainController.stringResources.getString("PORT_LABEL"));
        configLabel = new Label(MainController.stringResources.getString("CONFIG_LABEL"));
        languageLabel = new Label(MainController.stringResources.getString("LANGUAGE_LABEL"));
        initializationTypeLabel = new Label(MainController.stringResources.getString("INITIALIZATION_TYPE_LABEL"));
        logsLabel = new Label(MainController.stringResources.getString("LOGS_LABEL"));

        serverLabel.setBounds(16, 46, 608, 24);
        serverLabel.setFont(robotoBold);
        serverLabel.setBackground(Color.LIGHT_GRAY);
        ipAddressLabel.setBounds(16, 86, 120, 24);
        ipAddressLabel.setBackground(Color.LIGHT_GRAY);
        ipAddressTextLabel.setBounds(152, 86, 472, 24);
        ipAddressTextLabel.setBackground(Color.LIGHT_GRAY);
        portLabel.setBounds(16, 126, 120, 24);
        portLabel.setBackground(Color.LIGHT_GRAY);
        configLabel.setBounds(16, 206, 608, 24);
        configLabel.setFont(robotoBold);
        configLabel.setBackground(Color.LIGHT_GRAY);
        languageLabel.setBounds(16, 246, 120, 24);
        languageLabel.setBackground(Color.LIGHT_GRAY);
        initializationTypeLabel.setBounds(16, 286, 120, 24);
        initializationTypeLabel.setBackground(Color.LIGHT_GRAY);
        logsLabel.setBounds(16, 326, 608, 24);
        logsLabel.setFont(robotoBold);
        logsLabel.setBackground(Color.LIGHT_GRAY);

        add(serverLabel);
        add(ipAddressLabel);
        add(ipAddressTextLabel);
        add(portLabel);
        add(configLabel);
        add(languageLabel);
        add(initializationTypeLabel);
        add(logsLabel);
    }

    private void setChoices() {
        // Choices
        languageChoice = new Choice();
        initializationTypeChoice = new Choice();

        languageChoice.setBounds(152, 246, 336, 24);
        initializationTypeChoice.setBounds(152, 286, 336, 24);

        languageChoice.addItem(MainController.stringResources.getString("RUSSIAN_LANG_ITEM"));
        languageChoice.addItem(MainController.stringResources.getString("ENGLISH_LANG_ITEM"));

        initializationTypeChoice.addItem(MainController.stringResources.getString("EMPTY_INIT_ITEM"));
        initializationTypeChoice.addItem(MainController.stringResources.getString("DEFAULT_INIT_ITEM"));
        initializationTypeChoice.addItem(MainController.stringResources.getString("FROM_FILE_INIT_ITEM"));

        add(languageChoice);
        add(initializationTypeChoice);
    }

    private void setButtons() {
        // Buttons
        applyConfigButton = new Button(MainController.stringResources.getString("APPLY_BUTTON"));
        startServerButton = new Button(MainController.stringResources.getString("START_SERVER_BUTTON"));
        stopServerButton = new Button(MainController.stringResources.getString("STOP_SERVER_BUTTON"));

        applyConfigButton.setBounds(504, 246, 120, 64);
        startServerButton.setBounds(16, 166, 296, 24);
        stopServerButton.setBounds(328, 166, 296, 24);

        applyConfigButton.addActionListener(new ApplyConfigButtonListener(this));
        startServerButton.addActionListener(new StartServerButtonListener(this));
        stopServerButton.addActionListener(new StopServerButtonListener(this));

        add(applyConfigButton);
        add(startServerButton);
        add(stopServerButton);
    }

    private void setTextFields() {
        // TextFields
        portTextField = new TextField();
        portTextField.setBounds(152, 126, 472, 24);
        add(portTextField);
    }

    private void setTextAreas() {
        // TextArea
        logsTextArea = new TextArea();
        logsTextArea.setBounds(16, 366, 608, 338);
        logsTextArea.setEditable(false);
        add(logsTextArea);
    }

    public void updateStrings() {
        // Labels
        serverLabel.setText(MainController.stringResources.getString("SERVER_LABEL"));
        ipAddressLabel.setText(MainController.stringResources.getString("IP_ADDRESS_LABEL"));
        portLabel.setText(MainController.stringResources.getString("PORT_LABEL"));
        configLabel.setText(MainController.stringResources.getString("CONFIG_LABEL"));
        languageLabel.setText(MainController.stringResources.getString("LANGUAGE_LABEL"));
        initializationTypeLabel.setText(MainController.stringResources.getString("INITIALIZATION_TYPE_LABEL"));
        logsLabel.setText(MainController.stringResources.getString("LOGS_LABEL"));

        // Choices
        languageChoice.removeAll();
        languageChoice.addItem(MainController.stringResources.getString("RUSSIAN_LANG_ITEM"));
        languageChoice.addItem(MainController.stringResources.getString("ENGLISH_LANG_ITEM"));

        initializationTypeChoice.removeAll();
        initializationTypeChoice.addItem(MainController.stringResources.getString("EMPTY_INIT_ITEM"));
        initializationTypeChoice.addItem(MainController.stringResources.getString("DEFAULT_INIT_ITEM"));
        initializationTypeChoice.addItem(MainController.stringResources.getString("FROM_FILE_INIT_ITEM"));

        // Buttons
        applyConfigButton.setLabel(MainController.stringResources.getString("APPLY_BUTTON"));
        startServerButton.setLabel(MainController.stringResources.getString("START_SERVER_BUTTON"));
        stopServerButton.setLabel(MainController.stringResources.getString("STOP_SERVER_BUTTON"));
    }

    public Label getIpAddressTextLabel() {
        return ipAddressTextLabel;
    }

    public Choice getLanguageChoice() {
        return languageChoice;
    }

    public Choice getInitializationTypeChoice() {
        return initializationTypeChoice;
    }

    public Button getApplyConfigButton() {
        return applyConfigButton;
    }

    public Button getStartServerButton() {
        return startServerButton;
    }

    public Button getStopServerButton() {
        return stopServerButton;
    }

    public TextField getPortTextField() {
        return portTextField;
    }

    public TextArea getLogsTextArea() {
        return logsTextArea;
    }
}
