package ru.vilgor.formal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class FinalStateMachineImpl implements FinalStateMachine {

    private int rank;
    private final String start;
    private String finish;
    private Map<String, List<String>> inputs;
    private Map<String, Map<String, String>> matrix;
    private String currentState;
    private String className;

    @JsonCreator
    public FinalStateMachineImpl(@JsonProperty("start") final String start) {
        this.start = start;
        this.currentState = start;
    }

    @Override
    public AbstractMap.SimpleEntry<Boolean, Integer> maxString(String token, int offset) {
        this.currentState = start;
        int counter = 0;
        boolean isCorrect = true;
        for (int i = offset; i < token.length(); i++) {
            if (!nextState(String.valueOf(token.charAt(i)))) {
                break;
            }
            counter++;
        }

        if (!finish.equals(currentState)) {
            isCorrect = false;
        }

        return new AbstractMap.SimpleEntry<>(isCorrect, counter);
    }

    @Override
    public boolean nextState(String word) {
        Map<String, String> transitions = matrix.get(currentState);
        if (transitions != null) {
            for (Map.Entry<String, String> transition : transitions.entrySet()) {
                if (transition.getKey().equals(word)) {
                    currentState = transition.getValue();
                    return true;
                } else if (inputs != null) {
                    if (inputs.get(transition.getKey()).contains(word)) {
                        currentState = transition.getValue();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getCurrentState() {
        return currentState;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setClassName(String className){
        this.className = className;
    }

    @Override
    public String getClassName(){
        return className;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getStart() {
        return start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public Map<String, List<String>> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, List<String>> inputs) {
        this.inputs = inputs;
    }

    public Map<String, Map<String, String>> getMatrix() {
        return matrix;
    }

    public void setMatrix(Map<String, Map<String, String>> matrix) {
        this.matrix = matrix;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
}
