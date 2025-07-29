import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Gera um gráfico de corrente i(t) para um circuito RC usando apenas bibliotecas nativas do Java.
 */
public class ChartGenerator {

    /**
     * Exibe o gráfico dos pontos fornecidos.
     *
     * @param pontos Lista de pontos (tempo, corrente)
     */
    public static void exibirGrafico(List<RCCircuitCalculator.Ponto> pontos) {
        if (pontos == null || pontos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum dado para exibir no gráfico.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame frame = new JFrame("Gráfico de Corrente i(t)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(new GraficoPanel(pontos));
        frame.setVisible(true);
    }

    /**
     * Painel customizado que desenha o gráfico na tela.
     */
    private static class GraficoPanel extends JPanel {

        private final List<RCCircuitCalculator.Ponto> pontos;
        private final int margem = 60;

        public GraficoPanel(List<RCCircuitCalculator.Ponto> pontos) {
            this.pontos = pontos;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (pontos.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int largura = w - 2 * margem;
            int altura = h - 2 * margem;

            // Valores máximos
            double tMax = pontos.get(pontos.size() - 1).tempo;
            double iMax = pontos.stream().mapToDouble(p -> p.corrente).max().orElse(1.0);

            // Eixos
            g2.setColor(Color.BLACK);
            g2.drawLine(margem, h - margem, w - margem, h - margem); // eixo X
            g2.drawLine(margem, margem, margem, h - margem);         // eixo Y

            // Grade e rótulos
            int div = 10;
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(new Color(220, 220, 220));

            for (int i = 0; i <= div; i++) {
                int x = margem + i * largura / div;
                int y = h - margem - i * altura / div;

                // Grade
                g2.drawLine(x, margem, x, h - margem); // vertical
                g2.drawLine(margem, y, w - margem, y); // horizontal

                // Rótulo eixo X
                g2.setColor(Color.DARK_GRAY);
                String tLabel = String.format("%.1f", tMax * i / div);
                int tWidth = g2.getFontMetrics().stringWidth(tLabel);
                g2.drawString(tLabel, x - tWidth / 2, h - margem + 20);
                g2.setColor(new Color(220, 220, 220));

                // Rótulo eixo Y
                g2.setColor(Color.DARK_GRAY);
                String iLabel = String.format("%.2f", iMax * i / div);
                int iWidth = g2.getFontMetrics().stringWidth(iLabel);
                g2.drawString(iLabel, margem - iWidth - 10, y + 5);
                g2.setColor(new Color(220, 220, 220));
            }

            // Curva
            int lastX = -1, lastY = -1;
            g2.setColor(new Color(0, 102, 204));

            for (RCCircuitCalculator.Ponto p : pontos) {
                int x = margem + (int) ((p.tempo / tMax) * largura);
                int y = h - margem - (int) ((p.corrente / iMax) * altura);

                g2.fillOval(x - 2, y - 2, 4, 4); // ponto

                if (lastX != -1) {
                    g2.drawLine(lastX, lastY, x, y);
                }

                lastX = x;
                lastY = y;
            }

            // Título e eixos
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            String titulo = "Gráfico de Corrente i(t)";
            int tw = g2.getFontMetrics().stringWidth(titulo);
            g2.drawString(titulo, (w - tw) / 2, margem / 2);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Tempo (s)", w / 2 - 30, h - 10);

            // Rótulo eixo Y rotacionado
            g2.rotate(-Math.PI / 2);
            g2.drawString("Corrente (A)", -h / 2 - 40, 20);
            g2.rotate(Math.PI / 2); // volta ao normal
        }
    }
}
