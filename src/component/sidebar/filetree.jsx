import React from "react";
import "../../css/sidebar.css"
import {BsFileEarmarkTextFill, BsFolderFill} from "react-icons/bs"

export default function Filetree(){
  return (
    <div className="filetree">
      <div>파일트리 출력 예정</div>
      <BsFileEarmarkTextFill/>
      <BsFolderFill />
    </div>
  )
}