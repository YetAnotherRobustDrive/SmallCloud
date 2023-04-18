import React from "react";
import '../../css/admin.css'

export default function TitledBox(props) {

  return (
    <div className="titledBox">
        <div className="head">
            <div className="icon">{props.icon}</div>
            <div className="text">{props.text}</div>
        </div>
        <div className="child">
            {props.children}
        </div>
    </div>
  )
}