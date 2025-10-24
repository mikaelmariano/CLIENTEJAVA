import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Cliente> listaDeClientes = new ArrayList<>();

        String arquivoEntrada = "clientes.csv";

        // 1️⃣ Leitura do arquivo de entrada
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoEntrada))) {
            String linha;
            boolean primeira = true;

            while ((linha = br.readLine()) != null) {
                if (primeira) { // pula o cabeçalho
                    primeira = false;
                    continue;
                }

                String[] partes = linha.split(";");
                if (partes.length < 4) continue;

                String cpf = partes[0].trim();
                String nome = partes[1].trim();
                int codigo = Integer.parseInt(partes[2].trim());
                double credito = Double.parseDouble(partes[3].trim());

                listaDeClientes.add(new Cliente(cpf, nome, codigo, credito));
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        // 2️⃣ Filtragem e gravação dos arquivos
        salvarClientesFiltrados(listaDeClientes, 2000);
        salvarClientesFiltrados(listaDeClientes, 3000);
        salvarClientesFiltrados(listaDeClientes, 4000);

        // 3️⃣ Geração do resumo
        gerarResumo(listaDeClientes);
    }

    private static void salvarClientesFiltrados(List<Cliente> lista, double limite) {
        String nomeArquivo = "clientes" + (int) limite + ".csv";
        List<Cliente> filtrados = lista.stream()
                .filter(c -> c.getCredito() > limite)
                .collect(Collectors.toList());

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(nomeArquivo))) {
            bw.write("CPF;NOME;CODIGO;CREDITO\n");
            for (Cliente c : filtrados) {
                bw.write(c.toString() + "\n");
            }
            System.out.println("Arquivo " + nomeArquivo + " gerado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao gravar " + nomeArquivo + ": " + e.getMessage());
        }
    }

    private static void gerarResumo(List<Cliente> lista) {
        int total = lista.size();
        double totalCredito = lista.stream().mapToDouble(Cliente::getCredito).sum();

        double[] limites = {2000, 3000, 4000};
        String[] faixas = {" >2000", " >3000", " >4000"};

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("resumo.csv"))) {
            bw.write("Faixa;Quantidade;Percentual;TotalCredito\n");

            for (int i = 0; i < limites.length; i++) {
                double limite = limites[i];
                long qtd = lista.stream().filter(c -> c.getCredito() > limite).count();
                double totalFaixa = lista.stream()
                        .filter(c -> c.getCredito() > limite)
                        .mapToDouble(Cliente::getCredito)
                        .sum();
                double percentual = (qtd * 100.0) / total;

                bw.write(faixas[i] + ";" + qtd + ";" +
                        String.format(Locale.US, "%.2f", percentual) + "%;" +
                        String.format(Locale.US, "%.2f", totalFaixa) + "\n");
            }

            bw.write("\nTotal de clientes;" + total + ";;" + String.format(Locale.US, "%.2f", totalCredito));
            System.out.println("Arquivo resumo.csv gerado com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao gerar resumo: " + e.getMessage());
        }
    }
}