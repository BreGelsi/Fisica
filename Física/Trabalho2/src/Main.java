/**
 * Arquivo principal do simulador de circuito RC.
 * Inicia a interface gráfica para inserção de parâmetros.
 */
public class Main {
    public static void main(String[] args) {
        // Garante que a interface seja iniciada na thread correta
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainWindow(); // Cria e exibe a janela principal
        });
    }
}
