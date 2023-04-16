import React from "react";
import "../../css/sidebar.css"
import Footer from "./footer";
import { Link } from "react-router-dom";

export default function SidebarAdmin() {
  return (
    <div className="sidebar">
      <div className="stub" />
      <div className="menuBox">
        <Link className="link" to='/admin/rules'>정책변경</Link>
      </div>
      <div className="menuBox">
        <Link className="link">개인정보 취급 방침 변경</Link>
      </div>
      <div className="menuBox">
        <Link className="link">약관 변경</Link>
      </div>
      <div className="menuBox">
        <Link className="link">조직도 구성</Link>
      </div>
      <div className="menuBox">
        <Link className="link">사용자 관리</Link>
      </div>
      <div className="menuBox">
        <Link className="link">시스템 로그</Link>
      </div>
      <div className="menuBox">
        <Link className="link">보안 관리</Link>
      </div>
      <div className="filetree" />
      <Footer />
    </div>
  )
}