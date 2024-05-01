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
                    case "SUB":
                        intermediateCode.add("(IS, 02) AREG " + parts[1]);
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
            "LOAD J",
            "STORE M",
            "MACRO EST",
            "LOAD e",
            "ADD d",
            "MEND",
            "LOAD S",
            "MACRO SUB4 ABC",
            "LOAD U",
            "STORE ABC",
            "MEND",
            "LOAD P",
            "ADD V",
            "MACRO ADD7 P4, P5, P6",
            "LOAD P5",
            "SUB4 XYZ",
            "SUB 8",
            "SUB 2",
            "STORE P4",
            "STORE P6",
            "MEND",
            "EST",
            "ADD7 C4, C5, C6",
            "SUB4 z",
            "END"
        };

        passOne(assemblyCode);
        passTwo(assemblyCode);

        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}
