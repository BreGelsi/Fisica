import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {

    private JTextField rField, cField, v0Field, tStartField, tEndField, stepField;
    private JLabel statusLabel;

    public MainWindow() {
        super("Simulador de Circuito RC");

        // Aparência geral
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 440);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Cabeçalho institucional
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 60, 90));
        JLabel title = new JLabel("Simulador de Circuito RC", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Painel principal com entradas + lateral
        JPanel centralPanel = new JPanel(new BorderLayout(15, 15));
        centralPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        centralPanel.setBackground(Color.WHITE);

        // Painel de entrada de dados
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);
        rField = addField(inputPanel, "Resistência R (Ω):", "1000");
        cField = addField(inputPanel, "Capacitância C (F):", "0.001");
        v0Field = addField(inputPanel, "Tensão Inicial V₀ (V):", "5");
        tStartField = addField(inputPanel, "Tempo Inicial (s):", "0");
        tEndField = addField(inputPanel, "Tempo Final (s):", "5");
        stepField = addField(inputPanel, "Passo de Tempo (s):", "0.1");

        // Status
        statusLabel = new JLabel("Insira os parâmetros e clique em Gerar Gráfico.");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(60, 60, 60));
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusLabel);

        centralPanel.add(inputPanel, BorderLayout.CENTER);

        // Painel lateral com explicação
        JTextArea explicacao = new JTextArea(
            "📘 Fórmula utilizada:\n\n" +
            "     i(t) = (V₀ / R) * e^(-t / (R * C))\n\n" +
            "🔍 Onde:\n" +
            " • V₀: Tensão inicial (V)\n" +
            " • R: Resistência (Ω)\n" +
            " • C: Capacitância (F)\n" +
            " • t: Tempo (s)\n\n" +
            "🧮 Essa equação descreve a corrente i(t) durante\n" +
            "a descarga de um capacitor em um circuito RC."
        );
        explicacao.setFont(new Font("Monospaced", Font.PLAIN, 12));
        explicacao.setEditable(false);
        explicacao.setLineWrap(true);
        explicacao.setWrapStyleWord(true);
        explicacao.setBackground(new Color(248, 248, 250));
        explicacao.setBorder(BorderFactory.createTitledBorder("📐 Equação do Circuito"));
        JScrollPane scrollPane = new JScrollPane(explicacao);
        scrollPane.setPreferredSize(new Dimension(320, 0));
        centralPanel.add(scrollPane, BorderLayout.EAST);

        add(centralPanel, BorderLayout.CENTER);

        // Rodapé com botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        buttonPanel.setBackground(Color.WHITE);

        JButton resetBtn = new JButton("Limpar");
        JButton gerarBtn = new JButton("Gerar Gráfico");

        // Estilização institucional
        gerarBtn.setBackground(new Color(0, 120, 215));
        gerarBtn.setForeground(Color.WHITE);
        gerarBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        gerarBtn.setOpaque(true);
        gerarBtn.setBorderPainted(false);

        resetBtn.setBackground(new Color(230, 230, 230));
        resetBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        resetBtn.setOpaque(true);
        resetBtn.setBorderPainted(false);

        buttonPanel.add(resetBtn);
        buttonPanel.add(gerarBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ações
        gerarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double R = parseInput(rField, "Resistência R");
                    double C = parseInput(cField, "Capacitância C");
                    double V0 = parseInput(v0Field, "Tensão V₀");
                    double tStart = parseInput(tStartField, "Tempo Inicial");
                    double tEnd = parseInput(tEndField, "Tempo Final");
                    double step = parseInput(stepField, "Passo de Tempo");

                    if (tEnd <= tStart) {
                        showError("Tempo final deve ser maior que o tempo inicial.");
                        return;
                    }
                    if (step <= 0) {
                        showError("O passo de tempo deve ser maior que zero.");
                        return;
                    }

                    List<RCCircuitCalculator.Ponto> pontos =
                        RCCircuitCalculator.calcularCorrente(R, C, V0, tStart, tEnd, step);

                    ChartGenerator.exibirGrafico(pontos);
                    statusLabel.setText("Gráfico gerado com sucesso!");

                } catch (NumberFormatException ex) {
                    // já tratado no parseInput
                } catch (IllegalArgumentException ex) {
                    showError(ex.getMessage());
                }
            }
        });

        resetBtn.addActionListener(e -> {
            rField.setText("1000");
            cField.setText("0.001");
            v0Field.setText("5");
            tStartField.setText("0");
            tEndField.setText("5");
            stepField.setText("0.1");
            statusLabel.setText("Parâmetros redefinidos.");
        });

        setVisible(true);
    }

    private JTextField addField(JPanel panel, String labelText, String defaultValue) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JTextField field = new JTextField(defaultValue);
        panel.add(label);
        panel.add(field);
        return field;
    }

    private double parseInput(JTextField field, String label) throws NumberFormatException {
        try {
            return Double.parseDouble(field.getText().replace(",", "."));
        } catch (NumberFormatException ex) {
            showError("Valor inválido para " + label + ". Digite um número.");
            throw ex;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Erro: " + message);
    }
}
