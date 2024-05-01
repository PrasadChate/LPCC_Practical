import java.util.*;

class MacroProcessor {
    private List<String> inputCode;
    private Map<String, List<String>> MDT;
    private Map<String, Integer> MNT;

    public MacroProcessor(List<String> inputCode) {
        this.inputCode = inputCode;
        MDT = new LinkedHashMap<>();
        MNT = new LinkedHashMap<>();
        process();
    }

    public Map<String, List<String>> getMDT() {
        return MDT;
    }

    public Map<String, Integer> getMNT() {
        return MNT;
    }

    private void process() {
        buildMDTandMNT();
    }

    private void buildMDTandMNT() {
        int index = 0;
        while (index < inputCode.size()) {
            String line = inputCode.get(index);
            if (line.startsWith("MACRO")) {
                String macroName = line.split("\\s+")[1];
                List<String> macroDefinition = new ArrayList<>();
                index++;
                while (!inputCode.get(index).equals("MEND")) {
                    macroDefinition.add(inputCode.get(index));
                    index++;
                }
                MNT.put(macroName, MDT.size());
                MDT.put(macroName, macroDefinition);
            }
            index++;
        }
    }
}

public class MNTMDTGenerator {
    public static void main(String[] args) {
        List<String> inputCode = new ArrayList<>();
        inputCode.add("LOAD J");
        inputCode.add("STORE M");
        inputCode.add("MACRO EST1");
        inputCode.add("LOAD e");
        inputCode.add("ADD d");
        inputCode.add("MEND");
        inputCode.add("MACRO EST ABC");
        inputCode.add("EST1");
        inputCode.add("STORE ABC");
        inputCode.add("MEND");
        inputCode.add("MACRO ADD7 P4, P5, P6");
        inputCode.add("LOAD P5");
        inputCode.add("EST 8");
        inputCode.add("SUB4 2");
        inputCode.add("STORE P4");
        inputCode.add("STORE P6");
        inputCode.add("MEND");
        inputCode.add("EST");
        inputCode.add("ADD7 C4, C5, C6");
        inputCode.add("END");

        MacroProcessor processor = new MacroProcessor(inputCode);
        Map<String, List<String>> MDT = processor.getMDT();
        Map<String, Integer> MNT = processor.getMNT();

        // Print MNT
        System.out.println("Macro Name Table (MNT):");
        System.out.println(String.format("| %-10s | %-10s |", "Macro Name", "Index"));
        for (Map.Entry<String, Integer> entry : MNT.entrySet()) {
            System.out.println(String.format("| %-10s | %-10d |", entry.getKey(), entry.getValue()));
        }
        System.out.println();

        // Print MDT
        System.out.println("Macro Definition Table (MDT):");
        System.out.println(String.format("| %-10s | %-20s |", "Macro Name", "Definition"));
        for (Map.Entry<String, List<String>> entry : MDT.entrySet()) {
            System.out.println(String.format("| %-10s |", entry.getKey()));
            for (String line : entry.getValue()) {
                System.out.println(String.format("|            | %-20s |", line));
            }
            System.out.println();
        }
    }
}
