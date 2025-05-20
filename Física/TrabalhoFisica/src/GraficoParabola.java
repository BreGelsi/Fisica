import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class GraficoParabola extends JPanel {

    private double a, b, c;
    private double tFinal;
    private String planeta;
    private double gravidade;

    private Timer timer;
    private int frameAtual = 0;
    private final int totalFrames = 500;

    public GraficoParabola(double a, double b, double c, double tFinal, String planeta, double gravidade) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.tFinal = tFinal;
        this.planeta = planeta;
        this.gravidade = gravidade;
        setPreferredSize(new Dimension(650, 450));

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
        setBackground(new Color(250, 250, 255));
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Título
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.setColor(new Color(30, 50, 120));
        g2.drawString("Trajetória Parabólica - " + planeta + " (g = " + String.format("%.2f", gravidade) + " m/s²)", 110, 25);

        // Área do gráfico
        int origemX = 60;
        int origemY = 380;
        int largura = 520;
        int altura = 280;

        // Moldura
        g2.setColor(new Color(220, 220, 240));
        g2.fillRoundRect(origemX - 20, origemY - altura - 20, largura + 40, altura + 40, 20, 20);

        // Eixos
        g2.setColor(Color.GRAY);
        g2.drawLine(origemX, origemY, origemX + largura, origemY); // eixo X
        g2.drawLine(origemX, origemY, origemX, origemY - altura);  // eixo Y

        // Escalas
        double escalaTempo = largura / tFinal;
        double yMaxAltura = Back.calcularAlturaMaxima(a, b, c);
        double escalaAltura = altura / yMaxAltura;

        DecimalFormat df = new DecimalFormat("#0.0");

        // Rótulos eixo X
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 0; i <= 10; i++) {
            double t = tFinal * i / 10;
            int x = origemX + (int)(t * escalaTempo);
            g2.setColor(new Color(200, 200, 200));
            g2.drawLine(x, origemY, x, origemY - altura);
            g2.setColor(Color.BLACK);
            g2.drawString(df.format(t) + "s", x - 10, origemY + 15);
        }

        // Rótulos eixo Y
        for (int i = 0; i <= 10; i++) {
            double y = yMaxAltura * i / 10;
            int yPix = origemY - (int)(y * escalaAltura);
            g2.setColor(new Color(220, 220, 220));
            g2.drawLine(origemX, yPix, origemX + largura, yPix);
            g2.setColor(Color.BLACK);
            g2.drawString(df.format(y) + "m", origemX - 35, yPix + 5);
        }

        // Legenda
        g2.setColor(new Color(0, 100, 220));
        g2.fillRect(origemX + largura - 100, origemY - altura + 20, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("Trajetória", origemX + largura - 85, origemY - altura + 29);

        // Traçar parábola animada
        g2.setColor(new Color(0, 100, 220));
        for (int px = 0; px < frameAtual - 1; px++) {
            double t1 = tFinal * px / totalFrames;
            double t2 = tFinal * (px + 1) / totalFrames;
            double y1 = a * t1 * t1 + b * t1 + c;
            double y2 = a * t2 * t2 + b * t2 + c;

            int x1 = origemX + (int)(t1 * escalaTempo);
            int yP1 = origemY - (int)(y1 * escalaAltura);
            int x2 = origemX + (int)(t2 * escalaTempo);
            int yP2 = origemY - (int)(y2 * escalaAltura);

            g2.drawLine(x1, yP1, x2, yP2);
        }

        // Altura máxima destacada
        double tMax = -b / (2 * a);
        double yMax = a * tMax * tMax + b * tMax + c;
        int xMax = origemX + (int)(tMax * escalaTempo);
        int yMaxPix = origemY - (int)(yMax * escalaAltura);

        if (frameAtual >= totalFrames) {
            g2.setColor(Color.RED);
            g2.fillOval(xMax - 4, yMaxPix - 4, 8, 8);
            g2.setColor(Color.BLACK);
            // Ajuste inteligente para evitar sobreposição
            int labelOffsetX = (xMax > origemX + largura - 100) ? -120 : 10;
            int labelOffsetY = (yMaxPix < origemY - altura + 30) ? 20 : -10;

            g2.drawString("Altura Máx: " + df.format(yMax) + " m", xMax + labelOffsetX, yMaxPix + labelOffsetY);

        }

        // Rótulo dos eixos
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("Tempo (s)", origemX + largura - 50, origemY + 35);
        g2.drawString("Altura (m)", origemX - 50, origemY - altura + 10);
    }
}
