import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class CalculadoraGUI extends JFrame {

    private JTextField campoVelocidade, campoAltura;
    private JComboBox<String> planetaBox;
    private JTextArea resultadoArea;

    private static final String[] planetas = {
        "Terra", "Lua", "Marte", "Júpiter", "Vênus", "Mercúrio", "Saturno", "Urano", "Netuno"
    };

    public CalculadoraGUI() {
        // Janela principal
        setTitle("🌌 Calculadora de Lançamento Vertical");
        setSize(600, 540);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
    
        // 🎨 Paleta de cores
        Color fundo = new Color(245, 245, 250);
        Color destaque = new Color(60, 120, 200);
        Color branco = Color.WHITE;
        Color cinzaClaro = new Color(230, 230, 240);
        Color bordaLeve = new Color(200, 200, 220);
    
        getContentPane().setBackground(fundo);


        // 🖌️ Estilo do título
        JLabel titulo = new JLabel("Calculadora de Lançamento Vertical");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(Color.BLACK);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);
        


    
        // 🧩 Painel de entrada com estilo
        JPanel painelEntradas = new JPanel(new GridLayout(4, 2, 15, 15));
        painelEntradas.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(bordaLeve),
            "🔧 Dados de Entrada",
            0, 0, new Font("SansSerif", Font.BOLD, 14), destaque
        ));
        painelEntradas.setBackground(branco);
        painelEntradas.setPreferredSize(new Dimension(580, 200));
        painelEntradas.setOpaque(true);
    
        // 🌍 ComboBox Planetas
        JLabel labelPlaneta = new JLabel("🌍 Selecione o planeta:");
        labelPlaneta.setFont(new Font("SansSerif", Font.PLAIN, 14));
        planetaBox = new JComboBox<>(planetas);
        planetaBox.setBackground(branco);
        planetaBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        planetaBox.setFocusable(false);
    
        // 💨 Campo Velocidade
        JLabel labelVelocidade = new JLabel("Velocidade inicial (m/s):");
        labelVelocidade.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoVelocidade = new JTextField();
        campoVelocidade.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoVelocidade.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bordaLeve),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    
        // 📏 Campo Altura
        JLabel labelAltura = new JLabel("Altura inicial (m):");
        labelAltura.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoAltura = new JTextField();
        campoAltura.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoAltura.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bordaLeve),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    
        // 🔘 Botão estilizado e sempre visível
        JButton botaoCalcular = new JButton("🚀 Calcular");
        botaoCalcular.setFont(new Font("SansSerif", Font.BOLD, 14));
        botaoCalcular.setForeground(Color.WHITE);
        botaoCalcular.setBackground(new Color(45, 110, 190));
        botaoCalcular.setFocusPainted(false);
        botaoCalcular.setOpaque(true);
        botaoCalcular.setBorderPainted(false);
        botaoCalcular.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🔁 Efeito hover (mudança de cor ao passar o mouse)
        botaoCalcular.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botaoCalcular.setBackground(new Color(30, 90, 160));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botaoCalcular.setBackground(new Color(45, 110, 190));
            }
        });


    
        // ➕ Adiciona os componentes ao painel
        painelEntradas.add(labelPlaneta);
        painelEntradas.add(planetaBox);
        painelEntradas.add(labelVelocidade);
        painelEntradas.add(campoVelocidade);
        painelEntradas.add(labelAltura);
        painelEntradas.add(campoAltura);
        painelEntradas.add(new JLabel()); // espaço vazio
        painelEntradas.add(botaoCalcular);
    
        add(painelEntradas, BorderLayout.CENTER);
    
        // 🧾 Área de resultado
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setBackground(branco);
        resultadoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("📊 Resultados"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    
        JScrollPane scroll = new JScrollPane(resultadoArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        add(scroll, BorderLayout.SOUTH);
    
        // Ação do botão
        botaoCalcular.addActionListener(e -> calcularResultados());
    }
    

    private void calcularResultados() {
    try {
        // 🔹 Entrada e validação
        String planetaSelecionado = (String) planetaBox.getSelectedItem();
        String inputVelocidade = campoVelocidade.getText().trim().replace(",", ".");
        String inputAltura = campoAltura.getText().trim().replace(",", ".");

        if (inputVelocidade.isEmpty() || inputAltura.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Por favor, preencha todos os campos para continuar.",
                "Campos obrigatórios",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double v0 = Double.parseDouble(inputVelocidade);
        double y0 = Double.parseDouble(inputAltura);

        if (v0 < 0 || y0 < 0) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Os valores não podem ser negativos.",
                "Valor inválido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔹 Cálculo
        double g = Back.gravidadePorPlaneta(planetaSelecionado);
        double[] coef = Back.calcularCoeficientes(g, v0, y0);
        double a = coef[0], b = coef[1], c = coef[2];
        double tMax = Back.calcularTempoAlturaMax(a, b);
        double yMax = Back.calcularAlturaMaxima(a, b, c);
        double tTotal = Back.calcularTempoTotal(a, b, c);

        DecimalFormat df = new DecimalFormat("#0.00");

        // 🔹 Construir resultado visual e explicativo
        StringBuilder sb = new StringBuilder();
        sb.append("📐 Equação da altura:\n");
        sb.append(String.format("y(t) = %s t² + %s t + %s\n\n",
            df.format(a), df.format(b), df.format(c)));

        sb.append("📊 Resultados no planeta ").append(planetaSelecionado).append(" (g = ")
            .append(df.format(g)).append(" m/s²):\n");
        sb.append("• ✨ Tempo até altura máxima: ").append(df.format(tMax)).append(" s\n");
        sb.append("• ✨ Altura máxima alcançada: ").append(df.format(yMax)).append(" m\n");

        if (tTotal > 0) {
            sb.append("• ✨ Tempo total até o solo: ").append(df.format(tTotal)).append(" s\n\n");
        } else {
            sb.append("\n⚠️ O objeto não atinge o solo (sem raízes reais).\n\n");
        }

        // 🔹 Interpretação extra para usuário leigo
        sb.append("🧾 Interpretação:\n");
        sb.append("• O corpo foi lançado com velocidade de ").append(df.format(v0)).append(" m/s,");
        sb.append(" a partir de uma altura de ").append(df.format(y0)).append(" m.\n");
        sb.append("• Levou ").append(df.format(tMax)).append(" s para atingir o ponto mais alto (")
            .append(df.format(yMax)).append(" m).\n");
        if (tTotal > 0) {
            sb.append("• Retornou ao solo após ").append(df.format(tTotal)).append(" s.\n");
        }

        resultadoArea.setText(sb.toString());

        // 🔹 Confirmação visual
        JOptionPane.showMessageDialog(this,
            "✅ Cálculo realizado com sucesso!\nO gráfico será exibido a seguir.",
            "Concluído",
            JOptionPane.INFORMATION_MESSAGE);

        // 🔹 Exibir gráfico apenas se válido
        if (tTotal > 0) {
            sb.append("\n📈 Gráficos gerados:\n");
            sb.append("✔️ Altura vs Tempo\n");
            sb.append("✔️ Velocidade vs Tempo");

            resultadoArea.setText(sb.toString());

            JOptionPane.showMessageDialog(this,
                "✅ Cálculo realizado com sucesso!\nOs gráficos serão exibidos a seguir.",
                "Concluído",
                JOptionPane.INFORMATION_MESSAGE);

            // Gráfico de Altura vs Tempo
            JFrame frameGrafico = new JFrame("📈 Gráfico da Trajetória");
            frameGrafico.add(new GraficoParabola(a, b, c, tTotal, planetaSelecionado, g));
            frameGrafico.pack();
            frameGrafico.setLocationRelativeTo(this);
            frameGrafico.setVisible(true);

            // Gráfico de Velocidade vs Tempo
            JFrame frameVelocidade = new JFrame("📉 Velocidade vs Tempo");
            frameVelocidade.add(new GraficoVelocidadeTempo(v0, g, tTotal, planetaSelecionado));
            frameVelocidade.pack();
            frameVelocidade.setLocationRelativeTo(this);
            frameVelocidade.setVisible(true);

        } else {
            resultadoArea.setText(sb.toString());

            JOptionPane.showMessageDialog(this,
                "⚠️ Cálculo feito, mas o objeto não retornou ao solo.\nO gráfico de trajetória será exibido.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE);

            JFrame frameGrafico = new JFrame("📈 Gráfico da Trajetória");
            frameGrafico.add(new GraficoParabola(a, b, c, 2 * tMax, planetaSelecionado, g)); // tTotal inválido → usa 2*tMax como aproximação
            frameGrafico.pack();
            frameGrafico.setLocationRelativeTo(this);
            frameGrafico.setVisible(true);
        }


        

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
            "❌ Entrada inválida. Use apenas números (ex: 12.5 ou 1,8).",
            "Erro de formato",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
            "❌ Ocorreu um erro inesperado. Verifique os dados e tente novamente.",
            "Erro interno",
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI gui = new CalculadoraGUI();
            gui.setVisible(true);
        });
    }
}
