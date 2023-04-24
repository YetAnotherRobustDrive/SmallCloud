import React from "react";
import "../../css/sidebar.css";
import { AiFillStar, AiFillHome } from 'react-icons/ai';
import { BsFillTrashFill } from 'react-icons/bs';
import { Link } from "react-router-dom";

export default function Shortcut() {
  return (
    <div className="shortcut">
      <div className="bar">
        <Link to='/' style={{ textDecoration: "none", color: "black" }}>
          <AiFillHome />
          <span>Home</span>
        </Link>
      </div>
      <div className="bar">
        <AiFillStar />
        <span>Favorites</span>
      </div>
      <div className="bar">
        <BsFillTrashFill />
        <span>Trash</span>
      </div>
    </div>
  )
}