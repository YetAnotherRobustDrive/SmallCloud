import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import GetRootDir from "../../services/directory/GetRootDir";
import PostNewDir from "../../services/directory/PostNewDir";
import ModalGetString from "../modal/ModalGetString";
import '../../css/context.css'

export default function ContextBody() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isFail, setIsFail] = useState();
    const [message, setMessage] = useState();
    const [newName, setNewName] = useState();
    const params = useParams();

    useEffect(() => {
        const mkFile = async () => {
            let curr = params.fileID;
            if (curr === undefined) {     
                const rootIDRes = await GetRootDir();
                if (!rootIDRes[0]) {
                    setIsFail(true);
                    setMessage(rootIDRes[1]);
                    return;
                }    
                curr = rootIDRes[1];
            }
            const res = await PostNewDir(curr, newName);
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]); 
                return;               
            }
            window.location.reload();
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
                    setter={setNewName}
                    isOpen={isModalOpen}
                    after={() => setIsModalOpen(false)}
                />
            }
            <div className="context">
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