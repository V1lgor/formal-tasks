package ru.vilgor.formal;

import java.util.AbstractMap;

public interface FinalStateMachine {
    AbstractMap.SimpleEntry<Boolean, Integer> maxString(String token, int offset);

    boolean nextState(String word);

    String getCurrentState();

    int getRank();

    void setClassName(String className);

    String getClassName();
}
