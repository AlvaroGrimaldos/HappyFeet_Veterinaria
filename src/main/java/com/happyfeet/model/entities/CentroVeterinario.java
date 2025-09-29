package com.happyfeet.model.entities;

public class CentroVeterinario {
    private static CentroVeterinario instancia;

    private Integer id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String nit;

    private CentroVeterinario() {
        // Datos del centro veterinario (puedes cambiar estos valores)
        this.id = 1;
        this.nombre = "Centro Veterinario Happy Feet";
        this.direccion = "Calle 45 #23-15, Bucaramanga";
        this.telefono = "6076543210";
        this.email = "info@sanfrancisco.vet";
        this.nit = "900123456-1";
    }

    public static synchronized CentroVeterinario getInstancia() {
        if (instancia == null) {
            instancia = new CentroVeterinario();
        }
        return instancia;
    }

    // Getters
    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getNit() { return nit; }

    // Setters (por si necesitas cambiar los datos)
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setNit(String nit) { this.nit = nit; }

    @Override
    public String toString() {
        return "CentroVeterinario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", nit='" + nit + '\'' +
                '}';
    }
}
