import React from "react";
import "../../css/sidebar.css"
import Footer from "./Footer";
import { Link } from "react-router-dom";

export default function SidebarAdmin() {
  return (
    <div className="sidebar">
      <div className="stub" />
      <div className="menuBox">
        <Link className="link" to='/admin/questions'>문의 답변</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/rules'>정책 변경</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/privacy'>개인정보취급방침 변경</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/term'>약관 변경</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/notice'>공지 등록</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/faq'>FAQ 등록</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to="/admin/group">조직도 구성</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/user'>사용자 관리</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/user/register'>사용자 등록</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/admin/log' >시스템 로그</Link>
      </div>
      <div className="filetreeStub" />
      <Footer />
    </div>
  )
}