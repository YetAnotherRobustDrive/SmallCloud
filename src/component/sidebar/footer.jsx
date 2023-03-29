import React from "react";
import "../../css/sidebar.css"

export default function Footer(){
  return (
    <div className="footer">
      <div className="links">
        <a href="">고객센터</a>
        <a className="middle" href="">약관</a>
        <a href="">공지사항</a>
      </div>
      <div className="copyright">Copyright 2023. Mint. All rights reserved.</div>
    </div>
  )
}