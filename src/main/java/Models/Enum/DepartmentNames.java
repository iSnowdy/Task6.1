package Models.Enum;

public enum DepartmentNames {
    CONTABILIDAD("Contabilidad"),
    MARKETING("Marketing"),
    VENTAS("Ventas"),
    LOGISTICA("Logística");

    private final String departmentName;

    DepartmentNames(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentNameAsString() {
        return departmentName;
    }
}
