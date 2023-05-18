package org.mint.smallcloud.board.domain;


public enum BoardType {

    /**
     * question - 질문내용(user 정보있으면 1:1문의 없으면 그냥 문의)
     * answer - question 에 대한 답변
     * faq - 테이블 필요한가(프론트에서 하면 안댐?)
     * announcement - 공지사항
     * terms - 약관(가장 최근꺼 2개 가져와서 변경내역 보여줘야함)
     * privacy - 개인정보처리방침(얘도 2개 가져와야 함)
     */

        question, answer, faq, announcement, terms, privacy


}
