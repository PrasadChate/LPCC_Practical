import java.util.*;

class Assembler {
    private List<String[]> literalTable;
    private List<Integer> poolTable;
    private int locationCounter;
    private int poolStart;

    public Assembler() {
        literalTable = new ArrayList<>();
        poolTable = new ArrayList<>();
        locationCounter = 0;
        poolStart = 0;
    }

    public void firstPass(List<String> sourceCode) {
        for (String line : sourceCode) {
            line = line.trim();
            if (line.startsWith("START")) {
                locationCounter = Integer.parseInt(line.split("\\s+")[1]);
            } else if (line.startsWith("END")) {
                processLTORG();
                break;
            } else if (line.contains("='")) {
                processLiteral(line);
            } else if (line.startsWith("LTORG")) {
                processLTORG();
            } else {
                processInstruction(line);
            }
        }
    }

    public void processInstruction(String line) {
        if (!line.startsWith("DS") && !line.startsWith("LTORG")) {
            locationCounter++;
        }
    }

    public void processLiteral(String line) {
        String literal = line.split("='")[1].split("'")[0];
        literalTable.add(new String[]{literal, ""});
    }

    public void processLTORG() {
        for (int i = poolStart; i < literalTable.size(); i++) {
            String[] literal = literalTable.get(i);
            literal[1] = String.valueOf(locationCounter++);
        }
        poolTable.add(poolStart);
        poolStart = literalTable.size();
    }

    public void printPoolTable() {
        System.out.println("Pool Table:");
        for (int address : poolTable) {
            System.out.println(address);
        }
    }

    public void assemble(List<String> sourceCode) {
        firstPass(sourceCode);
        printPoolTable();
    }
}

public class Main {
    public static void main(String[] args) {
        List<String> sourceCode = Arrays.asList(
                "START 100",
                "READ A",
                "MOVER AREG, ='1'",
                "MOVEM AREG, B",
                "MOVER BREG, ='6'",
                "ADD AREG, BREG",
                "COMP AREG, A",
                "BC GT, LAST",
                "LTORG",
                "NEXT SUB AREG, ='1'",
                "MOVER CREG, B",
                "ADD CREG, ='8'",
                "MOVEM CREG, B",
                "PRINT B",
                "LAST STOP",
                "A DS 1",
                "B DS 1",
                "END");

        Assembler assembler = new Assembler();
        assembler.assemble(sourceCode);
    }
}
