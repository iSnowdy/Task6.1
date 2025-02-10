package Models.Enum;

public enum EmployeePosition {
    DEPENDIENTE("Dependiente"),
    VENDEDOR("Vendedor"),
    RESPONSABLE("Responsable"),
    ANALISTA("Analista"),
    PRESIDENTE("Presidente");

    private final String position;

    EmployeePosition(String position) {
        this.position = position;
    }

    public String getEmployeePositionAsString() {
        return position;
    }
}
