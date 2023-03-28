import React from "react";
import "../../css/sidebar.css"
import Filetree from "./filetree";
import Footer from "./footer";
import Shortcut from "./shortcut";
import Usage from "./usage";

export default function Sidebar(){
  return (
    <div className="sidebar">
      <Shortcut />
      <Filetree />
      <Usage />
      <Footer />
    </div>
  )
}