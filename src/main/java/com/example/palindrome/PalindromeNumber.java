package com.example.palindrome;

public class PalindromeNumber {

    public static void main(String[] args) {
        System.out.println(isPalindrome(12345));
        System.out.println(isPalindrome(1111));
        System.out.println(isPalindrome(1230));
        System.out.println(isPalindrome(0));
        System.out.println(isPalindrome(77577));
    }

    public static boolean isPalindrome(int x) {
        //if ending in 0, can't be palindrome
        if ((x % 10) == 0 && x != 0) {
            return false;
        }

        //if negative, can't be palindrome
        if (x < 0 ) {
            return false;
        }

        int y = x;
        int reversed = 0;

        //stripping away last digit from y, adding it to first digit in reversed int
        while(y > 0) {
            System.out.println("Current temp value = " + y);

            //current val of reversed times 10 (give new digit spot), plus remainder of y
            reversed = reversed * 10 + (y % 10);

            //stripping away last digit, until zero
            y = y / 10;
        }

        //compare our reversed str to original
        System.out.println("Reversed value = " + reversed);
        return reversed == x;
    }
    
}
