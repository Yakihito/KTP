package primes;
//Поиск и вывод всех простых чисел меньше 100.
public class Primes {
//Перебор Чисел и вывод
    public static void main(String[] args) {
    for (int n = 2;n < 100;n++){
        if(isPrime(n))
            System.out.println(n);
    }
}
//Поиск простых чисел
public static boolean isPrime(int n){
    if (n <= 3)
        return true;
    else if (n % 2 == 0 || n % 3 == 0)
        return false;
    int k = 5;
    while (Math.pow(k, 2) <= n){
        if (n % k == 0 || n % (k + 2) == 0)
            return false;
        k += 6;
    }
    return true;
} 
}
