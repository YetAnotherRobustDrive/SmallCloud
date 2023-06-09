import React from "react";
import '../../css/bodyframe.css'
import { useNavigate } from "react-router-dom"
import CustomIcon from "./CustomIcon";

export default function NarrowBox(props) {
    const navigate = useNavigate();

    const datas = [
        <CustomIcon
            onClick={() => {
                navigate("/share");
            }}
            data={
                {
                    id: 99,
                    name: "전체보기",
                    type: "special"
                }
            }
            key={99}
            noContext={true}/>,
        ...props.children]
    return (
        <div className="narrow" onContextMenu={e => { e.stopPropagation(); e.preventDefault(); }}>
            {datas}
        </div>
    )
}