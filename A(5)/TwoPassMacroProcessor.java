import java.util.*;

public class TwoPassMacroProcessor {
    
    private static HashMap<String, String> macroTable = new HashMap<>();
    private static ArrayList<String> intermediateCode = new ArrayList<>();
    private static int locationCounter = 0;

    // Pass 1: Populate macro table with macro definitions
    private static void passOne(String[] assemblyCode) {
        for (int i = 0; i < assemblyCode.length; i++) {
            String line = assemblyCode[i].trim();
            String[] parts = line.split("\\s+", 2);
            if (parts.length > 1 && parts[1].startsWith("MACRO")) {
                String macroName = parts[0];
                StringBuilder macroBody = new StringBuilder();
                while (!(line = assemblyCode[++i].trim()).startsWith("MEND")) {
                    macroBody.append(line).append("\n");
                }
                macroTable.put(macroName, macroBody.toString());
            } else if (parts.length > 1 && parts[1].startsWith("DC")) {
                intermediateCode.add("(DL, 01) (C," + parts[2] + ")");
            } else if (parts.length > 1 && parts[1].startsWith("DS")) {
                intermediateCode.add("(DL, 02) (C," + parts[2] + ")");
            } else {
                locationCounter++;
            }
        }
    }

    // Pass 2: Generate intermediate code with macro expansions
    private static void passTwo(String[] assemblyCode) {
        for (String line : assemblyCode) {
            String[] parts = line.trim().split("\\s+", 2);
            if (parts.length > 1 && parts[1].startsWith("MACRO")) {
                String macroName = parts[0];
                String[] arguments = parts[1].substring(parts[1].indexOf(' ') + 1).trim().split(",");
                String macroBody = macroTable.get(macroName);
                for (String arg : arguments) {
                    macroBody = macroBody.replaceAll("\\b" + arg + "\\b", "#" + arg);
                }
                Scanner scanner = new Scanner(macroBody);
                while (scanner.hasNextLine()) {
                    String macroLine = scanner.nextLine();
                    for (String arg : arguments) {
                        if (macroLine.contains("#" + arg)) {
                            macroLine = macroLine.replaceAll("#" + arg, arg);
                        }
                    }
                    intermediateCode.add(macroLine);
                }
                scanner.close();
            } else if (parts.length > 1 && parts[1].startsWith("END")) {
                intermediateCode.add("(AD, 02)");
            } else {
                switch (parts[0]) {
                    case "LOAD":
                        intermediateCode.add("(IS, 01) AREG " + parts[1]);
                        break;
                    case "STORE":
                        intermediateCode.add("(IS, 03) AREG " + parts[1]);
                        break;
                    case "ADD1":
                        intermediateCode.add("(IS, 01) AREG " + parts[1]);
                        break;
                    case "ADD5":
                        intermediateCode.add("(IS, 01) AREG " + parts[1]);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // Main function
    public static void main(String[] args) {
        String[] assemblyCode = {
            "LOAD A",
            "MACRO ABC",
            "LOAD p",
            "SUB q",
            "MEND",
            "STORE B",
            "MULT D",
            "MACRO ADD1 ARG",
            "LOAD X",
            "STORE ARG",
            "MEND",
            "LOAD B",
            "MACRO ADD5 A1, A2, A3",
            "STORE A2",
            "ADD1 5",
            "ADD1 10",
            "LOAD A1",
            "LOAD A3",
            "MEND",
            "ADD1 t",
            "ABC",
            "ADD5 D1, D2, D3",
            "END"
        };

        passOne(assemblyCode);
        passTwo(assemblyCode);

        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}
