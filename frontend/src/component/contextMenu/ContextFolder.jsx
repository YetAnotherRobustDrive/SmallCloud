import React, { useEffect, useState } from "react";
import '../../css/context.css';
import PostDeleteFolder from "../../services/directory/PostDeleteFolder";
import PostFavoriteFolder from "../../services/directory/PostFavoriteFolder";
import PostNewDirName from "../../services/directory/PostNewDirName";
import PostPurgeFolder from "../../services/directory/PostPurgeFolder";
import PostRestoreFolder from "../../services/directory/PostRestoreFolder";
import PostUnfavoriteFolder from "../../services/directory/PostUnfavoriteFolder";
import ModalFolderShare from "../modal/ModalFolderShare";
import ModalGetString from "../modal/ModalGetString";

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
        const res = await PostDeleteFolder(props.folderID);
        if (!res[0]) {
            alert(res[1]);
            return;
        }
        window.location.reload();
    }

    const handleFavorite = async (isAddFavorite) => {
        if (isAddFavorite) {
            const res = await PostFavoriteFolder(props.folderID);
            if (!res[0]) {
                if (res[1] === "이미 존재하는 라벨입니다.") {
                    alert("이미 즐겨찾기에 추가되어 있습니다.");
                    return;
                }
                alert(res[1]);
                return;
            }
            alert("즐겨찾기에 추가되었습니다.");
        }
        else {
            const res = await PostUnfavoriteFolder(props.folderID);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
            alert("즐겨찾기에서 삭제되었습니다.");
        }
        window.location.reload();
    }

    const handlePurge = async () => {
        const confirm = window.confirm("정말로 영구 삭제하시겠습니까?");
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

    const handleRestore = async () => {
        const confirm = window.confirm("정말로 복원하시겠습니까?");
        if (!confirm) {
            return;
        }
        const res = await PostRestoreFolder(props.folderID);
        if (!res[0]) {
            alert(res[1]);
            return;
        }
        alert("복원되었습니다.");
        window.location.reload();
    }

    console.log(props.isFavorite);
    let options = [];
    if (props.isFavorite === false) {
        options = [
            ...options, 
            { label: "즐겨찾기 추가", onClick: () => { handleFavorite(true) } }
        ];
    }
    else {
        options = [
            ...options, 
            { label: "즐겨찾기 삭제", onClick: () => { handleFavorite(false) } }
        ];
    }
    options = [
        ...options,
        { label: "이름 변경", onClick: () => { setIsModalOpen(true) } },
        { label: "공유 관리", onClick: () => { setIsShareOpen(true) } },
        { label: "폴더 삭제", onClick: () => { handleDelete() } },
    ];
    if (props.isDeleted) {
        options = [
            ...options,
            { label: "폴더 복원", onClick: () => { handleRestore() } },
            { label: "폴더 영구 삭제", onClick: () => { handlePurge() } },
        ];
    }

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