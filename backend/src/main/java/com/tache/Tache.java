package com.adanext.tache;

public class Tache {

    private Long id;
    private String titre;
    private String statut;

    public Tache(Long id, String titre, String statut) {
        this.id = id;
        this.titre = titre;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getStatut() { return statut; }
}