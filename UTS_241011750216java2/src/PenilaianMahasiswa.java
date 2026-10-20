import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class PenilaianMahasiswa extends JFrame {

    private JTextField txtNim, txtNama, txtSks, txtPresensi, txtTugas, txtUts, txtUas, txtNilaiAkhir, txtGrade;
    private JRadioButton rbLaki, rbPerempuan;
    private ButtonGroup bgJk;
    private JComboBox<String> cbMatkul;
    private JTextArea taLog;
    private final Map<String, Integer> matkul = new LinkedHashMap<>();
    private boolean sudahProses = false;

    public PenilaianMahasiswa() {
        // Daftar mata kuliah (bobot 2 dan 3 SKS, minimal 5)
        matkul.put("REKAYASA WEB", 2);
        matkul.put("BASIS DATA", 3);
        matkul.put("PEMROGRAMAN BERORIENTASI OBYEK", 3);
        matkul.put("JARINGAN KOMPUTER", 2);
        matkul.put("STRUKTUR DATA", 3);
        matkul.put("SISTEM OPERASI", 2);

        setTitle("Aplikasi Penilaian Mahasiswa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initMenuBar();
        initForm();
    }

    private void initMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu mFile = new JMenu("File");
        JMenuItem miExit = new JMenuItem("Exit");
        miExit.addActionListener(e -> System.exit(0));
        mFile.add(miExit);

        JMenu mApps = new JMenu("Apps");
        JMenuItem miNilai = new JMenuItem("Penilaian");
        miNilai.addActionListener(e -> {
            txtNim.requestFocus();
            JOptionPane.showMessageDialog(this, "Aplikasi Penilaian Mahasiswa", "Apps",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        mApps.add(miNilai);

        JMenu mAbout = new JMenu("About");
        JMenuItem miTentang = new JMenuItem("Tentang");
        miTentang.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Aplikasi Penilaian Mahasiswa\nUTS Pemrograman Berorientasi Obyek (JAVA II)\nJava Swing GUI",
                "Tentang", JOptionPane.INFORMATION_MESSAGE));
        mAbout.add(miTentang);

        bar.add(mFile);
        bar.add(mApps);
        bar.add(mAbout);
        setJMenuBar(bar);
    }

    private void initForm() {
        JLabel title = new JLabel("APLIKASI PENILAIAN MAHASISWA", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(title, BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(1, 2, 20, 0));

        // ---- Panel kiri: data mahasiswa ----
        JPanel kiri = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        txtNim = new JTextField(15);
        txtNama = new JTextField(15);
        rbLaki = new JRadioButton("LAKI-LAKI", true);
        rbPerempuan = new JRadioButton("PEREMPUAN");
        bgJk = new ButtonGroup();
        bgJk.add(rbLaki);
        bgJk.add(rbPerempuan);
        JPanel pJk = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pJk.add(rbLaki);
        pJk.add(rbPerempuan);

        cbMatkul = new JComboBox<>(matkul.keySet().toArray(new String[0]));
        txtSks = new JTextField(5);
        txtSks.setEditable(false);
        cbMatkul.addActionListener(e -> updateSks());
        updateSks();

        addRow(kiri, g, 0, "NIM", txtNim);
        addRow(kiri, g, 1, "NAMA", txtNama);
        addRow(kiri, g, 2, "JENIS KEL", pJk);
        addRow(kiri, g, 3, "MATA KULIAH", cbMatkul);
        addRow(kiri, g, 4, "SKS", txtSks);

        // ---- Panel kanan: bobot penilaian ----
        JPanel kanan = new JPanel(new GridBagLayout());
        kanan.setBorder(BorderFactory.createTitledBorder("Bobot Penilaian"));

        txtPresensi = new JTextField(8);
        txtTugas = new JTextField(8);
        txtUts = new JTextField(8);
        txtUas = new JTextField(8);
        txtNilaiAkhir = new JTextField(8);
        txtGrade = new JTextField(8);
        txtNilaiAkhir.setEditable(false);
        txtGrade.setEditable(false);

        addRow(kanan, g, 0, "PRESENSI", txtPresensi);
        addRow(kanan, g, 1, "TUGAS", txtTugas);
        addRow(kanan, g, 2, "UTS", txtUts);
        addRow(kanan, g, 3, "UAS", txtUas);
        addRow(kanan, g, 4, "NILAI AKHIR", txtNilaiAkhir);
        addRow(kanan, g, 5, "GRADE", txtGrade);

        form.add(kiri);
        form.add(kanan);

        // ---- Tombol ----
        JButton btnAdd = new JButton("Add Data");
        JButton btnClear = new JButton("Clear");
        JButton btnProses = new JButton("Proses");
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pBtn.add(btnAdd);
        pBtn.add(btnClear);
        pBtn.add(btnProses);

        btnProses.addActionListener(e -> proses());
        btnAdd.addActionListener(e -> addData());
        btnClear.addActionListener(e -> clearForm());

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(pBtn, BorderLayout.SOUTH);

        // ---- TextArea log di dalam JScrollPane ----
        taLog = new JTextArea(10, 50);
        taLog.setEditable(false);
        taLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(taLog);

        main.add(atas, BorderLayout.NORTH);
        main.add(sp, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent c) {
        g.gridy = row;
        g.gridx = 0;
        g.weightx = 0;
        p.add(new JLabel(label), g);
        g.gridx = 1;
        g.weightx = 1;
        p.add(c, g);
    }

    private void updateSks() {
        Object sel = cbMatkul.getSelectedItem();
        if (sel != null) {
            txtSks.setText(String.valueOf(matkul.get(sel.toString())));
        }
    }

    private int jumlahPertemuan() {
        return Integer.parseInt(txtSks.getText()) == 3 ? 21 : 14;
    }

    private boolean proses() {
        try {
            if (txtNim.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "NIM dan Nama harus diisi!", "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
            int presensi = Integer.parseInt(txtPresensi.getText().trim());
            double tugas = Double.parseDouble(txtTugas.getText().trim());
            double uts = Double.parseDouble(txtUts.getText().trim());
            double uas = Double.parseDouble(txtUas.getText().trim());
            int jml = jumlahPertemuan();

            // Validasi presensi
            if (presensi > jml) {
                JOptionPane.showMessageDialog(this,
                        "Presensi (" + presensi + ") melebihi jumlah pertemuan (" + jml + ")!",
                        "Validasi Presensi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (presensi < 0 || tugas < 0 || uts < 0 || uas < 0 || tugas > 100 || uts > 100 || uas > 100) {
                JOptionPane.showMessageDialog(this, "Nilai harus antara 0 - 100 (presensi tidak boleh negatif)!",
                        "Validasi Input", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double persenAbsen = ((double) presensi / jml) * 100;
            double nAbsen = persenAbsen * 0.1;
            double nTugas = tugas * 0.2;
            double nUts = uts * 0.3;
            double nUas = uas * 0.4;
            double akhir = nAbsen + nTugas + nUts + nUas;

            String grade;
            if (persenAbsen < 50) {
                grade = "E";
            } else if (persenAbsen < 75) {
                grade = "D";
            } else if (akhir > 80) {
                grade = "A";
            } else if (akhir > 60) {
                grade = "B";
            } else if (akhir > 40) {
                grade = "C";
            } else {
                grade = "D";
            }

            txtNilaiAkhir.setText(String.format("%.2f", akhir));
            txtGrade.setText(grade);
            sudahProses = true;
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Presensi, Tugas, UTS, dan UAS harus berupa angka!",
                    "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void addData() {
        if (!sudahProses && !proses()) {
            return;
        }
        String jk = rbLaki.isSelected() ? "LAKI-LAKI" : "PEREMPUAN";
        taLog.append(txtNim.getText().trim() + " | " + txtNama.getText().trim()
                + " | " + jk
                + " | Matkul : " + cbMatkul.getSelectedItem()
                + " | SKS : " + txtSks.getText()
                + " | Absensi : " + txtPresensi.getText().trim()
                + " | Tugas : " + txtTugas.getText().trim()
                + " | UTS : " + txtUts.getText().trim()
                + " | UAS : " + txtUas.getText().trim()
                + " | Nilai Akhir : " + txtNilaiAkhir.getText()
                + " | Grade : " + txtGrade.getText() + "\n");
    }

    private void clearForm() {
        txtNim.setText("");
        txtNama.setText("");
        rbLaki.setSelected(true);
        cbMatkul.setSelectedIndex(0);
        updateSks();
        txtPresensi.setText("");
        txtTugas.setText("");
        txtUts.setText("");
        txtUas.setText("");
        txtNilaiAkhir.setText("");
        txtGrade.setText("");
        sudahProses = false;
        txtNim.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PenilaianMahasiswa().setVisible(true));
    }
}
