# cli

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TelegramCLIGUI extends JFrame {
    private JTextArea outputArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private Process process;

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
        clearButton = new JButton("Clear");

        // Размещение компонентов
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.add(clearButton, BorderLayout.WEST);
        add(inputPanel, BorderLayout.SOUTH);

        // Обработка нажатия кнопки "Send"
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                if (!command.isEmpty()) {
                    new Thread(() -> executeCommand(command)).start(); // Запуск в отдельном потоке
                    inputField.setText("");
                }
            }
        });

        // Обработка нажатия кнопки "Clear"
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
            }
        });

        // Запуск процесса Telegram CLI
        startTelegramCLI();
    }

    private void startTelegramCLI() {
        try {
            process = Runtime.getRuntime().exec("C:\\telegram-cli\\telegram-cli.exe -k C:\\telegram-cli\\tg-server.pub -W");
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                     BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        appendOutput(line);
                    }
                    while ((line = errorReader.readLine()) != null) {
                        appendOutput("Error: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeCommand(String command) {
        try {
            if (process != null) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                writer.write(command + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> outputArea.append(text + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelegramCLIGUI().setVisible(true));
    }
}
```



Вход Telegram 


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class NchatRunner {
    public static void main(String[] args) {
        try {
            // Запуск nchat как внешнего процесса
            Process process = new ProcessBuilder("python", "nchat.py", "--config", "configs/nostr.json")
                    .redirectErrorStream(true)
                    .start();

            // Чтение вывода nchat
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Ожидание завершения процесса
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
