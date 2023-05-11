import React from "react";
import "../../css/sidebar.css"
import Footer from "./Footer";
import { Link } from "react-router-dom";

export default function SidebarMypage() {
  return (
    <div className="sidebar">
      <div className="stub" />
      <div className="menuBox">
        <Link className="link" to='/mypage'>개인정보</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/mypage/service'>서비스정보</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to='/mypage/security'>보안정보</Link>
      </div>
      <div className="filetreeStub" />
      <Footer />
    </div>
  )
}