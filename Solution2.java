package src.hamster.Amstrong;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.IntStream;


public class Solution2 {
        public static long[] getNumbers(long N) {
            if(N <= 1) return new long[0];
            TreeSet<Long> list = new TreeSet<>();
            int length = String.valueOf(N).length();
            long [][] degreesArray  = fillArrayDegrees(length);//was tested
            int[] arrayDigits = fillArrayNines(length);//was tested
            do {
                long message = checkDigits(arrayDigits, degreesArray);
                if(message != 0 && message < N) list.add(message);
                else {
                    int[]arrayCopy = arrayDigits.clone();
                    while (IntStream.of(arrayCopy).anyMatch(x -> x == 0)) {
                        arrayCopy = IntStream.of(arrayCopy).skip(1).toArray();
                        long message2 = checkDigits(arrayCopy, degreesArray);
                        if(message2 != 0 && message2 < N) {
                            list.add(message2);
                            break;
                        }
                    }
                }
            }
            while (decrementArray(arrayDigits));
            return list.stream().mapToLong(i -> i).toArray();
        }

        /*
        Попробуйте повысить быстродействие алгоритма. Для этого прежде всего, на мой взгляд, следует
        оптимизировать код метода checkDigits(): вместо двух сортировок массивов и их последующего
        сравнения методом equals() эффективнее будет вычислить степенную сумму для массива resultArrayDigits
        и сравнить полученное значение со значением result. Также неплохо было бы избавиться от стрима,
        преобразующего число result в массив. Само преобразование можно произвести при помощи цикла,
        последовательно деля исходное число на 10 и перемещаясь таким образом по его разрядам.
         */
        public static long checkDigits(int[] arrayDigits, long[][] degreesArray) {
            long result1 = 0;
            int degree = arrayDigits.length;//135
            for(int x : arrayDigits) {
                result1 += degreesArray[x][degree - 1];
            }
            if(String.valueOf(result1).length() != degree || result1 < 0) return 0;//153
            long copyResult = result1;
            long result2 = 0;
            int[] resultArrayDigits = new int[degree];//153

            for(int i = resultArrayDigits.length - 1; i > - 1 ; i--) {//заполняем массив 153
                resultArrayDigits[i] = (int) (copyResult % 10);
                copyResult /= 10;
            }
        try {
            for(int y : resultArrayDigits) {//проверяем сумму степеней 153
                result2 += degreesArray[y][degree - 1];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(Arrays.toString(arrayDigits));
            System.out.println(Arrays.toString(resultArrayDigits));
            System.exit(0);
        }
            if(result1 == result2) return result2;
            else return 0;
        }

        public static boolean decrementArray(int[] array) {
            int index = 0;
            while (index < array.length && array[index] == 0) {// пока индекс меньше длины массива и определенный индекс равен нулю
                index++;
            }
            if(index + 1 == array.length && array[index] == 1){//когда достигнем 0 0 1
                return  false;
            }
            Arrays.fill(array, 0, index + 1, array[index] - 1);
            //System.out.println(Arrays.toString(array));
            return true;
        }

        public static int[] fillArrayNines(int x) {
            int[] arrayDigits = new int[x];
            for(int i = 0; i < x; i ++) {
                arrayDigits[i] = 9;
            }
            //System.out.println(Arrays.toString(arrayDigits));
            return arrayDigits;
        }
        public static long[][] fillArrayDegrees(int length) {
            long [][] arrayDegrees = new long[10][length];
            for(int i = 0; i < 10; i ++) {
                for(int j = 0; j < length; j++) {
                    int degree = 1 + j;
                    long result = i;
                    for (int a = 1; a < degree; a++) {//используем цикл потому что Math.pow() некорректно работает с большими числами
                        result *= i;
                    }
                    arrayDegrees[i][j] = result;
                }
            }
            //for(long[] x : arrayDegrees) System.out.println(Arrays.toString(x));
            return arrayDegrees;
        }

        public static void main(String[] args) {
            long a = System.currentTimeMillis();
            System.out.println(Arrays.toString(getNumbers(1000)));
            long b = System.currentTimeMillis();
            System.out.println("memory " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (8 * 1024));
            System.out.println("time = " + (b - a) / 1000);

            a = System.currentTimeMillis();
            System.out.println(Arrays.toString(getNumbers(Long.MAX_VALUE)));
            b = System.currentTimeMillis();
            System.out.println("memory " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (8 * 1024));
            System.out.println("time = " + (b - a) / 1000);
        }
    }
