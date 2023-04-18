import React from "react";
import { Link } from "react-router-dom";
import "../../css/header.css"
import AlertAndMy from "./alertAndMy";
import Logo from "./logo";
import SearchBar from "./searchBar";

export default function Header() {

  return (
    <div className="header">
      <Link to='/' style={{textDecoration: "none", color:"black"}}>
        <Logo />
      </Link>
      <SearchBar />
      <AlertAndMy />
    </div>
  )
}