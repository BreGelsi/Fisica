import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class GraficoVelocidadeTempo extends JPanel {

    private double v0, gravidade, tempoTotal;
    private String planeta;
    private Timer timer;
    private int frameAtual = 0;
    private final int totalFrames = 500;

    public GraficoVelocidadeTempo(double v0, double gravidade, double tempoTotal, String planeta) {
        this.v0 = v0;
        this.gravidade = gravidade;
        this.tempoTotal = tempoTotal;
        this.planeta = planeta;

        setPreferredSize(new Dimension(650, 470)); // Aumentado para dar espaço ao título

        timer = new Timer(3, e -> {
            frameAtual++;
            if (frameAtual >= totalFrames) {
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(248, 250, 255));
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (tempoTotal <= 0 || gravidade == 0) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("⚠️ Não é possível gerar o gráfico: valores inválidos de tempo ou gravidade.", 50, 200);
            return;
        }

        int origemX = 60;
        int origemY = 330;
        int largura = 520;
        int altura = 280;

        double vFinal = v0 - gravidade * tempoTotal;
        double margem = Math.abs(v0 * 0.1);
        double vMin = Math.min(vFinal, 0) - margem;
        double vMax = v0 + margem;

        double escalaTempo = largura / tempoTotal;
        double escalaVelocidade = altura / Math.max((vMax - vMin), 0.1);

        DecimalFormat df = new DecimalFormat("#0.0");

        // Título
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.setColor(new Color(30, 50, 120));
        g2.drawString("📉 Velocidade vs Tempo — " + planeta + " (g = " + String.format("%.2f", gravidade) + " m/s²)", 80, 30);

        // Moldura
        g2.setColor(new Color(230, 230, 245));
        g2.fillRoundRect(origemX - 20, origemY - altura - 20, largura + 40, altura + 40, 20, 20);

        // Faixa v = 0
        int yZero = origemY - (int)((0 - vMin) * escalaVelocidade);
        g2.setColor(new Color(210, 230, 250));
        g2.fillRect(origemX, yZero - 1, largura, 2);

        // Eixos
        g2.setColor(Color.GRAY);
        g2.drawLine(origemX, origemY, origemX + largura, origemY); // eixo x
        g2.drawLine(origemX, origemY, origemX, origemY - altura);  // eixo y

        // Marcas do eixo X (tempo)
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 0; i <= 10; i++) {
            double t = tempoTotal * i / 10;
            int x = origemX + (int)(t * escalaTempo);
            g2.setColor(new Color(210, 210, 210));
            g2.drawLine(x, origemY, x, origemY - altura);
            g2.setColor(Color.BLACK);
            g2.drawString(df.format(t) + "s", x - 10, origemY + 15);
        }

        // Marcas do eixo Y (reduzido para 6 divisões)
        for (int i = 0; i <= 6; i++) {
            double v = vMin + (vMax - vMin) * i / 6;
            int y = origemY - (int)((v - vMin) * escalaVelocidade);
            g2.setColor(new Color(230, 230, 230));
            g2.drawLine(origemX, y, origemX + largura, y);
            g2.setColor(Color.BLACK);
            g2.drawString(df.format(v) + " m/s", origemX - 55, y + 5);
        }

        // Reta animada da velocidade
        g2.setColor(new Color(0, 150, 100));
        int lastX = 0, lastY = 0;
        for (int px = 0; px < frameAtual - 1; px++) {
            double t1 = tempoTotal * px / totalFrames;
            double t2 = tempoTotal * (px + 1) / totalFrames;
            double v1 = v0 - gravidade * t1;
            double v2 = v0 - gravidade * t2;

            int x1 = origemX + (int)(t1 * escalaTempo);
            int y1 = origemY - (int)((v1 - vMin) * escalaVelocidade);
            int x2 = origemX + (int)(t2 * escalaTempo);
            int y2 = origemY - (int)((v2 - vMin) * escalaVelocidade);

            g2.drawLine(x1, y1, x2, y2);
            lastX = x2;
            lastY = y2;
        }

        // Pontos
        if (frameAtual >= totalFrames) {
            int xIni = origemX;
            int yIni = origemY - (int)((v0 - vMin) * escalaVelocidade);
            g2.setColor(new Color(0, 120, 255));
            g2.fillOval(xIni - 4, yIni - 4, 8, 8);

            g2.setColor(new Color(120, 200, 120));
            g2.fillOval(lastX - 4, lastY - 4, 8, 8);

            double tMax = (gravidade != 0) ? v0 / gravidade : 0;
            int xZero = origemX + (int)(tMax * escalaTempo);
            g2.setColor(Color.RED);
            g2.fillOval(xZero - 4, yZero - 4, 8, 8);

            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(Color.BLACK);
            g2.drawString("v = 0 em " + df.format(tMax) + "s", xZero + 8, yZero - 12);
        }

        // Legenda clara e bem posicionada
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.setColor(new Color(0, 150, 100));
        g2.fillRect(origemX + largura - 105, origemY - altura + 30, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("Velocidade(t)", origemX + largura - 90, origemY - altura + 40);

        // Rótulos dos eixos
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("Tempo (s)", origemX + largura - 50, origemY + 35);
        g2.drawString("Velocidade (m/s)", origemX - 50, origemY - altura + 10);
    }
}
