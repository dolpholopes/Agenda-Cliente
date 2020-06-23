package com.example.agendacliente.model;

public class Agendamento {

    private String nome;
    private String contato;
    private boolean wpp;
    private boolean corte;
    private boolean barba;

    public Agendamento() {
    }

    public Agendamento(String nome, String contato, boolean wpp, boolean corte, boolean barba) {
        this.nome = nome;
        this.contato = contato;
        this.wpp = wpp;
        this.corte = corte;
        this.barba = barba;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public boolean isWpp() {
        return wpp;
    }

    public void setWpp(boolean wpp) {
        this.wpp = wpp;
    }

    public boolean isCorte() {
        return corte;
    }

    public void setCorte(boolean corte) {
        this.corte = corte;
    }

    public boolean isBarba() {
        return barba;
    }

    public void setBarba(boolean barba) {
        this.barba = barba;
    }
}
