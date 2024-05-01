import java.util.HashMap;

public class TwoPassAssembler {

    // Symbol Table to store labels and their addresses
    private static HashMap<String, Integer> symbolTable = new HashMap<>();

    // Pass 1: Populate symbol table with labels and their addresses
    private static void passOne(String[] assemblyCode) {
        int locationCounter = 0;
        for (String line : assemblyCode) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0) continue; // Skip empty lines
            if (parts[0].equals("START")) {
                locationCounter = Integer.parseInt(parts[1]);
            } else if (parts[0].equals("END")) {
                break;
            } else if (parts[0].endsWith(":")) {
                symbolTable.put(parts[0].substring(0, parts[0].length() - 1), locationCounter);
            } else {
                if (parts.length > 1 && parts[1].equals("DS")) {
                    symbolTable.put(parts[0], locationCounter);
                    locationCounter += Integer.parseInt(parts[2]);
                } else {
                    locationCounter++;
                }
            }
        }
    }

    // Pass 2: Generate intermediate code
    private static String[] passTwo(String[] assemblyCode) {
        String[] intermediateCode = new String[assemblyCode.length * 2]; // double the length for DL statements
        int index = 0;
        for (String line : assemblyCode) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0) continue; // Skip empty lines
            switch (parts[0]) {
                case "START":
                    intermediateCode[index++] = "(AD,01) (C," + parts[1] + ")";
                    break;
                case "READ":
                    intermediateCode[index++] = "(IS, 09) " + parts[1];
                    break;
                case "MOVER":
                    intermediateCode[index++] = "(IS, 01) AREG " + parts[2];
                    break;
                case "SUB":
                    intermediateCode[index++] = "(IS, 03) AREG " + parts[2];
                    break;
                case "STOP":
                    intermediateCode[index++] = "(IS, 00)";
                    break;
                case "DS":
                    // Ignore DS in pass 2
                    break;
                case "DC":
                    intermediateCode[index++] = "(DL, 01) (C," + parts[1] + ")";
                    break;
                case "END":
                    intermediateCode[index++] = "(AD,02)";
                    break;
                default:
                    break;
            }
        }
        
        // Add DS statements after processing other instructions
        for (String line : assemblyCode) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0) continue; // Skip empty lines
            if (parts.length > 1 && parts[1].equals("DS")) {
                intermediateCode[index++] = "(DL, 02) (C," + parts[2] + ")";
            }
        }
        
        return intermediateCode;
    }

    // Main function
    public static void main(String[] args) {
        String[] assemblyCode = {
            "START 100",
            "READ A",
            "READ B",
            "MOVER AREG, A",
            "SUB AREG, B",
            "STOP",
            "A DS 1",
            "B DS 1",
            "END"
        };

        passOne(assemblyCode);
        String[] intermediateCode = passTwo(assemblyCode);

        for (String line : intermediateCode) {
            if (line != null) {
                System.out.println(line);
            }
        }
    }
}
