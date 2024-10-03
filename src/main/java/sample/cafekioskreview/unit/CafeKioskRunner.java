package sample.cafekioskreview.unit;

import sample.cafekioskreview.unit.beverage.Americano;
import sample.cafekioskreview.unit.beverage.Latte;

public class CafeKioskRunner {

    public static void main(String[] args) {
        // 요구 사항
        // 1. 주문 목록에 음료 추가/삭제 기능
        // 2. 주문 목록 전체 지우기
        // 3. 주문 목록 총 금액 계산하기
        // 4. 주문 생성하기
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println(">>> 아메리카노 추가");

        cafeKiosk.add(new Latte());
        System.out.println(">>> 라떼 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문가격 :" + totalPrice);

    }
}
