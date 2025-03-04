package Utils;

import Exceptions.DatabaseUpdateException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Utility class for handling user input related to object field selection and value modification.
 */

public class ObjectFieldsUtil {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user to select a field from an object for modification, excluding certain fields.
     *
     * @param object         The object whose field is to be selected.
     * @param excludedFields A {@code Set<String>} of field names to be excluded from selection.
     * @return The selected field wrapped in an {@code Optional<Field>}, or an empty Optional if the selection is invalid.
     */

    public static Optional<Field> promptUserForFieldSelection(Object object, Set<String> excludedFields) {
        List<Field> selectableFields = getSelectableFields(object, excludedFields);

        if (selectableFields.isEmpty()) {
            System.out.println("No editable fields available");
            return Optional.empty();
        }

        printFieldOptions(selectableFields);

        System.out.print("Enter the number of the field you want to modify: ");

        int fieldIndex;
        try {
            fieldIndex = scanner.nextInt();
            scanner.nextLine(); // Consume buffer
        } catch (InputMismatchException e) {
            System.out.println("Please enter an integer. Returning...");
            return Optional.empty();
        }

        return validateAndReturnField(selectableFields, fieldIndex);
    }

    /**
     * Retrieves a {@code List<Field>} of selectable fields from an object, excluding the specified fields.
     *
     * @param object         The object whose fields are to be retrieved.
     * @param excludedFields A {@code Set<String>} of field names to be excluded.
     * @return A list of selectable fields.
     */

    private static List<Field> getSelectableFields(Object object, Set<String> excludedFields) {
        Field[] fields = object.getClass().getDeclaredFields();
        List<Field> selectableFields = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            if (!excludedFields.contains(field.getName())) {
                selectableFields.add(field);
            }
        }
        return selectableFields;
    }

    /**
     * Prints available field options for user selection.
     *
     * @param fields The {@code List<Field>} of selectable fields.
     */

    private static void printFieldOptions(List<Field> fields) {
        for (int i = 0; i < fields.size(); i++) {
            System.out.println(i + ". " + fields.get(i).getName());
        }
    }

    /**
     * Validates the user selection and returns the corresponding field.
     *
     * @param selectableFields The {@code List<Field>} of selectable fields.
     * @param fieldIndex       The user-selected field index.
     * @return An {@code Optional<Field>} containing the Field selected or empty if invalid.
     */

    private static Optional<Field> validateAndReturnField(List<Field> selectableFields, int fieldIndex) {
        if (fieldIndex < 0 || fieldIndex >= selectableFields.size()) {
            System.out.println("Invalid selection. Please enter a valid field number");
            return Optional.empty();
        }

        Field selectedField = selectableFields.get(fieldIndex);
        System.out.println("Field: " + selectedField.getName() + " selected");
        return Optional.of(selectedField);
    }

    /**
     * Prompts the user to enter a new value for a given field.
     *
     * @param field The field to be modified.
     * @return The new value entered by the user.
     */

    public static Object promptUserForNewValue(Field field) {
        System.out.print("Enter the new value for " + field.getName() + ": ");

        if (field.getType() == int.class) {
            int newValue = scanner.nextInt();
            scanner.nextLine(); // Consume buffer
            return newValue;
        } else {
            return scanner.nextLine();
        }
    }
}
