import React from "react";
import { Link } from "react-router-dom";
import "../../css/header.css"
import AlertAndMy from "./AlertAndMy";
import Logo from "./Logo";
import SearchBar from "./SearchBar";

export default function Header(props) {

  return (
    <div className="header">
      <Link to='/' style={{textDecoration: "none", color:"black"}}>
        <Logo width={props.innerWidth}/>
      </Link>
      <SearchBar />
      <AlertAndMy />
    </div>
  )
}