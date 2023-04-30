import React from "react";
import "../../css/sidebar.css"
import Footer from "./Footer";
import { Link } from "react-router-dom";

export default function SidebarCS() {
  return (
    <div className="sidebar">
      <div className="stub" />
      <div className="menuBox">
        <Link className="link" to="/cs/terms">약관</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to="/cs/faq">FAQ</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to="/cs/ask">1:1 문의</Link>
      </div>
      <div className="menuBox">
        <Link className="link" to="/cs/notice" >공지사항</Link>
      </div>
      <div className="filetree" style={{overflow:"hidden"}}/>
      <Footer />
    </div>
  )
}