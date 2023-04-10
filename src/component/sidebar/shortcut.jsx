import React from "react";
import "../../css/sidebar.css";
import { AiFillStar, AiFillHome } from 'react-icons/ai';
import { BsFillTrashFill } from 'react-icons/bs';

export default function Shortcut() {
  return (
    <div className="shortcut">
      <div className="bar">
        <AiFillHome />
        <span>Home</span>
      </div>
      <div className="bar">
        <AiFillStar />Favorites
      </div>
      <div className="bar">
        <BsFillTrashFill />Trash
      </div>
    </div>
  )
}