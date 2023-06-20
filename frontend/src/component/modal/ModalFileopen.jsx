import React, { useEffect, useState } from "react";
import { AiOutlineClose } from 'react-icons/ai';
import '../../css/fileview.css';
import GetImg from "../../services/file/GetImg";
import ModalLoading from "./ModalLoading";
import SwalError from "../swal/SwalError";
export default function ModalFileopen(props) {
    const [img, setImg] = useState(null);
    const [loading, setLoading] = useState(0);
    const imgType = ["png", "jpg", "jpeg", "gif", "bmp", "svg"];
    useEffect(() => {
        if (imgType.includes(props.type)) {
            const getImg = async () => {
                const res = await GetImg(props.id, setLoading);
                if (!res[0]) {
                    SwalError(res[1]);
                    return;
                }
                const reader = new FileReader();
                reader.readAsDataURL(res[1]);
                return new Promise((resolve) => {
                    reader.onload = () => {
                        setImg(reader.result);
                        resolve();
                    };
                });
            }
            getImg();
        }
        else if (props.type === "mpd") {
            const play = async () => {
                const innerBox = window.document.querySelector(".preview");
                const player = window.document.querySelector("video");
                player.hidden = false;
                player.style.width = innerBox.offsetWidth + "px";
                player.style.height = innerBox.offsetHeight + "px";
                player.style.position = "absolute";
                player.style.zIndex = "100";
                window.player.load(localStorage.getItem("API_SERVER") + "files/" + props.id + "?token=" + localStorage.getItem("accessToken"))
            }
            play();
        }
    }, [])

    return (
        <div className="fileopen">
            {loading !== 0 && <ModalLoading isOpen={loading !== 0} />}
            <div className="preview">
                {(!imgType.includes(props.type) && props.type !== "mpd") &&
                    <div className="inner">
                        <span>
                            "미디어 파일이 아닙니다."
                        </span>
                    </div>
                }
                {imgType.includes(props.type) &&
                    <img src={img} alt="loading..." className="inner" />
                }
            </div>
            <div className="fileopen-close" onClick={() => {
                window.document.querySelector("video").hidden = true;
                window.document.querySelector("video").pause();
                props.after()
            }
            }><AiOutlineClose /></div>
        </div>
    )

}