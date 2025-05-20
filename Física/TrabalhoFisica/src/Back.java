public class Back {

    /**
     * Retorna os coeficientes a, b, c da equação: y(t) = a*t² + b*t + c
     */
    public static double[] calcularCoeficientes(double gravidade, double v0, double y0) {
        double a = -gravidade / 2;
        double b = v0;
        double c = y0;
        return new double[]{a, b, c};
    }

    /**
     * Calcula o tempo até a altura máxima.
     */
    public static double calcularTempoAlturaMax(double a, double b) {
        if (a == 0) return 0; // evita divisão por zero
        return -b / (2 * a);
    }

    /**
     * Calcula a altura máxima com base nos coeficientes da equação.
     */
    public static double calcularAlturaMaxima(double a, double b, double c) {
        double tMax = calcularTempoAlturaMax(a, b);
        return calcularAlturaNoTempo(a, b, c, tMax);
    }

    /**
     * Calcula o tempo total até o objeto atingir o solo (y = 0).
     * Usa fórmula de Bhaskara para equações quadráticas.
     */
    public static double calcularTempoTotal(double a, double b, double c) {
        double delta = b * b - 4 * a * c;
        if (delta < 0 || a == 0) return -1; // sem raízes reais ou equação linear
        double raiz = Math.sqrt(delta);
        double t1 = (-b + raiz) / (2 * a);
        double t2 = (-b - raiz) / (2 * a);
        return Math.max(t1, t2); // maior tempo positivo
    }

    /**
     * Calcula a altura y(t) para um tempo t específico.
     */
    public static double calcularAlturaNoTempo(double a, double b, double c, double t) {
        return a * t * t + b * t + c;
    }

    /**
     * Calcula a velocidade em um instante t: v(t) = v0 - g*t
     */
    public static double calcularVelocidadeNoTempo(double gravidade, double v0, double t) {
        return v0 - gravidade * t;
    }

    /**
     * Retorna o valor da gravidade para um planeta conhecido.
     */
    public static double gravidadePorPlaneta(String planeta) {
        switch (planeta.toLowerCase()) {
            case "lua": return 1.62;
            case "marte": return 3.71;
            case "júpiter": return 24.79;
            case "vênus": return 8.87;
            case "mercúrio": return 3.7;
            case "saturno": return 10.44;
            case "urano": return 8.69;
            case "netuno": return 11.15;
            default: return 9.8; // Terra
        }
    }

    /**
     * Gera uma tabela de tempo x altura (com N pontos entre 0 e tempo final).
     */
    public static void imprimirTabelaDeAlturas(double a, double b, double c, double tFinal, int pontos) {
        double passo = tFinal / pontos;
        System.out.println("\n🧪 Tabela (tempo x altura):");
        System.out.println("---------------------------");
        for (int i = 0; i <= pontos; i++) {
            double t = i * passo;
            double y = calcularAlturaNoTempo(a, b, c, t);
            System.out.printf("t = %.2f s → y = %.2f m\n", t, y);
        }
    }
}
