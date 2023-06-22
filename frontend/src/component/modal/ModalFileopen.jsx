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
    const videoFormats = ["mp4", "webm", "mkv", "avi", "mov", "wmv"];
    const [isEncoded, setIsEncoded] = useState(false);
    const [isLoaded, setIsLoaded] = useState(true);

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
            setIsLoaded(false);
        }
        else if (videoFormats.includes(props.type)) {
            const play = async () => {
                const res = await fetch(localStorage.getItem("API_SERVER") + "files/" + props.id + "/isEncoded", {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + localStorage.getItem("accessToken"),
                    },
                });
                if (res.status === 200) {
                    const data = await res.json();
                    setIsEncoded(data.result);
                    if (data.result === false) {
                        return;
                    }
                }
                else {
                    SwalError("영상을 불러오는데 실패했습니다.");
                    return;
                }
                const player = window.document.querySelector("video");
                player.hidden = false;
                player.style.width = "70%"
                player.style.height = "70%"
                player.style.position = "absolute";
                player.style.top = "55%";
                player.style.left = "40%";
                player.style.transform = "translate(-50%, -50%)";
                player.style.zIndex = "100";
                await window.player.load(localStorage.getItem("API_SERVER") + "files/" + props.id + "/mpd?token=" + localStorage.getItem("accessToken"))            }
            play();
        }
        setIsLoaded(false);
    }, [])

    return (
        <div className="fileopen">
            {loading !== 0 && <ModalLoading isOpen={loading !== 0} />}
            <div className="preview">
                {!isLoaded && <>
                    {(!imgType.includes(props.type) && !isEncoded) &&
                        <div className="inner">
                            <span>
                                "미디어 파일이 아닙니다."
                            </span>
                        </div>
                    }
                    {imgType.includes(props.type) &&
                        <img src={img} alt="loading..." className="inner" />
                    }
                </>
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