import React from "react";
import {AiFillPlusCircle} from 'react-icons/ai'
import { Link } from "react-router-dom";
import '../../css/load.css'

export default function UploadBtn() {

    return (
        <Link to="/upload" className="UploadBtn"><AiFillPlusCircle /></Link>
    )
}