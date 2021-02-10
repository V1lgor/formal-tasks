package ru.vilgor.formal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import ru.vilgor.formal.FinalStateMachine;
import ru.vilgor.formal.FinalStateMachineImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

public class FinalStateMachineTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CollectionType javaType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, FinalStateMachineImpl.class);

    @Test
    public void testMachine() throws IOException {

        List<FinalStateMachine> keywordFsmList = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine beginFsm = keywordFsmList.get(0);
        beginFsm.nextState("b");
        beginFsm.nextState("e");
        beginFsm.nextState("g");

        Assert.assertEquals(beginFsm.getCurrentState(), "q3");
    }

    @Test
    public void testMachineInputs() throws IOException {

        List<FinalStateMachine> numberFsmList = objectMapper.readValue(new File("./src/test/resources/number.json"), javaType);
        FinalStateMachine numberFsm = numberFsmList.get(0);
        numberFsm.nextState("+");
        numberFsm.nextState("1");
        numberFsm.nextState("2");

        Assert.assertEquals(numberFsm.getCurrentState(), "q1");
    }

    @Test
    public void testMaxString() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        boolean found = keyword.maxString("begin", 0).getKey();

        Assert.assertEquals(keyword.getCurrentState(), "q5");
        Assert.assertTrue(found);
    }

    @Test
    public void testMaxStringFail() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        boolean found = keyword.maxString("beg", 0).getKey();

        Assert.assertEquals(keyword.getCurrentState(), "q3");
        Assert.assertFalse(found);
    }

    @Test
    public void longerLineTest() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        AbstractMap.SimpleEntry<Boolean, Integer> result = keyword.maxString("begin and something more", 0);

        int pos = result.getValue();
        Assert.assertTrue(result.getKey());
        Assert.assertEquals(pos, 5);
    }

    @Test
    public void offsetTest() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/keyword.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        AbstractMap.SimpleEntry<Boolean, Integer> result = keyword.maxString("offset begin", 7);

        Assert.assertTrue(result.getKey());
    }

    @Test
    public void offsetWhiteSpaceTest() throws IOException {

        List<FinalStateMachine> keywords = objectMapper.readValue(new File("./src/test/resources/whitespace.json"), javaType);
        FinalStateMachine keyword = keywords.get(0);

        AbstractMap.SimpleEntry<Boolean, Integer> result = keyword.maxString("k ", 1);

        Assert.assertTrue(result.getKey());
    }
}
