package backend.com.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private double amount;

    private String method; // WAVE / ORANGE

    private String status; // PENDING / SUCCESS / FAILED

    private String transactionId;

    // Constructeur vide (OBLIGATOIRE)
    public Payement() {
    }

    // Constructeur complet
    public Payement(Long id, String phone, double amount, String method, String status, String transactionId) {
        this.id = id;
        this.phone = phone;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.transactionId = transactionId;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
