package ru.vilgor.formal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import ru.vilgor.formal.FinalStateMachine;
import ru.vilgor.formal.FinalStateMachineImpl;
import ru.vilgor.formal.LexemeAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LexemeAnalyzerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CollectionType javaType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, FinalStateMachineImpl.class);

    private List<Path> fsmJsonPathList;
    {
        try {
            fsmJsonPathList = Files.walk(Paths.get("./src/test/resources")).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final List<FinalStateMachine> fsmList = new ArrayList<>();
    {
        fsmJsonPathList.forEach(path -> {
            try {
                List<FinalStateMachine> group = objectMapper.readValue(new File(path.toString()), javaType);
                group.forEach(machine -> machine.setClassName(path.toFile().getName().split("\\.")[0]));
                fsmList.addAll(group);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void analyzerTest() throws IOException {

        String javaExpression = "if (somevariable == 5) {return 7;}";

        LexemeAnalyzer scanner = new LexemeAnalyzer(fsmList);
        List<AbstractMap.SimpleEntry<String, String>> lexemeList = scanner.analyze(javaExpression);

        for (AbstractMap.SimpleEntry<String, String> lexeme : lexemeList) {
            System.out.println("Lexeme: " + lexeme.getKey());
            System.out.println("Lexeme class: " + lexeme.getValue());
            System.out.println();
        }

        Assert.assertEquals(lexemeList.get(0).getKey(), "if");
        Assert.assertEquals(lexemeList.get(0).getValue(), "keyword");

        Assert.assertEquals(lexemeList.get(1).getKey(), " ");
        Assert.assertEquals(lexemeList.get(1).getValue(), "whitespace");

        Assert.assertEquals(lexemeList.get(2).getKey(), "(");
        Assert.assertEquals(lexemeList.get(2).getValue(), "spec");

        Assert.assertEquals(lexemeList.get(3).getKey(), "somevariable");
        Assert.assertEquals(lexemeList.get(3).getValue(), "id");

        Assert.assertEquals(lexemeList.get(4).getKey(), " ");
        Assert.assertEquals(lexemeList.get(4).getValue(), "whitespace");

        Assert.assertEquals(lexemeList.get(5).getKey(), "==");
        Assert.assertEquals(lexemeList.get(5).getValue(), "operator");

        Assert.assertEquals(lexemeList.get(6).getKey(), " ");
        Assert.assertEquals(lexemeList.get(6).getValue(), "whitespace");

        Assert.assertEquals(lexemeList.get(7).getKey(), "5");
        Assert.assertEquals(lexemeList.get(7).getValue(), "number");

        Assert.assertEquals(lexemeList.get(8).getKey(), ")");
        Assert.assertEquals(lexemeList.get(8).getValue(), "spec");
    }
}
