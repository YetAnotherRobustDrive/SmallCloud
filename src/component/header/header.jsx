import React from "react";
import "../../css/header.css"
import AlertAndMy from "./alertAndMy";
import Logo from "./logo";
import SearchBar from "./searchBar";

export default function Header(){
  return (
    <div className="header">
        <Logo/>
        <SearchBar />
        <AlertAndMy />
    </div>
  )
}