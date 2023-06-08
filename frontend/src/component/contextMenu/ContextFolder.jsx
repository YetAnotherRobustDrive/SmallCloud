import React, { useEffect, useState } from "react";
import '../../css/context.css';
import PostNewDirName from "../../services/directory/PostNewDirName";
import ModalGetString from "../modal/ModalGetString";
import ModalFolderShare from "../modal/ModalFolderShare";
import PostPurgeFolder from "../../services/directory/PostPurgeFolder";
import PostFavoriteFolder from "../../services/directory/PostFavoriteFolder";

export default function ContextFolder(props) {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isShareOpen, setIsShareOpen] = useState(false);
    const [newName, setNewName] = useState();

    useEffect(() => {
        const rename = async () => {
            const res = await PostNewDirName(props.folderID, newName);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
            window.location.reload();
        }
        if (newName !== undefined && newName !== "") {
            rename();
        }
    }, [newName])

    const handleDelete = async () => {
        const confirm = window.confirm("정말로 삭제하시겠습니까?");
        if (!confirm) {
            return;
        }
        const res = await PostPurgeFolder(props.folderID);
        if (!res[0]) {
            alert(res[1]);
            return;
        }
        window.location.reload();
    }

    const handleFavorite = async () => {
        const res = await PostFavoriteFolder(props.folderID);
        if (!res[0]) {
            alert(res[1]);
            return;
        }
        alert("즐겨찾기에 추가되었습니다.");
    }

    const options = [
        { label: "즐겨찾기에 추가", onClick: () => { handleFavorite() } },
        { label: "이름 변경", onClick: () => { setIsModalOpen(true) } },
        { label: "공유 관리", onClick: () => { setIsShareOpen(true) } },
        { label: "폴더 삭제", onClick: () => { handleDelete() } },
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
            {isShareOpen &&
                <ModalFolderShare
                    folderID={props.folderID}
                    isOpen={isShareOpen}
                    after={() => setIsShareOpen(false)}
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