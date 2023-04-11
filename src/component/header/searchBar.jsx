import React, { useState } from "react";
import "../../css/header.css"
import {BiSearch} from "react-icons/bi"

export default function SearchBar(){

    return (
        <div className="searchbar">
            <div>
                <input type="text" placeholder="검색어를 입력하세요..."/>
                <BiSearch />
            </div>
        </div>
    )

} 