import React from "react";
import "../../css/sidebar.css"
import Footer from "./Footer";
import Shortcut from "./Shortcut";

export default function Upbar() {
  return (
    <div className="upbar">
      <Shortcut />
      <Footer />
    </div>
  )
}