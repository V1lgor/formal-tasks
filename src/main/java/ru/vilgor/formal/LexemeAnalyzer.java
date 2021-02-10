package ru.vilgor.formal;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Класс лексического анализатора
public class LexemeAnalyzer {

    // Список автоматов-распознавателей лексем
    private List<FinalStateMachine> fsmList;

    public LexemeAnalyzer(List<FinalStateMachine> fsmList) {
        this.fsmList = fsmList;
    }

    // Разбирает заданную строку на лексемы
    // Возвращает список пар вида <КлассЛексемы, Лексема>
    public List<AbstractMap.SimpleEntry<String, String>> analyze(String str) {
        List<AbstractMap.SimpleEntry<String, String>> lexemeList = new ArrayList<>();

        int offset = 0; // Смещение относительно начала строки

        String resultLexeme = "";   //
        int lexemeOffset = 0;
        int lexemeRank = Integer.MAX_VALUE;
        String lexemeClass = "";

        // Пока не дошли до конца строки
        while (offset < str.length()) {
            // Идем по каждому автомату
            for (FinalStateMachine fsm : fsmList) {
                // И пытаемся заставить его распознать лексему
                Map.Entry<Boolean, Integer> result = fsm.maxString(str, offset);

                // Если текущий автомат распознал лексему
                if (result.getKey()) {
                    // Вытаскиваем эту лексему из строки
                    String lexeme = str.substring(offset, offset + result.getValue());

                    // Если она длиннее, чем запомненная лексема, запоминаем все ее характеристики
                    if (lexeme.length() > resultLexeme.length()) {
                        resultLexeme = lexeme;
                        lexemeRank = fsm.getRank();
                        lexemeOffset = result.getValue();
                        lexemeClass = fsm.getClassName();
                    } // А если их длины совпадают, выбираем ту, приоритет которой выше
                    else if (resultLexeme.length() == lexeme.length() && lexemeRank > fsm.getRank()) {
                        resultLexeme = lexeme;
                        lexemeRank = fsm.getRank();
                        lexemeOffset = result.getValue();
                        lexemeClass = fsm.getClassName();
                    }
                }
            }
            lexemeList.add(new AbstractMap.SimpleEntry<>(resultLexeme, lexemeClass));
            offset += lexemeOffset;
            resultLexeme = "";
            lexemeRank = Integer.MAX_VALUE;
        }

        return lexemeList;
    }
}
