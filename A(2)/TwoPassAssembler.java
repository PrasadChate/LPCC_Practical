import java.util.*;

public class TwoPassAssembler {
    private Map<String, Integer> literalTable = new LinkedHashMap<>();
    private List<String> literalsList = new ArrayList<>();
    private int locationCounter;

    public TwoPassAssembler() {
        this.locationCounter = 0;
    }

    public void firstPass(String[] sourceCode) {
        for (String line : sourceCode) {
            String[] parts = line.trim().split("\\s+");
            if (parts[0].equals("START")) {
                locationCounter = Integer.parseInt(parts[1]);
            } else if (parts[0].equals("END")) {
                processLiterals();
                break;
            } else {
                for (String part : parts) {
                    if (part.startsWith("='")) {
                        literalsList.add(part.substring(2, part.length() - 1));
                    }
                }
                locationCounter++;
            }
        }
    }

    private void processLiterals() {
        for (String literal : literalsList) {
            if (!literalTable.containsKey(literal)) {
                literalTable.put(literal, locationCounter);
                locationCounter++;
            }
        }
    }

    public void printLiteralTable() {
        System.out.println("Literal Table:");
        System.out.println("Literal\tAddress");
        literalTable.forEach((key, value) -> System.out.println("'" + key + "'\t" + value));
    }

    public static void main(String[] args) {
        String[] sourceCode = {
            "START 100",
            "READ A",
            "READ B",
            "MOVER AREG, ='50'",
            "MOVER BREG, ='60'",
            "ADD AREG, BREG",
            "LOOP MOVER CREG, A",
            "ADD CREG, ='10'",
            "COMP CREG, B",
            "BC LT, LOOP",
            "NEXT SUB AREG, ='10'",
            "COMP AREG, B",
            "BC GT, NEXT",
            "STOP",
            "A DS 1",
            "B DS 1",
            "END"
        };

        TwoPassAssembler assembler = new TwoPassAssembler();
        assembler.firstPass(sourceCode);
        assembler.printLiteralTable();
    }
}
