import React from "react";
import { Link } from "react-router-dom";
import "../../css/sidebar.css"

export default function Footer(){
  return (
    <div className="footer">
      <div className="links">
        <Link to="/cs/faq">고객센터</Link>
        <Link to="/cs/terms" className="middle">약관</Link>
        <Link to="/cs/notice">공지사항</Link>
      </div>
      <div className="copyright">Copyright 2023. Mint. All rights reserved.</div>
    </div>
  )
}