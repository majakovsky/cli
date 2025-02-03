import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TelegramCLIGUI extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;
    private JButton sendButton;

    public TelegramCLIGUI() {
        setTitle("Telegram CLI GUI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создание компонентов
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        // Размещение компонентов
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Обработка нажатия кнопки
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                executeCommand(command);
                inputField.setText("");
            }
        });
    }

    private void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec("path/to/telegram-cli -k path/to/tg-server.pub -W");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            writer.write(command + "\n");
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelegramCLIGUI().setVisible(true);
            }
        });
    }
}