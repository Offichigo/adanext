package fr.adatechschool.adanext.dto.request;

import fr.adatechschool.adanext.model.Task;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskStatusRequest {

    @NotNull(message = "Le statut est obligatoire")
    private Task.Status status;

    public Task.Status getStatus() { return status; }
    public void setStatus(Task.Status status) { this.status = status; }
}
