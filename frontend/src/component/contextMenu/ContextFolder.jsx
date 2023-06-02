import React, { useEffect, useState } from "react";
import '../../css/context.css';
import PostNewDirName from "../../services/directory/PostNewDirName";
import ModalGetString from "../modal/ModalGetString";

export default function ContextFolder(props) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isFail, setIsFail] = useState();
    const [message, setMessage] = useState();
    const [newName, setNewName] = useState();

    useEffect(() => {
        const rename = async () => {
            const res = await PostNewDirName(props.folderID, newName);
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
                return;
            }
            window.location.reload();
        }
        if (newName !== undefined && newName !== "") {
            rename();
        }
    }, [newName])

    const options = [
        { label: "이름 변경", onClick: () => { setIsModalOpen(true) } },
    ];

    return (
        <>
            {isModalOpen &&
                <ModalGetString
                    title={"폴더 이름 바꾸기"}
                    placeholder={"새 이름"}
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