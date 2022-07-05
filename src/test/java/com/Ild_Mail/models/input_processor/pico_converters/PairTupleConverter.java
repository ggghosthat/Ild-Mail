package com.Ild_Mail.models.input_processor.pico_converters;

import org.javatuples.Pair;
import picocli.CommandLine;

public class PairTupleConverter implements CommandLine.ITypeConverter<Pair<Integer, Integer>> {

    @Override
    public Pair<Integer, Integer> convert(String s) throws Exception {
        String [] splited = s.replace("(","")
                             .replace(")","")
                             .split(",");
        int a = Integer.valueOf(splited[0]);
        int b = Integer.valueOf(splited[1]);

        return new Pair<Integer, Integer>(a,b);
    }
}
