import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por calcular a corrente em um circuito RC ao longo do tempo.
 */
public class RCCircuitCalculator {

    /**
     * Representa um ponto da função i(t), contendo o tempo e a corrente correspondente.
     */
    public static class Ponto {
        public final double tempo;
        public final double corrente;

        public Ponto(double tempo, double corrente) {
            this.tempo = tempo;
            this.corrente = corrente;
        }

        @Override
        public String toString() {
            return String.format("t = %.4fs, i = %.6fA", tempo, corrente);
        }
    }

    /**
     * Calcula os valores da corrente i(t) = (V₀ / R) * e^(-t / (RC)) em um intervalo de tempo.
     *
     * @param R      Resistência em ohms. Deve ser > 0.
     * @param C      Capacitância em farads. Deve ser > 0.
     * @param V0     Tensão inicial em volts.
     * @param tStart Tempo inicial em segundos.
     * @param tEnd   Tempo final em segundos. Deve ser maior que tStart.
     * @param step   Intervalo de tempo entre os pontos. Deve ser > 0.
     * @return Lista de pontos (tempo, corrente) representando i(t)
     * @throws IllegalArgumentException se algum parâmetro for inválido
     */
    public static List<Ponto> calcularCorrente(double R, double C, double V0,
                                                double tStart, double tEnd, double step) {
        validarParametros(R, C, tStart, tEnd, step);

        List<Ponto> pontos = new ArrayList<>();
        double tempo = tStart;

        while (tempo <= tEnd + 1e-9) { // tolerância numérica
            double corrente = (V0 / R) * Math.exp(-tempo / (R * C));
            pontos.add(new Ponto(tempo, corrente));
            tempo += step;
        }

        return pontos;
    }

    /**
     * Verifica se os parâmetros fornecidos são válidos.
     *
     * @throws IllegalArgumentException se algum parâmetro for inválido
     */
    private static void validarParametros(double R, double C, double tStart, double tEnd, double step) {
        StringBuilder erros = new StringBuilder();

        if (R <= 0) erros.append("Resistência R deve ser maior que zero.\n");
        if (C <= 0) erros.append("Capacitância C deve ser maior que zero.\n");
        if (tEnd <= tStart) erros.append("Tempo final deve ser maior que o inicial.\n");
        if (step <= 0) erros.append("Passo de tempo deve ser maior que zero.\n");

        if (erros.length() > 0) {
            throw new IllegalArgumentException(erros.toString().trim());
        }
    }
}
