package com.example.addresssearchapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1_isCorrect() {
        String[] ar = new String[]{"aa","bb","cc"};
        List<String> list = Arrays.asList(ar);
       String result = DatabaseHelper.JoinStringList(list,"+");
        assertEquals("aa+bb+cc", result);
    }
}



