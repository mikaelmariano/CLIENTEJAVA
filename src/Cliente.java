public class Cliente {
    private String cpf;
    private String nome;
    private int codigo;
    private double credito;

    public Cliente(String cpf, String nome, int codigo, double credito) {
        this.cpf = cpf;
        this.nome = nome;
        this.codigo = codigo;
        this.credito = credito;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public double getCredito() {
        return credito;
    }

    @Override
    public String toString() {
        return cpf + ";" + nome + ";" + codigo + ";" + credito;
    }
}