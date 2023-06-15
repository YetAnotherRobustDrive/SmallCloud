import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import '../../css/context.css';
import GetRootDir from "../../services/directory/GetRootDir";
import PostNewDir from "../../services/directory/PostNewDir";
import ModalGetString from "../modal/ModalGetString";
import SwalError from "../swal/SwalError";
import SwalAlert from "../swal/SwalAlert";

export default function ContextBody() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newName, setNewName] = useState();
    const params = useParams();

    useEffect(() => {
        const mkFile = async () => {
            let curr = params.fileID;
            if (curr === undefined) {     
                const rootIDRes = await GetRootDir();
                if (!rootIDRes[0]) {
                    SwalError(rootIDRes[1]);
                    return;
                }    
                curr = rootIDRes[1];
            }
            const res = await PostNewDir(curr, newName);
            if (!res[0]) {
                SwalError(res[1]);
                return;               
            }
            SwalAlert("success", "폴더가 생성되었습니다.", () => window.location.reload());
        }
        if (newName !== undefined && newName !== "") {
            mkFile();
        }
    }, [newName])

    const options = [
        { label: "폴더 생성", onClick: () => setIsModalOpen(true) },
    ];

    return (
        <>
            {isModalOpen &&
                <ModalGetString
                    title={"폴더 생성"}
                    placeholder={"폴더 이름"}
                    setter={setNewName}
                    isOpen={isModalOpen}
                    after={() => setIsModalOpen(false)}
                />
            }
            <div className="context" onContextMenu={e => { e.preventDefault(); e.stopPropagation(); }}>
                <ul className="optionList">
                    {options.map((option, idx) => (
                        <li className="option" key={idx} onClick={option.onClick}>
                            {option.label}
                        </li>
                    ))}
                </ul>
            </div>
        </>
    )
}