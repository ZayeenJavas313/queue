import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class SimulasiAntrian extends JFrame {

    Queue<String[]> queue = new LinkedList<>();
    int nomor = 1;

    JTextField inputNama;
    JTextArea areaAntrian;

    public SimulasiAntrian() {
        setTitle("Simulasi Antrian Bank");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== PANEL ATAS =====
        JPanel panelAtas = new JPanel(new GridLayout(3, 1, 5, 5));

        JLabel label = new JLabel("Masukkan Nama:", JLabel.CENTER);
        panelAtas.add(label);

        inputNama = new JTextField();
        panelAtas.add(inputNama);

        JPanel panelTombol = new JPanel(new FlowLayout());

        JButton btnAmbil = new JButton("Ambil Antrian");
        JButton btnTampil = new JButton("Tampilkan");
        JButton btnPanggil = new JButton("Panggil");

        panelTombol.add(btnAmbil);
        panelTombol.add(btnTampil);
        panelTombol.add(btnPanggil);

        panelAtas.add(panelTombol);
        add(panelAtas, BorderLayout.NORTH);

        // ===== AREA ANTRIAN =====
        areaAntrian = new JTextArea();
        areaAntrian.setEditable(false);
        areaAntrian.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaAntrian);
        add(scroll, BorderLayout.CENTER);

        // ===== EVENT =====

        // Ambil Antrian
        btnAmbil.addActionListener(e -> {
            String nama = inputNama.getText().trim();

            if (nama.equals("")) {
                JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong!");
                return;
            }

            String[] data = {String.valueOf(nomor), nama};
            queue.add(data);
            nomor++;

            inputNama.setText("");
            updateTextArea();
        });

        // Tampilkan
        btnTampil.addActionListener(e -> updateTextArea());

        // Panggil
        btnPanggil.addActionListener(e -> {
            if (queue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Antrian kosong");
                return;
            }

            String[] data = queue.poll();
            String teks = "Nomor antrian " + data[0] +
                          ", atas nama " + data[1] +
                          ", silakan menuju loket";

            // bunyi "ting"
            Toolkit.getDefaultToolkit().beep();

            // popup
            JOptionPane.showMessageDialog(null, teks);

            // suara dijalankan di thread (agar tidak freeze)
            new Thread(() -> speak(teks)).start();

            updateTextArea();
        });
    }

    // ===== UPDATE TEXT AREA =====
    public void updateTextArea() {
        areaAntrian.setText("Daftar Antrian:\n\n");

        for (String[] data : queue) {
            areaAntrian.append("Nomor " + data[0] + " - " + data[1] + "\n");
        }
    }

    // ===== TEXT TO SPEECH (WINDOWS - TANPA INSTALL) =====
    public void speak(String text) {
        try {
            // escape tanda kutip
            text = text.replace("'", "");

            String command = "PowerShell -Command \"Add-Type -AssemblyName System.Speech; " +
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                    "$speak.Speak('" + text + "');\"";

            Runtime.getRuntime().exec(command);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimulasiAntrian().setVisible(true);
        });
    }
}
