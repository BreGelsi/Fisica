import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Anima a exibição do gráfico de corrente i(t) em um circuito RC.
 * Usa apenas bibliotecas nativas do Java.
 */
public class AnimationTimer {

    /**
     * Exibe uma janela com o gráfico animado, desenhando ponto a ponto.
     *
     * @param pontos      Lista de pontos (tempo, corrente) a serem desenhados
     * @param intervaloMs Intervalo entre cada ponto (em milissegundos)
     */
    public static void exibirAnimado(List<RCCircuitCalculator.Ponto> pontos, int intervaloMs) {
        if (pontos == null || pontos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum dado para exibir na animação.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame frame = new JFrame("Animação de Corrente i(t)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        PainelAnimado painel = new PainelAnimado(pontos);
        frame.add(painel);
        frame.setVisible(true);

        // Timer para animar a curva ponto a ponto
        Timer timer = new Timer(intervaloMs, e -> {
            painel.avancar();
            painel.repaint();
            if (painel.acabou()) {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(frame, "Animação finalizada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        timer.start();
    }

    /**
     * Painel que desenha o gráfico animado, ponto a ponto.
     */
    private static class PainelAnimado extends JPanel {

        private final List<RCCircuitCalculator.Ponto> pontos;
        private int indiceAtual = 0;
        private final int margem = 60;

        public PainelAnimado(List<RCCircuitCalculator.Ponto> pontos) {
            this.pontos = pontos;
            setBackground(Color.WHITE);
        }

        /**
         * Avança um ponto na animação.
         */
        public void avancar() {
            if (indiceAtual < pontos.size()) {
                indiceAtual++;
            }
        }

        /**
         * Verifica se todos os pontos já foram desenhados.
         */
        public boolean acabou() {
            return indiceAtual >= pontos.size();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (pontos.isEmpty() || indiceAtual == 0) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int largura = w - 2 * margem;
            int altura = h - 2 * margem;

            // Determina escala com base nos dados
            double tMax = pontos.get(pontos.size() - 1).tempo;
            double iMax = pontos.stream().mapToDouble(p -> p.corrente).max().orElse(1.0);

            // Desenha eixos
            g2.setColor(Color.BLACK);
            g2.drawLine(margem, h - margem, w - margem, h - margem); // eixo X
            g2.drawLine(margem, margem, margem, h - margem);         // eixo Y

            // Desenha grid e rótulos
            int div = 10;
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(new Color(220, 220, 220));

            for (int i = 0; i <= div; i++) {
                int x = margem + i * largura / div;
                int y = h - margem - i * altura / div;

                g2.drawLine(x, margem, x, h - margem); // vertical
                g2.drawLine(margem, y, w - margem, y); // horizontal

                // Rótulos do eixo X
                g2.setColor(Color.DARK_GRAY);
                String tLabel = String.format("%.1f", tMax * i / div);
                int tWidth = g2.getFontMetrics().stringWidth(tLabel);
                g2.drawString(tLabel, x - tWidth / 2, h - margem + 20);

                // Rótulos do eixo Y
                String iLabel = String.format("%.2f", iMax * i / div);
                int iWidth = g2.getFontMetrics().stringWidth(iLabel);
                g2.drawString(iLabel, margem - iWidth - 10, y + 5);

                g2.setColor(new Color(220, 220, 220)); // volta para a cor da grade
            }

            // Desenha a curva animada
            g2.setColor(new Color(0, 102, 204));
            int lastX = -1, lastY = -1;

            for (int i = 0; i < indiceAtual; i++) {
                RCCircuitCalculator.Ponto p = pontos.get(i);
                int x = margem + (int) ((p.tempo / tMax) * largura);
                int y = h - margem - (int) ((p.corrente / iMax) * altura);

                g2.fillOval(x - 2, y - 2, 4, 4); // ponto

                if (lastX != -1) {
                    g2.drawLine(lastX, lastY, x, y);
                }

                lastX = x;
                lastY = y;
            }

            // Título e nomes dos eixos
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            String titulo = "Animação de Corrente i(t)";
            int tw = g2.getFontMetrics().stringWidth(titulo);
            g2.drawString(titulo, (w - tw) / 2, margem / 2);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("Tempo (s)", w / 2 - 30, h - 10);

            // Rótulo vertical girado
            g2.rotate(-Math.PI / 2);
            g2.drawString("Corrente (A)", -h / 2 - 40, 20);
            g2.rotate(Math.PI / 2); // volta ao normal
        }
    }
}
