import java.util.*;

public class TwoPassAssembler {
    private Map<String, Integer> symbolTable;
    private int locationCounter;

    public TwoPassAssembler() {
        symbolTable = new HashMap<>();
        locationCounter = 0;
    }

    public void firstPass(List<String> sourceCode) {
        for (String line : sourceCode) {
            line = line.trim();
            if (line.startsWith("START")) {
                locationCounter = Integer.parseInt(line.split("\\s+")[1]);
            } else if (line.startsWith("END")) {
                break;
            } else if (!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                if (parts.length > 1 && !isInstruction(parts[0])) {
                    processLabel(line);
                }
                if (isInstruction(parts[0]) || parts[1].equals("DS")) {
                    locationCounter++;
                }
            }
        }
    }

    public boolean isInstruction(String word) {
        // List of all instruction mnemonics
        Set<String> instructions = new HashSet<>(Arrays.asList(
            "START", "END", "READ", "MOVER", "ADD", "SUB", "COMP", "BC", "STOP", "DS"
        ));
        return instructions.contains(word);
    }

    public void processLabel(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length > 1 && !isInstruction(parts[0])) {
            String label = parts[0];
            symbolTable.put(label, locationCounter);
        }
    }

    public void generateSymbolTable() {
        System.out.println("Symbol Table:");
        System.out.println("Label\tAddress");
        for (Map.Entry<String, Integer> entry : symbolTable.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    public void assemble(List<String> sourceCode) {
        firstPass(sourceCode);
        generateSymbolTable();
    }

    public static void main(String[] args) {
        List<String> sourceCode = Arrays.asList(
                "START 180",
                "READ M",
                "READ N",
                "LOOP MOVER AREG, M",
                "MOVER BREG, N",
                "COMP BREG, ='200'",
                "BC GT, LOOP",
                "BACK SUB AREG, M",
                "COMP AREG, ='500'",
                "BC LT, BACK",
                "STOP",
                "M DS 1",
                "N DS 1",
                "END");

        TwoPassAssembler assembler = new TwoPassAssembler();
        assembler.assemble(sourceCode);
    }
}