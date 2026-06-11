package com.adanext.tache;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TacheService {

    public List<Tache> listerToutes() {
        return List.of(
            new Tache(1L, "Préparer la démo", "A_FAIRE"),
            new Tache(2L, "Réviser JPA", "EN_COURS")
        );
    }
}