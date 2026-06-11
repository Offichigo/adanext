package com.adanext.tache;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    @GetMapping
    public List<String> listerTaches() {
        return List.of("Préparer la démo", "Réviser JPA");
    }
}