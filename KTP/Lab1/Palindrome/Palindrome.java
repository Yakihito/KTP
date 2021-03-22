package palindrome;
import java.util.Scanner;
//Является ли слово палиндромом
public class Palindrome {
    //Ввод слов
    public static void main(String args[]) {
        String[] inputs = new String[6];
        Scanner scanner = new Scanner(System.in);
        for(int i=0; i<6; i++) {
        String title = scanner.nextLine();
        System.out.println(isPalindrome(title));
        }
    }
    //Переворот слова в обратную сторону
      public static String reverseString(String a) {
        int j = a.length();
        char[] newWord = new char[j];
        for (int i = 0; i < a.length(); i++) {
            newWord[--j] = a.charAt(i);
        }
        return new String(newWord);
    }
      //Сравнение слов
      public static boolean isPalindrome(String s) {
        if (reverseString(s).equals(s)){
        System.out.println("Слово является палидромом");
        return true;
        }
        else{
        System.out.println("Слово не является палидромом");
        }
        return false;
      }
}

