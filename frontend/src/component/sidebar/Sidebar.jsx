import React from "react";
import "../../css/sidebar.css"
import Filetree from "./Filetree";
import Footer from "./Footer";
import Shortcut from "./Shortcut";
import Usage from "./Usage";

export default function Sidebar() {
  return (
    <div className="sidebar">
      <div className="stub" />
      <Shortcut />
      <Filetree />
      <Usage />
      <Footer />
    </div>
  )
}