package aho.uozu;

import java.util.ArrayList;
import java.util.List;

public class ConsoleOutputMock implements ConsoleOutput {

    private final List<String> lines;

    ConsoleOutputMock() {
        lines = new ArrayList<>();
    }

    @Override
    public void writeLine(String value) {
        lines.add(value);
    }

    public String getOutputLine(int lineNumber) {
        return lines.get(lineNumber);
    }
}
