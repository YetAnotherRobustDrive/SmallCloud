import React, { useState } from "react";
import "../../css/header.css"
import { useNavigate } from "react-router-dom"
import { BiSearch } from "react-icons/bi"

export default function SearchBar() {
    const navigate = useNavigate();

    const handleKeyDown = (e) => {
        if (e.key === "Enter") {
            if (e.target.value === "") {
                return;
            }
            const encoded = encodeURIComponent(e.target.value);
            navigate("/search?q=" + encoded);
        }
        else if (e.key === "Escape") {
            e.target.value = "";
        }
    }


    return (
        <div className="searchbar">
            <div>
                <input type="text" placeholder="검색어를 입력하세요..."
                    onKeyDown={handleKeyDown}
                />
                <BiSearch />
            </div>
        </div>
    )

} 