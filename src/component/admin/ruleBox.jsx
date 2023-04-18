import React from "react";
import '../../css/admin.css'

export default function RuleBox(props) {

  return (
    <div className="rulebox">
        <div className="texts">
            <div className="title">{props.title}</div>
            <div className="desc">{props.desc}</div>
        </div>
        <div className="interac">
            {props.children}
        </div>
    </div>
  )
}