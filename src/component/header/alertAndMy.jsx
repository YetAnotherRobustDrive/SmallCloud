import React from "react";
import "../../css/header.css";
import {BsBell, BsPersonCircle} from "react-icons/bs"

export default function AlertAndMy() {
  return (
    <div className="two">
      <div>
        <BsBell/>
      </div>
      <div>
        <BsPersonCircle/>
      </div>
    </div>
  )
}