import React from "react";
import "../../css/header.css";
import MypageDropdown from "../dropdown/MyPageDropdown";
import AlertDropdown from "../dropdown/AlertDropdown";

export default function AlertAndMy() {
  return (
    <div className="two">
      <div>
        <AlertDropdown/>
      </div>
      <div>
        <MypageDropdown/>
      </div>
    </div>
  )
}