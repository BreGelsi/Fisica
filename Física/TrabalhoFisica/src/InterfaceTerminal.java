import java.util.Scanner;

public class InterfaceTerminal {

    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            limparTela();
            exibirCabecalho();
            String planeta = lerPlaneta();
            double gravidade = Back.gravidadePorPlaneta(planeta);

            double v0 = lerDouble("👉 Digite a velocidade inicial (m/s): ");
            double y0 = lerDouble("👉 Digite a altura inicial (m): ");

            double[] coef = Back.calcularCoeficientes(gravidade, v0, y0);
            double a = coef[0], b = coef[1], c = coef[2];

            double tMax = Back.calcularTempoAlturaMax(a, b);
            double yMax = Back.calcularAlturaMaxima(a, b, c);
            double tTotal = Back.calcularTempoTotal(a, b, c);

            exibirResultados(planeta, gravidade, a, b, c, tMax, yMax, tTotal);

            System.out.print("\n🔁 Deseja fazer outro cálculo? (s/n): ");
            continuar = sc.next().trim().equalsIgnoreCase("s");
            sc.nextLine(); // consumir quebra de linha
        }

        System.out.println("\n✅ Programa encerrado. Obrigada por usar!");
        sc.close();
    }

    private void exibirCabecalho() {
        System.out.println("===============================================");
        System.out.println("🚀 CALCULADORA DE LANÇAMENTO VERTICAL");
        System.out.println("===============================================");
    }

    private String lerPlaneta() {
        System.out.println("\n🌍 Planetas disponíveis:");
        System.out.println("Terra, Lua, Marte, Júpiter, Vênus, Mercúrio, Saturno, Urano, Netuno");

        System.out.print("Digite o planeta desejado (ou pressione Enter para 'Terra'): ");
        String planeta = sc.nextLine().trim();
        return planeta.isEmpty() ? "Terra" : planeta;
    }

    private double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(sc.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("❌ Valor inválido. Digite um número decimal (use ponto se necessário).");
            }
        }
    }

    private void exibirResultados(String planeta, double g, double a, double b, double c, double tMax, double yMax, double tTotal) {
        System.out.println("\n📐 Equação da altura:");
        System.out.printf("y(t) = %.2ft² + %.2ft + %.2f\n", a, b, c);

        System.out.println("\n📊 Resultados:");
        System.out.printf("🌍 Planeta: %s (g = %.2f m/s²)\n", planeta, g);
        System.out.printf("🕒 Tempo até a altura máxima: %.2f s\n", tMax);
        System.out.printf("📍 Altura máxima: %.2f m\n", yMax);

        if (tTotal > 0) {
            System.out.printf("🧭 Tempo total até atingir o solo: %.2f s\n", tTotal);
            Back.imprimirTabelaDeAlturas(a, b, c, tTotal, 10);
        } else {
            System.out.println("⚠️ O objeto não atinge o solo (sem raízes reais).");
        }
    }

    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
