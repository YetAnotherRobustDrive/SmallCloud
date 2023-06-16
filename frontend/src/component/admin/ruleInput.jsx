import React from "react";
import '../../css/admin.css';
import AdminPostConfig from "../../services/config/AdminPostConfig";
import SwalAlert from "../swal/SwalAlert";
import SwalError from "../swal/SwalError";

export default function RuleInput(props) {

    const handleKeyDown = async (e) => {
        e.stopPropagation();
        if (e.key === 'Enter') {
            e.preventDefault();
            const value = document.getElementById("value" + props.code).value;
            if (value === "") {
                SwalError("변경할 값을 입력해주세요.");
                return;
            }
            const code = props.code;
            const res = await AdminPostConfig(code, value);
            if(!res[0]){
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "변경되었습니다.", () => { props.onKeyDown(code, value) })
        }
    }

    return (
        <div onKeyDown={handleKeyDown} className="ruleInput">
            <div className="curr">
                <span>현재 {props.desc} : </span>
                <span>{props.now}</span>
            </div>
            <div className="new">
                <span>변경 {props.desc} : </span>
                <input type="number" id={"value" + props.code } placeholder=""></input>
            </div>
        </div>
    )
}