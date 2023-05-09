import React from "react";
import "../../css/sidebar.css"
import Footer from "./Footer";
import { Link } from "react-router-dom";

export default function SidebarMypage() {
  return (
    <div className="sidebar">
      <div className="stub" />
      <div className="menuBox">
        <Link className="link">개인정보</Link>
      </div>
      <div className="menuBox">
        <Link className="link">서비스정보</Link>
      </div>
      <div className="menuBox">
        <Link className="link">보안정보</Link>
      </div>
      <div className="filetreeStub" />
      <Footer />
    </div>
  )
}