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
        inputCode.add("LOAD A");
        inputCode.add("STORE B");
        inputCode.add("MACRO ABC");
        inputCode.add("LOAD p");
        inputCode.add("SUB q");
        inputCode.add("MEND");
        inputCode.add("MACRO ADD1 ARG");
        inputCode.add("LOAD X");
        inputCode.add("STORE ARG");
        inputCode.add("MEND");
        inputCode.add("MACRO ADD5 A1, A2, A3");
        inputCode.add("STORE A2");
        inputCode.add("ADD1 5");
        inputCode.add("ADD1 10");
        inputCode.add("LOAD A1");
        inputCode.add("LOAD A3");
        inputCode.add("MEND");
        inputCode.add("ABC");
        inputCode.add("ADD5 D1, D2, D3");
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
