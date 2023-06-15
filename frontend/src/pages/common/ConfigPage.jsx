import React, { useEffect } from "react";
import configData from "../../config/config";
import logo_img from '../../config/img/logo.png';
import "../../css/login.css";
import "../../css/modal.css";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import ModalLoading from "../../component/modal/ModalLoading";

export default function ConfigPage() {
    const [isLoading, setIsLoading] = React.useState(false);

    const setConfig = (addr) => {
        localStorage.setItem('API_SERVER', addr);
    }

    const checkConnection = async (addr) => {
        try {
            setIsLoading(true);
            const res = await fetch(addr + 'ping', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + localStorage.getItem('token'),
                },
            });
            if (res.status === 200) {
                SwalAlert("success", "서버 연결에 성공하였습니다.", 
                () => {setConfig(addr); window.location.reload();});
            } else {
                SwalError("서버 연결에 실패하였습니다.");  
                throw new Error();
            }            
        } catch (error) {
            SwalError("서버 연결에 실패하였습니다.");            
        } finally {
            setIsLoading(false);
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        e.stopPropagation();
        const form = e.currentTarget;
        const ip = form.ip.value;
        const fullAddr = 'http://' + ip + ':8000/';
        checkConnection(fullAddr);
    }

    useEffect(() => {
        localStorage.clear();
    }, []);

    return (
        <form className="login" onSubmit={handleSubmit}>
            {isLoading && <ModalLoading isOpen={isLoading}/>}
            <img src={logo_img} alt="LOGO" />
            <span className="namespan">서버 주소를 입력해주세요.</span>
            <input name='ip' type="text" placeholder="000.000.000.000" autoFocus />
            <div className="buttons">
                <button className="link" >설정완료</button>
            </div>
        </form>
    )
}