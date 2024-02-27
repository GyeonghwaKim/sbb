package com.mysite.study;

import java.util.function.BiFunction;

public class Test {
    /*람다식 lambda expression
    * Java8에 추가된 기능
    * 메서드를 간략한 식으로 표현
    * 익명 함수라고도 불림
    * --------------------------
    * 함수형 인터페이스
    * 람다식 형태로 익명 클래스가 만들어질 수 있는 인터페이스
    * 조건: 추상 메소드가 하나만 있어야함
    * 람다식과 1:1 대응이 되어야 하기 때문
    * @FunctionalInterface 달아줘야햠
    */
    public static void main(String[] args) {

        Toaster toaster1=new Toaster() {
            @Override
            public void toast() {
                System.out.println("람다 사용 X");
            }
        };

        Toaster toaster2=()->{System.out.println("람다식 사용");};
        toaster2.toast();

        Param param1=new Param() {
            @Override
            public int func(int i) {
                return i*i;
            }
        };

        Param param2=(i)-> i*i;
        param2.func(1);

    }

    Runnable runnable;

    BiFunction<String,Integer,Toaster> biFunction;
}

