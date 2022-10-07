package com.soufiane.gestiondestock;

import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class compute {

    public static String compute(String digits) {
        String result = "";

        int digits_ = Integer.valueOf(digits);

        if (digits_ % 3 == 0) {
            result = result.concat("Foo");
        }
        if (digits_ % 5 == 0) {
            result = result.concat("Bar");
        }
        if (digits_ % 7 == 0) {
            result = result.concat("Qix");
        }

        for (int i=0; i < digits.length(); i++) {
            char value = digits.charAt(i);

            if (value == '3') {
                result = result.concat("Foo");
            }

            if (value == '5') {
                System.out.println("equal 5");
                result = result.concat("Bar");
            }

            if (value == '7') {
                result = result.concat("Qix");
            }
        }

        if (result.equals("")){
            return digits;
        }else{
            return result;
        }

    }

    public static int[] findSumPair(int[] numbers, int k) {
        int[] results= new int[2];
        List<Integer> myList = new ArrayList<>();
        for (int i=0; i< numbers.length - 1; i++) {
            for (int j=i+1; j< numbers.length; j++) {
                if (numbers [i] + numbers[j] == k) {
                    System.out.println("index 1 : "+numbers[i]);
                    System.out.println("index 2 : "+numbers [j]);
                    int temp_number_i = numbers[i];
                    System.out.println("--- : "+IntStream.range(0, numbers.length).filter(n -> numbers[n] == temp_number_i).findFirst().orElse(-1));
                    results[0] = i;
                    results[1] = j;
                    System.out.println(results[0]);
                    System.out.println(results.length);
                }
            }
        }
        return results;
    }


    public static void main(String[] args) {

////        System.out.println("result : " + compute.compute("1"));
//        int[] nums = { 1, 5, 8, 1, 2 };
//        int target = 13;
//        int[] result = compute.findSumPair(nums, target);
//        System.out.println("result : " + Arrays.toString(result));
        int i=0;
        System.out.println(2>>1);
    }

}


