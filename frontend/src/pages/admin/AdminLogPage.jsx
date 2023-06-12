import React from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import BodyHeader from "../../component/main/BodyHeader";
import "../../css/admin.css"

export default function AdminLogPage() {

    const actionList = [
        {
            title: "인증 관련 (auth)",
            list: [
                { name: "등록", value: "/auth/register" },
                { name: "로그인", value: "/auth/login" },
                { name: "리프레시 토큰", value: "/auth/refresh" },
                { name: "특권 유저 변경", value: "/auth/elevate" },
                { name: "유저 삭제", value: "/auth/deregister" },
                { name: "특권 유저 확인", value: "/auth/privileged" },
                { name: "관리자 유저 확인", value: "/auth/admin-check" },
            ]
        },
        {
            title: "유저 관련 (users)",
            list: [
                { name: "삭제", value: "/users/delete" },
                { name: "등록", value: "/users/register" },
                { name: "프로필 업데이트", value: "/users/update" },
                { name: "프로필 조회", value: "/users/profile" },
                { name: "유저 검색", value: "/users/search" },
                { name: "패스워드 변경", value: "/users/password" },
                { name: "만료일 설정", value: "/users/update-expired" },
            ]
        },
        {
            title: "문의 관련 (inquiries)",
            list: [
                { name: "등록", value: "/inquiries" },
                { name: "전체 조회", value: "/inquiries" },
                { name: "선택", value: "/inquiries/{id}" },
                { name: "답변 등록", value: "/inquiries/{id}/answer" },
                { name: "답변 되지 않은 문의 조회", value: "/inquiries/answer" },
                { name: "내 문의 전체 조회", value: "/inquiries/my" },
                { name: "보드 등록", value: "/inquiries/{id}/board" },
                { name: "보드(faq, 공지사항) 전체 조회", value: "/inquiries/{id}/board" },
                { name: "보드(terms, privacy) 조회", value: "/inquiries/{id}/board" },
            ]
        },
        {
            title: "디렉토리 관련 (directories)",
            list: [
                { name: "등록", value: "/directories" },
                { name: "이름 바꾸기", value: "/directories/{id}" },
                { name: "이동", value: "/directories/{id}" },
                { name: "정보", value: "/directories/{id}" },
                { name: "자식 폴더", value: "/directories/{id}/subDirectories" },
                { name: "자식 파일", value: "/directories/{id}/subFiles" },
                { name: "완전삭제", value: "/directories/{id}/purge" },
                { name: "삭제", value: "/directories/{id}" },
                { name: "복구", value: "/directories/{id}/restore" },
                { name: "즐겨찾기 추가", value: "/directories/{id}/favorite" },
                { name: "즐겨찾기 삭제", value: "/directories/{id}/unfavorite" },
                { name: "폴더 검색", value: "/directories/search" },
            ]
        },
        {
            title: "공유 관련 (share)",
            list: [
                { name: "등록", value: "/share" },
                { name: "삭제", value: "/share/{id}" },
                { name: "파일 리스트", value: "/share/{id}/file-list" },
                { name: "폴더 리스트", value: "/share/{id}/directory-list" },
            ]
        },
        {
            title: "파일 관련 (files)",
            list: [
                { name: "파일 라벨 업데이트", value: "/files/{id}/labelUpdate" },
                { name: "파일 삭제", value: "/files/{id}" },
                { name: "파일 복구", value: "/files/{id}/restore" },
                { name: "파일 이동", value: "/files/{id}" },
                { name: "파일 완전 삭제", value: "/files/{id}/purge" },
                { name: "즐겨찾기 추가", value: "/files/{id}/favorite" },
                { name: "즐겨찾기 삭제", value: "/files/{id}/unfavorite" },
                { name: "파일 검색", value: "/files/search" },
            ]
        },
        {
            title: "그룹 관련 (group)",
            list: [
                { name: "그룹 만들기", value: "/group" },
                { name: "그룹 삭제", value: "/group/{id}" },
                { name: "그룹원 추가", value: "/group/{id}/add" },
                { name: "그룹 업데이트", value: "/group/{id}" },
                { name: "그룹원 삭제", value: "/group/{id}/remove" },
                { name: "그룹 트리", value: "/group/tree" },
                { name: "그룹원 목록", value: "/group/{id}/members" },
                { name: "그룹 검색", value: "/group/search" },
            ]
        },
        {
            title: "관리자 관련 (admin)",
            list: [
                { name: "유저 잠금", value: "/admin/lock" },
                { name: "유저 잠금", value: "/admin/unlock" },
                { name: "유저 패스워드 변경", value: "/admin/password" },
            ]
        },
        {
            title: "라벨 관련 (label)",
            list: [
                { name: "라벨 검색", value: "/label/search" },
                { name: "휴지통 라벨 검색", value: "/label/trash" },
                { name: "즐겨찾기 라벨 검색", value: "/label/favorite" },
            ]
        },
    ]

    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text="시스템로그 확인" />
                <form className="logOptionSelect">
                    <span className="title" htmlFor="action">종류 및 결과</span>
                    <div className="actionSelect">
                        <select className="action" name="action">
                            <option value="all">전체</option>
                            {actionList.map((action, index) => (
                                <optgroup key={index} label={action.title}>
                                    {action.list.map((item, index) => (
                                        <option key={index} value={item.value}>{item.name}</option>
                                    ))}
                                </optgroup>
                            ))}
                        </select>
                        <select className="status" name="status">
                            <option value="all">전체</option>
                            <option value="success">성공</option>
                            <option value="fail">실패</option>
                        </select>
                    </div>
                    <span className="title">시간</span>
                    <div className="timeSelect">
                        <input className="start" type="datetime-local" name="start" />
                        <span className="between">~</span>
                        <input className="end" type="datetime-local" name="end" />
                    </div>
                    <span className="title">닉네임</span>
                    <div className="userSelect" >
                        <input className="user" type="text" name="user" placeholder="사용자 닉네임을 입력하세요. (입력하지 않으면 전체 검색)"/>
                    </div>
                    <div className="searchButton">
                        <button type="reset">초기화</button>
                        <button type="submit">검색</button>
                    </div>
                </form>

            </BodyFrame>
        </>
    )
}
